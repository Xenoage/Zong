package com.xenoage.zong.webserver.actions;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.zong.webserver.util.Database.delete;
import static com.xenoage.zong.webserver.util.Database.exists;
import static com.xenoage.zong.webserver.util.Database.insert;
import static com.xenoage.zong.webserver.util.Database.stmt;
import static com.xenoage.zong.webserver.util.Database.unixTime;
import static com.xenoage.zong.webserver.util.Response.writeSuccess;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.io.JseStreamUtils;
import com.xenoage.utils.jse.io.URLUtils;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.mp3.out.Mp3ScoreFileOutput;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreFileOutput;
import com.xenoage.zong.desktop.renderer.AwtBitmapPageRenderer;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.webserver.Webserver;
import com.xenoage.zong.webserver.io.CursorOutput;
import com.xenoage.zong.webserver.model.Doc;
import com.xenoage.zong.webserver.model.Page;
import com.xenoage.zong.webserver.model.ScaledPage;
import com.xenoage.zong.webserver.model.Scaling;
import com.xenoage.zong.webserver.model.requests.OpenRequest;
import com.xenoage.zong.webserver.model.requests.Request;
import com.xenoage.zong.webserver.util.Database;
import com.xenoage.zong.webserver.util.WorkerThread;

/**
 * Responds to an {@link OpenRequest}, that means, a file
 * it is downloaded, opened, layouted and rendered and information
 * about it is send to the client.
 * 
 * If the document is still in the cache, it is not not loaded again.
 * The cache is cleaned after the time defined in the settings.
 * 
 * @author Andreas Wenger
 */
