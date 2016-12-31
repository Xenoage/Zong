package musicxmltestsuite.report;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.io.JseStreamUtils;
import com.xenoage.utils.jse.reflection.InterfaceInstance;
import com.xenoage.zong.desktop.io.DocumentIO;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.documents.ScoreDoc;
import musicxmltestsuite.MusicXmlTestSuiteHtmlReport;
import musicxmltestsuite.tests.base.Base;
import musicxmltestsuite.tests.utils.ErroneousScore;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static java.util.Comparator.comparing;
import static musicxmltestsuite.MusicXmlTestSuiteHtmlReport.dirReport;

/**
 * Creates an HTML page, which brings the MusicXML test files
 * rendered with Lilypond face to face with their respective
 * Zong! renderings.
 *
 * Can be started with the main method or by running the tests
 * in {@link MusicXmlTestSuiteHtmlReport}.
 *
 * ImageMagick should be installed to crop the resulting images.
 *
 * @author Andreas Wenger
 */
public class VisualHtmlReport {

	private static final String appDir = "visual/app/";
	private static final String lilypondDir = "visual/lilypond/";

	private List<Base> tests;


	public static void run()
			throws Throwable {
		new VisualHtmlReport().start();
	}

	private void start()
			throws Throwable {
		collectTests();
		prepareDirectories();
		for (Base test : tests) {
			System.out.println("Processing " + test.getFileName() + " ...");
			File scoreFile = io().findNormalFile(Base.dirPath + test.getFileName());
			try {
				renderScore(scoreFile);
				copyLilypondImage(scoreFile);
			} catch (Exception ex) {
				throw new Exception("Error with " + scoreFile.getName(), ex);
			}
		}
		writeToHtmlFile();
	}

	/**
	 * Gets all {@link Base} tests, except those who contain invalid scores (see {@link ErroneousScore}),
	 * sorted by name.
	 */
	private void collectTests()
			throws Exception {
		List<Base> tests = alist();
		Reflections reflections = new Reflections("musicxmltestsuite.tests.base");
		for (Class<? extends Base> testClass : reflections.getSubTypesOf(Base.class)) {
			//ignore erroneous scores
			if (false == testClass.isAnnotationPresent(ErroneousScore.class)) {
				//easyMock library fails; it does not recognize the default methods; so we
				//use our own solution to instantiate the interface
				tests.add((Base) new InterfaceInstance(testClass).defineClass().newInstance());
			}
		}
		tests.sort(comparing(Base::getFileName));
		this.tests = tests;
	}

	private static void prepareDirectories() {
		for (String subdir : new String[]{appDir, lilypondDir}) {
			File dir = new File(dirReport, subdir);
			JseFileUtils.deleteDirectory(dir);
			dir.mkdirs();
		}
	}

	/**
	 * Renders the given score into an image file.
	 */
	private void renderScore(File scoreFile)
			throws IOException {
		ScoreDoc doc = DocumentIO.read(scoreFile, new MusicXmlScoreDocFileInput());
		File pngFile = new File(new File(dirReport, appDir), scoreFile.getName() + ".png");
		DocumentIO.write(doc, pngFile, new PngScoreDocFileOutput());
		cropScoreImage(pngFile);
	}

	/**
	 * Crops the empty margins of the given PNG file.
	 * ImageMagick (mogrify) needs to be installed, otherwise the file will not be changed.
	 */
	private void cropScoreImage(File outFile) {
		try {
			ProcessBuilder imageMagick = new ProcessBuilder("mogrify", "-trim", "-crop", "+0-80",
					"+repage", "-trim", outFile.getAbsolutePath());
			imageMagick.start();
		} catch (Exception ex) {
		}
	}

	/**
	 * Copies the lilypond rendering of the given score file into the output directory.
	 * The lilypond rendering is expected at the path of the score file with ".png" added.
	 * @param scoreFile
	 */
	private void copyLilypondImage(File scoreFile)
			throws IOException {
		File lilypondImage = new File(scoreFile.getAbsolutePath() + ".png");
		Files.copy(lilypondImage.toPath(), new File(new File(dirReport, lilypondDir), scoreFile.getName() + ".png").toPath());
	}

	public void writeToHtmlFile() {
		String html = createHtmlReport();
		JseFileUtils.writeFile(html, getHtmlFilePath());
	}

	private String getHtmlFilePath() {
		return dirReport + "visual.html";
	}

	private String createHtmlReport() {
		String html = loadHtmlTemplate("template");
		html = html.replace("[[date]]", Instant.now().toString());
		html = html.replace("[[rows]]", createHtmlRows());
		return html;
	}

	private String loadHtmlTemplate(String name) {
		return JseStreamUtils.readToString(
				getClass().getResourceAsStream("templates/visual/" + name + ".html"));
	}

	private String createHtmlRows() {
		StringBuilder ret = new StringBuilder();
		for (Base test : tests)
			ret.append(createHtmlRow(test));
		return ret.toString();
	}

	private String createHtmlRow(Base test) {
		String template = loadHtmlTemplate("row");
		template = template.replace("[[test-name]]", test.getFileName());
		template = template.replace("[[lilypond-score]]", createHtmlCell(test, lilypondDir));
		template = template.replace("[[app-score]]", createHtmlCell(test, appDir));
		return template;
	}

	private String createHtmlCell(Base test, String dir) {
		String template = loadHtmlTemplate("cell");
		String file = new File(dir, test.getFileName() + ".png").getPath();
		template = template.replace("[[score-path]]", ""+file);
		return template;
	}

}
