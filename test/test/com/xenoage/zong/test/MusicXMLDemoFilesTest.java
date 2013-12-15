package com.xenoage.zong.test;

import static com.xenoage.zong.musicxml.util.PlainMusicXMLFilenameFilter.plainMusicXMLFilenameFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

import com.xenoage.utils.jse.io.FileUtils;
import com.xenoage.utils.jse.xml.JseXmlReader;
import com.xenoage.utils.jse.xml.JseXmlWriter;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.MusicXMLDocument;

/**
 * Tests the loading of all MusicXML demo files.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLDemoFilesTest {

	private String dirs[] = { "../shared/data/test/scores/musicxml11",
		"../shared/data/test/scores/musicxml20" };


	@Test public void testLoading()
		throws Exception {
		long totalXMLLoadingTime = 0;
		long totalMusicXMLReadingTime = 0;
		long lastTime = 0;
		for (String dir : dirs) {
			for (File file : FileUtils.listFiles(new File(dir), plainMusicXMLFilenameFilter, false)) {
				System.out.println(file);
				lastTime = System.currentTimeMillis();
				XmlReader reader = new JseXmlReader(new FileInputStream(file));
				totalXMLLoadingTime += (System.currentTimeMillis() - lastTime);
				try {
					lastTime = System.currentTimeMillis();
					MusicXMLDocument doc = MusicXMLDocument.read(reader);

					//TEST
					//MusicXMLDocument score = MusicXMLDocument.read(doc);
					//Document d = score.getScore().write();
					//XMLWriter.writeFile(d, new FileOutputStream("test-output.xml"));

					//TEST
					doc.write(new JseXmlWriter(new FileOutputStream("test.xml")));

					totalMusicXMLReadingTime += (System.currentTimeMillis() - lastTime);
				} catch (XmlException ex) {
					throw new Exception("Failed for " + dir + "/" + file.getName() + ": " + ex.getMessage(),
						ex);
				}
			}
		}
		//print time
		System.out.println("Total XML loading time:      " + totalXMLLoadingTime);
		System.out.println("Total MusicXML reading time: " + totalMusicXMLReadingTime);
	}

}
