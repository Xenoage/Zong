package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base41e;
import musicxmltestsuite.tests.utils.StavesListTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.StavesList;


public class Test41e
	implements Base41e, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		StavesListTest.checkPartNames(stavesList, expectedPartNames);
		StavesListTest.checkPartAbbreviations(stavesList, expectedPartAbbreviations);
	}
	
}
