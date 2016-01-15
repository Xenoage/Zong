package com.xenoage.zong.demos.simplegui;

import java.io.File;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.desktop.io.DocumentIO;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreDocFileOutput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreDocFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.midi.out.PlaybackListener;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.layouter.PlaybackLayouter;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * The loaded document, its layout and playback capabilities.
 * 
 * @author Andreas Wenger
 */
public class Content
	implements PlaybackListener {
	
	private MainWindow mainWindow;
	
	private int scoreIndex = 0;
	private ScoreDoc scoreDoc = null;
	private Layout layout = null;
	private PlaybackLayouter playbackLayouter = null;
	
	
	public Content(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		//listen for playback events (see method playbackAtMP)
		Playback.registerListener(this);	
	}
	
	/**
	 * Loads the next MusicXML demo score from the scores directory.
	 */
	public void loadNextScore() {
		File[] files = new File("scores").listFiles((d, n) -> n.endsWith(".xml"));
		loadScore("scores/" + files[scoreIndex].getName());
		scoreIndex = (scoreIndex + 1) % files.length;
	}
	
	/**
	 * Loads the MusicXML score from the given file path.
	 */
	private void loadScore(String filePath) {
		try {
			//stop current playback
			Playback.stop();
			//load the score
			scoreDoc = DocumentIO.read(new File(filePath), new MusicXmlScoreDocFileInput());
			//layout the first page
			layout = scoreDoc.getLayout();
			Score score = scoreDoc.getScore();
			layout.updateScoreLayouts(score);
			//create playback layouter for the playback cursor
			playbackLayouter = new PlaybackLayouter(layout.getScoreFrameChain(score).getScoreLayout());
			//set image to view
			mainWindow.renderLayout(layout);
			//load score into MIDI playback
			Playback.openScore(scoreDoc.getScore());
		}
		catch (Exception ex) {
			Err.handle(Report.error(ex));
		}
	}
	
	/**
	 * Saves the current score using the given format.
	 * pdf, png, mid and ogg is supported by this demo.
	 */
	public void saveAs(String format) {
		FileOutput<ScoreDoc> out = null;
		switch (format) {
			case "pdf": out = new PdfScoreDocFileOutput(); break;
			case "png": out = new PngScoreDocFileOutput(); break;
			case "mid": out = new MidiScoreDocFileOutput(); break;
			case "ogg": out = new OggScoreDocFileOutput(); break;
			default: return;
		}
		String filePath = "demo." + format;
		try {
			DocumentIO.write(scoreDoc, new File(filePath), out);
			mainWindow.showMessageDialog(filePath + " saved.");
		} catch (Exception ex) {
			Err.handle(Report.error(ex));
		}
	}
	
	/**
	 * This method is called when the mouse was clicked on the content.
	 * A message with the clicked element is shown to the user. 
	 */
	public void onClick(Point2f positionPx) {
		//get the layout of first score frame
		ScoreFrame frame = layout.getScoreFrames().get(0);
		ScoreFrameLayout frameLayout = frame.getScoreFrameLayout();
		//convert position from screen space to page space, then from page space
		//to frame space, and them from frame space to score frame space
		Point2f positionMm = positionPx.scale(Units.pxToMm(1, MainWindow.zoom));
		Point2f framePositionMm = positionMm.sub(frame.getAbsolutePosition());
		Point2f scorePositionMm = frame.getScoreLayoutPosition(framePositionMm);
		//find elements under this position
		for (Stamping stamping : frameLayout.getAllStampings()) {
			if (stamping.getBoundingShape() != null &&
				stamping.getBoundingShape().contains(scorePositionMm)) {
				MusicElement element = stamping.getMusicElement();
				if (element != null) {
					//music element found
					String message = "An element was clicked: " + element;
					if (element instanceof MPElement) {
						//music element with a known musical position found
						MPElement mpElement = (MPElement) element;
						if (mpElement.getParent() != null)
							message += " at " + mpElement.getMP();
					}
					mainWindow.showMessageDialog(message);
				}
			}
		}
	}
	
	/**
	 * This method is called by the MIDI playback whenever a new
	 * musical position is reached.
	 */
	@Override public void playbackAtMP(MP mp, long ms) {
		//update cursor position and redraw the layout
		playbackLayouter.setCursorAt(mp);
		mainWindow.renderLayout(layout);
	}

	@Override public void playbackAtMs(long ms) {
	}

	@Override public void playbackStarted() {
	}

	@Override public void playbackPaused() {
	}

	@Override public void playbackStopped() {
	}

	@Override public void playbackAtEnd() {
	}

}
