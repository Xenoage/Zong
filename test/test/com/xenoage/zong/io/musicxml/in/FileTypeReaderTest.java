package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
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
			FileTypeReader.getFileType(jsePlatformUtils().openFile(dir + "BeetAnGeSample.xml")));
		assertEquals(FileType.Compressed,
			FileTypeReader.getFileType(jsePlatformUtils().openFile(dir + "BrahWiMeSample.mxl")));
		assertEquals(FileType.XMLOpus,
			FileTypeReader.getFileType(jsePlatformUtils().openFile(dir + "SomeOpus.xml")));
	}

}
