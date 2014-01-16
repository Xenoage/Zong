package com.xenoage.zong;

import static com.xenoage.utils.jse.io.JseFileUtils.listFilesDeep;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This test tries to load and layout a huge range of MusicXML files.
 * 
 * @author Andreas Wenger
 */
public class MusicXmlMassTest {

	private static final String dir = //*
	"C:/Users/andi/Werkstatt/Zong-Test/";
	//"/home/andi/Werkstatt/Zong-Test/MusicXML/Andi/MusicXML 3.0 - Dolet 6 Beta 1/";

	SymbolPool symbolPool;


	@Before public void setUp() {
		PlatformUtils.init(new JsePlatformUtils());
		LangManager.loadLanguage("en");
	}

	/**
	 * We check all .xml files in the {@link #dir} directory
	 * (and its subdirectories recursively).
	 */
	@Test public void testSampleFiles() {
		int ok = 0;
		Collection<File> files = listFilesDeep(new File(dir), JseFileUtils.getXMLFilter());
		System.out.println("Processing " + files.size() + " files...");
		for (File file : files) {
			if (testFile(file))
				ok++;
		}
		System.out.println("Could load " + ok + " of " + files.size() + " files (" +
			new DecimalFormat("#.##").format(100f * ok / files.size()) + "%)");
		if (ok < files.size())
			Assert.fail();
	}

	/*
	@Test public void testSingleFile()
	{
		File file = new File(dir + "/" + "jsbchorales.net/043300B_.xml");
		if (!testFile(file)) Assert.fail();
	} //*/

	private boolean testFile(File file) {
		try {
			ScoreDoc score = new MusicXmlScoreDocFileInput().read(new JseInputStream(file),
				file.getAbsolutePath());
			String tempFile = "temp.xml";
			//TODO new MusicXMLScoreDocFileOutput().write(score, new FileOutputStream(tempFile), tempFile);

			//*
			System.out.println("OK:   " + file.toString().substring(dir.length()) + " (" +
			score.getScore().getInfo().getTitle() + ")");
			/*/
			System.out.print("."); //*/
			new File(tempFile).delete();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			//fail("Failed to load file: " + file);
			System.out.println("fail: " + file.toString().substring(dir.length()));
			return false;
		}
	}

}
