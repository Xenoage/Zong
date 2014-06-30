package com.xenoage.zong;

import static com.xenoage.utils.io.FileFilters.xmlFilter;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.jse.io.JseFileUtils.getFilter;
import static com.xenoage.utils.jse.io.JseFileUtils.listFilesDeep;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;

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
 * @author Andreas Wenger
 */
public class MusicXmlMassTest {

	private static final String dir = "../../Zong-Test/";


	/**
	 * We check all .xml files in the {@link #dir} directory
	 * (and its subdirectories recursively).
	 */
	@Test public void testSampleFiles() {
		int ok = 0;
		Collection<File> files = listFilesDeep(new File(dir), getFilter(xmlFilter));
		System.out.println("Processing " + files.size() + " files...");
		It<File> filesIt = it(files);
		for (File file : filesIt) {
			if (testFile(file))
				ok++;
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
		File file = new File(dir + "/MusicXML/MusicXML 1.1/MozaChloSample.xml");
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
