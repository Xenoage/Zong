package com.xenoage.zong.test;

import static com.xenoage.utils.xml.XMLReader.readFile;
import static com.xenoage.zong.musicxml.util.MusicXMLFilenameFilter.musicXMLFilenameFilter;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.w3c.dom.Document;

import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.zong.musicxml.MusicXMLDocument;


/**
 * Tests the loading of all MusicXML demo files.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLDemoFilesTest
{
	
	private String dirs[] = {
		"../shared/data/test/musicxml11",
		"../shared/data/test/musicxml20"};
	
	
	@Test public void testLoading()
		throws Exception
	{
		long totalXMLLoadingTime = 0;
		long totalMusicXMLReadingTime = 0;
		long lastTime = 0;
		for (String dir : dirs)
		{
			for (File file : FileUtils.listFiles(new File(dir), musicXMLFilenameFilter, false))
			{
				System.out.println(file);
				lastTime = System.currentTimeMillis();
				Document doc = readFile(new FileInputStream(file));
				totalXMLLoadingTime += (System.currentTimeMillis() - lastTime);
				try
				{
					lastTime = System.currentTimeMillis();
					MusicXMLDocument.read(doc);
					
					//TEST
					//MusicXMLDocument score = MusicXMLDocument.read(doc);
					//Document d = score.getScore().write();
					//XMLWriter.writeFile(d, new FileOutputStream("test-output.xml"));
					
					totalMusicXMLReadingTime += (System.currentTimeMillis() - lastTime);
				}
				catch (XmlDataException ex)
				{
					throw new Exception(
						"Failed for " + dir + "/" + file.getName() + ": " + ex.getMessage(),
						ex);
				}
			}
		}
		//print time
		System.out.println("Total XML loading time:      " + totalXMLLoadingTime);
		System.out.println("Total MusicXML reading time: " + totalMusicXMLReadingTime);
	}
	

}
