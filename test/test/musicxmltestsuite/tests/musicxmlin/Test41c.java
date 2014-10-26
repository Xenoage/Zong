package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base41c;
import musicxmltestsuite.tests.utils.StavesListTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.StavesList;


public class Test41c
	implements Base41c, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		StavesListTest.checkPartNames(stavesList, expectedPartNames);
		StavesListTest.checkBracketGroups(stavesList, expectedBracketGroups);
		StavesListTest.checkBarlineGroups(stavesList, expectedBarlineGroups);
	}
	
}
