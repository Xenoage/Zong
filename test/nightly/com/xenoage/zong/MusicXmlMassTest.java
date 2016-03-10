package com.xenoage.zong;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.jse.io.JseFileUtils.listFilesDeep;
import static com.xenoage.zong.musicxml.util.MusicXMLFilenameFilter.musicXMLFilenameFilter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This test tries to load and layout a huge range of MusicXML files.
 * 
 * If the files do not exist, nothing is tested. This class allows to test
 * a large number of files locally, which can not be uploaded to the
 * public repository because of copyright restrictions.
 * 
 * This test should be excluded from the normal test suite and
 * should only be started manually.
 * 
 * @author Andreas Wenger
 */
public class MusicXmlMassTest {

	private static final String dir = "../../Zong-Test/";


	/**
	 * We check all .xml files in the {@link #dir} directory
	 * (and its subdirectories recursively).
	 * Files and folders containing "__" are ignored.
	 */
	@Test public void testSampleFiles() {
		int ok = 0;
		List<File> files = listFilesDeep(new File(dir), musicXMLFilenameFilter);
		//remove all files called "mei.xml", they include not MusicXML but MEI
		files.removeIf(f -> f.getName().equals("mei.xml"));
		//remove all files and directories with prefix "__"
		files.removeIf(f -> f.getAbsolutePath().contains("__"));
		System.out.println("Processing " + files.size() + " files...");
		It<File> filesIt = it(files);
		for (File file : filesIt) {
			if (testFile(file))
				ok++;
			else Assert.fail();
			if (filesIt.getIndex() % 10 == 0)
				System.out.println("Progress: " + (100 * filesIt.getIndex() / files.size()) + "%");
		}
		System.out.println("Could load " + ok + " of " + files.size() + " files (" +
			new DecimalFormat("#.##").format(100f * ok / files.size()) + "%)");
		if (ok < files.size())
			Assert.fail();
	}

	/*
	@Test public void testSingleFile()
	{
		File file = new File(dir + "MusicXML/hausmusik.ch/z/zschille/kantate/jauchzet_dem_herrn/fuerchte_dich_nicht/kan_4.xml");
		if (!testFile(file)) Assert.fail();
	} //*/

	private boolean testFile(File file) {
		try {
			ScoreDoc score = new MusicXmlScoreDocFileInput().read(new JseInputStream(file),
				file.getAbsolutePath());
			String tempFile = "temp.xml";
			//TODO new MusicXMLScoreDocFileOutput().write(score, new FileOutputStream(tempFile), tempFile);

			//*
			System.out.print("OK:   " + file.toString().substring(dir.length()) + " (" +
			score.getScore().getInfo().getTitle() + ")");
			@SuppressWarnings("unchecked") List<String> errorMessages =
				(List<String>) score.getScore().getMetaData().get("mxlerrors");
			if (errorMessages != null)
				System.out.print("  ! " + errorMessages.size() + " warning(s)");
			System.out.println();
			
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
