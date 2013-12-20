package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.xenoage.utils.filter.AllFilter;
import com.xenoage.zong.io.musicxml.opus.Opus;

/**
 * Tests for {@link OpusFileInput}.
 * 
 * @author Andreas Wenger
 */
public class OpusFileInputTest {

	@Test public void test()
		throws Exception {
		OpusFileInput opusInput = new OpusFileInput();
		String dir = "data/test/scores/MxlOpusFileInputTest";
		Opus opus = opusInput.readOpusFile(platformUtils().openFile(dir + "/SomeOpus.xml"));
		//must contain one score, one opus and one opus-link
		assertTrue(opus.getItems().get(0) instanceof com.xenoage.zong.io.musicxml.opus.Score);
		assertTrue(opus.getItems().get(1) instanceof com.xenoage.zong.io.musicxml.opus.Opus);
		assertTrue(opus.getItems().get(2) instanceof com.xenoage.zong.io.musicxml.opus.OpusLink);
		//resolve links
		opus = opusInput.resolveOpusLinks(opus, null, dir);
		//check flattened list of scores and load the files
		List<String> scores = opus.getScoreFilenames();
		assertEquals(4, scores.size());
		String[] scoresExpected = new String[] { "BeetAnGeSample.xml", "BrahWiMeSample.mxl",
			"DebuMandSample.xml", "SchbAvMaSample.xml" };
		for (String scoreExpected : scoresExpected) {
			try {
				assertTrue(scoreExpected + " not found", scores.contains(scoreExpected));
				String filePath = dir + "/" + scoreExpected;
				FileReader.loadScores(platformUtils().openFile(filePath), filePath, new AllFilter<String>());
			} catch (Exception ex) {
				throw new Exception("Failed to load " + scoreExpected, ex);
			}
		}
	}

}
