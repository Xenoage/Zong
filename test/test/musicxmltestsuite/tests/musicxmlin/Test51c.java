package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import musicxmltestsuite.tests.base.Base51c;

import org.junit.Test;

import com.xenoage.zong.core.Score;

public class Test51c
	implements Base51c, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		assertEquals(Arrays.asList(expectedRights), score.getInfo().getRights());
	}

}
