package com.xenoage.zong.io.musicxml.in;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import com.xenoage.zong.io.musicxml.FileType;


/**
 * Test class for the {@link FileTypeReader} class.
 * 
 * @author Andreas Wenger
 */
public class FileTypeReaderTest
{
	
	@Test public void getFileTypeTest()
		throws IOException
	{
		File dir = new File("data/test/FileTypeReaderTest");
		assertEquals(FileType.XMLScorePartwise,
			FileTypeReader.getFileType(new FileInputStream(new File(dir, "BeetAnGeSample.xml"))));
		assertEquals(FileType.Compressed,
			FileTypeReader.getFileType(new FileInputStream(new File(dir, "BrahWiMeSample.mxl"))));
		assertEquals(FileType.XMLOpus,
			FileTypeReader.getFileType(new FileInputStream(new File(dir, "SomeOpus.xml"))));
	}

}
