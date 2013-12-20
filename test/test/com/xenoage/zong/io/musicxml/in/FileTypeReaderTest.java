package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.xenoage.zong.io.musicxml.FileType;

/**
 * Test class for the {@link FileTypeReader} class.
 * 
 * @author Andreas Wenger
 */
public class FileTypeReaderTest {

	@Test public void getFileTypeTest()
		throws IOException {
		String dir = "data/test/scores/FileTypeReaderTest/";
		assertEquals(FileType.XMLScorePartwise,
			FileTypeReader.getFileType(platformUtils().openFile(dir + "BeetAnGeSample.xml")));
		assertEquals(FileType.Compressed,
			FileTypeReader.getFileType(platformUtils().openFile(dir + "BrahWiMeSample.mxl")));
		assertEquals(FileType.XMLOpus,
			FileTypeReader.getFileType(platformUtils().openFile(dir + "SomeOpus.xml")));
	}

}
