package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base41c;
import musicxmltestsuite.tests.utils.StavesListTest;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.StavesList;


public class Test41c
	implements Base41c, MusicXmlInTest {
	
	private StavesList stavesList;
	
	@Before public void before() {
		Score score = getScore();
		stavesList = score.getStavesList();
	}
	
	@Test public void testPartNames() {
		StavesListTest.checkPartNames(stavesList, expectedPartNames);
	}
	
	@Test public void testBracketGroups() {
		StavesListTest.checkBracketGroups(stavesList, expectedBracketGroups);
	}
	
	@Test public void testBarlineGroups() {
		StavesListTest.checkBarlineGroups(stavesList, expectedBarlineGroups);
	}
	
}