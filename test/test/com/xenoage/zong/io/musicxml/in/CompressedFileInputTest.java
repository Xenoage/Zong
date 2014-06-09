package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * Test class for the {@link CompressedFileInput} class.
 * 
 * @author Andreas Wenger
 */
public class CompressedFileInputTest {

	@Test public void test()
		throws Exception {
		CompressedFileInput zip = new CompressedFileInput(jsePlatformUtils().openFile(
			"data/test/scores/MxlCompressedFileInputTest/album.mxl"));
		//root item is an opus
		assertTrue(zip.isOpus());
		//check flattened list of scores and load the files
		List<String> scores = zip.getScoreFilenames();
		assertEquals(4, scores.size());
		String[] scoresExpected = new String[] { "BeetAnGeSample.xml", "BrahWiMeSample.mxl",
			"DebuMandSample.xml", "SchbAvMaSample.xml" };
		for (String scoreExpected : scoresExpected) {
			try {
				assertTrue(scores.contains(scoreExpected));
				zip.loadScore(scoreExpected);
			} catch (Exception ex) {
				throw new Exception("Failed to load " + scoreExpected, ex);
			}
		}
		zip.close();
	}

}