public class OpenAction
	extends Action {

	@Override public void perform(Request request, Webserver server, HttpServletResponse response)
		throws SQLException, IOException {
		OpenRequest openRequest = getAs(OpenRequest.class, request);

		log(remark("OpenAction started for URL " + openRequest.url));
		final Connection db = server.getDBConnection();

		//cleanup: delete documents which were not used during a defined period
		PreparedStatement stmtDelete = stmt(db, "SELECT id FROM docs WHERE last_access < ?",
			unixTime() - Integer.parseInt(server.getSetting("cachetime")));
		ResultSet resDelete = stmtDelete.executeQuery();
		while (resDelete.next()) {
			//TODO: automatically cascade
			int deleteID = resDelete.getInt(1);
			delete(db, "audio", "doc_id = ?", deleteID);
			delete(db, "cursors", "doc_id = ?", deleteID);
			delete(db, "pages", "doc_id = ?", deleteID);
			delete(db, "pageinfos", "doc_id = ?", deleteID);
			delete(db, "scaledpageinfos", "doc_id = ?", deleteID);
			delete(db, "docs", "id = ?", deleteID);
		}
		stmtDelete.close();

		//test
		//long startTime = System.currentTimeMillis();

		//see if document is already in database. if not, load it from the URL.
		ScoreDoc scoreDoc = null;
		Doc doc = null;
		PreparedStatement stmtDoc = stmt(db, "SELECT id FROM docs WHERE url = ?", openRequest.url);
		ResultSet resDoc = stmtDoc.executeQuery();
		if (resDoc.next()) {
			//the document already exists in the database
			log(remark("Requested document is still in cache. Using it."));
			doc = Doc.fromDB(db, openRequest.url);
		}
		else {
			//the document is unknown. load it.
			log(remark("Requested document is not in cache. Loading it."));
			Tuple2<ScoreDoc, Doc> t = loadDocument(openRequest.url, openRequest.requestedID);
			scoreDoc = t.get1();
			doc = t.get2();
		}
		stmtDoc.close();

		//load size of first page
		PreparedStatement stmtFirstPageSize = stmt(db,
			"SELECT width, height FROM pageinfos WHERE doc_id = ? AND page = 0", doc.id);
		ResultSet resFirstPageSize = stmtFirstPageSize.executeQuery();
		resFirstPageSize.next();
		Size2f firstPageSize = new Size2f(resFirstPageSize.getFloat(1), resFirstPageSize.getFloat(2));
		stmtFirstPageSize.close();

		//scalings are saved as value*72dpi/10000 in the database.
		//convert all requested scalings to this format
		final LinkedList<Integer> requestedScalings = llist();
		for (Scaling scaling : openRequest.scalings) {
			requestedScalings.add(scaling.convertTo10000(firstPageSize));
		}

		//find all requested scalings, that are not already available in the database
		final LinkedList<Integer> scalingsToRender = llist();
		for (int scaling : requestedScalings) {
			boolean scalingExists = (ScaledPage.fromDB(db, doc.id, 0, scaling) != null);
			if (!scalingExists) {
				scalingsToRender.add(scaling);
				//if ScoreDoc was not loaded yet, load it now
				if (scoreDoc == null)
					scoreDoc = loadDocument(openRequest.url, openRequest.requestedID).get1();
				//save information about scaled pages
				List<com.xenoage.zong.layout.Page> pages = scoreDoc.getLayout().getPages();
				for (int iPage : range(pages)) {
					com.xenoage.zong.layout.Page page = pages.get(iPage);
					Size2f pageSize = page.getFormat().getSize();
					ScaledPage scaledPage = new ScaledPage(doc.id, iPage, scaling,
						Units.mmToPxInt(pageSize.width, scaling / 10000f),
						Units.mmToPxInt(pageSize.height, scaling / 10000f));
					scaledPage.insertIntoDB(db);
				}
			}
		}

		//decide, if we have something to do
		final boolean renderPages = (scalingsToRender.size() > 0);
		final boolean renderAudio = !exists(db, "audio", "doc_id = ?", doc.id);
		final boolean renderCursor = !exists(db, "cursors", "doc_id = ?", doc.id);
		if (renderPages || renderAudio || renderCursor) {
			//if ScoreDoc was not loaded yet, load it now
			if (scoreDoc == null)
				scoreDoc = loadDocument(openRequest.url, openRequest.requestedID).get1();
		}

		//from here on, try to do things in parallel

		//first thread: render pages and save them in the database
		final ScoreDoc scoreDocFinal = scoreDoc;
		final Doc docFinal = doc;
		final ArrayList<Page> pages = alist();
		final ArrayList<ArrayList<ScaledPage>> scaledPages = alist();
		Thread threadPages = new WorkerThread() {

			@Override public void runTry()
				throws Exception {
				if (renderPages) {

					//render the pages
					for (int scaling : scalingsToRender) {
						List<BufferedImage> pages = renderTiles(scoreDocFinal.getLayout(), scaling / 10000f);
						for (int iPage : range(pages)) {
							BufferedImage page = pages.get(iPage);

							//write page
							ByteArrayOutputStream imageData = new ByteArrayOutputStream();
							ImageIO.write(page, "png", imageData);
							insert(db, "pages", "doc_id, page, scaling, image", docFinal.id, iPage, scaling,
								imageData.toByteArray());
						}
					}
					log(remark("Rendered " + scoreDocFinal.getLayout().getPages().size() + " pages at " +
						scalingsToRender + " scalings"));
				}

				//collect pages for response
				for (int iPage : range(docFinal.pages)) {
					pages.add(Page.fromDB(db, docFinal.id, iPage));
				}

				//collect scaled pages for response
				for (int iPage : range(docFinal.pages)) {
					ArrayList<ScaledPage> sp = alist();
					for (int scaling : requestedScalings) {
						sp.add(ScaledPage.fromDB(db, docFinal.id, iPage, scaling));
					}
					scaledPages.add(sp);
				}
				System.out.println("pages finished"); //TEST
			}
		};
		threadPages.start();

		//second and third thread: render audio files, if not already in the cache
		Thread threadOgg = new WorkerThread() {

			@Override public void runTry()
				throws Exception {
				//render OGG file
				if (renderAudio)
					renderAndSaveAudioFile(db, docFinal, scoreDocFinal, "OGG", new OggScoreFileOutput());
				System.out.println("ogg finished"); //TEST
			}
		};
		threadOgg.start();
		Thread threadMp3 = new WorkerThread() {

			@Override public void runTry()
				throws Exception {
				//render MP3 file
				if (renderAudio)
					renderAndSaveAudioFile(db, docFinal, scoreDocFinal, "MP3", new Mp3ScoreFileOutput());
				System.out.println("mp3 finished"); //TEST
			}
		};
		threadMp3.start();

		//fourth thread: render cursor file, if not already in the cache
		Thread threadCursor = new WorkerThread() {

			@Override public void runTry()
				throws Exception {
				//create cursor data
				if (renderCursor) {
					log(remark("Creating cursor data"));
					JsonObject jsonCursor = new CursorOutput().write(scoreDocFinal);
					insert(db, "cursors", "doc_id, cursors", docFinal.id, jsonCursor.toString());
					System.out.println("cursors finished"); //TEST
				}
			}
		};
		threadCursor.start();

		//wait until all threads are finished
		try {
			threadPages.join();
			threadOgg.join();
			threadMp3.join();
			threadCursor.join();
		} catch (InterruptedException e) {
			throw new RuntimeException("interrupted");
		}

		//create response message
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("id", "" + doc.publicID);
		JsonArray jsonPages = new JsonArray();
		for (int iPage : range(pages)) {
			Page page = pages.get(iPage);
			JsonObject jsonPage = new JsonObject();
			jsonPage.addProperty("width", page.width);
			jsonPage.addProperty("height", page.height);
			JsonArray jsonScalesPages = new JsonArray();
			for (ScaledPage sp : scaledPages.get(iPage))
				jsonScalesPages.add(Webserver.instance.getGson().toJsonTree(sp));
			jsonPage.add("scaledPages", jsonScalesPages);
			jsonPages.add(jsonPage);
		}
		jsonResponse.add("pages", jsonPages);

		//test
		//long endTime = System.currentTimeMillis();
		//System.out.println("total time: " + (endTime - startTime));

		//send success response
		writeSuccess(response, jsonResponse);
	}

	//TODO: try-with-resources
	private void renderAndSaveAudioFile(Connection db, Doc doc, ScoreDoc scoreDoc,
		String audioFormatID, FileOutput<Score> scoreFileOutput)
		throws IOException, SQLException {
		log(remark("Rendering " + audioFormatID + " audio file"));
		File tempFile = File.createTempFile(getClass().getName(), "." + audioFormatID.toLowerCase());
		scoreFileOutput.write(scoreDoc.getScore(), new JseOutputStream(new FileOutputStream(tempFile)),
			tempFile.getAbsolutePath());
		byte[] bytes = JseStreamUtils.readToByteArray(new FileInputStream(tempFile));
		if (bytes == null)
			throw new IOException("Could not read " + audioFormatID + " file");
		insert(db, "audio", "doc_id, format, audio", doc.id, audioFormatID, bytes);
		tempFile.delete();
	}

	/**
	 * Loads the {@link Doc} at the given URL and stores information about
	 * the score in the database, if it is not already present.
	 */
	public Tuple2<ScoreDoc, Doc> loadDocument(String url, @MaybeNull UUID publicID)
		throws SQLException {
		Connection db = Webserver.instance.getDBConnection();
		ScoreDoc scoreDoc;

		//public ID of the document
		if (publicID == null)
			publicID = UUID.randomUUID();
		//may not exist yet
		PreparedStatement stmt = stmt(db, "SELECT public_id FROM docs WHERE public_id = ?", publicID);
		ResultSet res = stmt.executeQuery();
		boolean error = res.next();
		stmt.close();
		if (error)
			throw new SQLException("A document with this public ID already exists");

		//load MusicXML document
		try {
			//open local or remote file
			InputStream inputStream;
			if (URLUtils.isAbsoluteURL(url)) {
				inputStream = new URL(url).openStream();
			}
			else {
				inputStream = new FileInputStream(Webserver.webPath + url);
			}
			MusicXmlScoreDocFileInput in = new MusicXmlScoreDocFileInput();
			scoreDoc = in.read(new JseInputStream(inputStream), null);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("file not found");
		} catch (MalformedURLException ex) {
			throw new RuntimeException("invalid URL: " + url);
		} catch (IOException ex) {
			throw new RuntimeException("can not read from URL: " + url);
		}

		//register file in database, if not already known
		Layout layout = scoreDoc.getLayout();
		boolean isDocKnown = Database.exists(db, "docs", "url = ?", "" + url);
		if (!isDocKnown) {
			Database.insert(db, "docs", "url, public_id, pages, last_access", "" + url, "" + publicID,
				layout.getPages().size(), unixTime());
		}

		//read information about the document
		Doc doc = Doc.fromDB(db, "" + url);

		//for new documents: save information
		if (!isDocKnown) {
			//page information
			for (int iPage : range(layout.getPages())) {
				Size2f pageSize = layout.getPages().get(iPage).getFormat().getSize();
				new Page(doc.id, iPage, pageSize.width, pageSize.height).insertIntoDB(db);
			}
		}

		return t(scoreDoc, doc);
	}

	/**
	 * Renders the tiles. This function is single-threaded.
	 * (Otherwise we get strange artefacts. The AWT renderer seems not to be thread-safe)
	 */
	private static synchronized List<BufferedImage> renderTiles(Layout layout, float scaling) {
		List<BufferedImage> ret = alist();
		for (int iPage : range(layout.getPages())) {
			ret.add(AwtBitmapPageRenderer.paint(layout, iPage, scaling));
		}
		return ret;
	}

}
