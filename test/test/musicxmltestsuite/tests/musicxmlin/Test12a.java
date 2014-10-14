package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base12a;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;

public class Test12a
	implements Base12a, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		//check clefs and line position of c4
		int m = 0;
		checkClef(score, m++, ClefSymbol.G, 2, -2);
		checkClef(score, m++, ClefSymbol.C, 4, 4);
		checkClef(score, m++, ClefSymbol.C, 6, 6);
		checkClef(score, m++, ClefSymbol.F, 6, 10);
		checkClef(score, m++, ClefSymbol.PercTwoRects, 4, -2); //in Zong!, we use Perc = Treble
		checkClef(score, m++, ClefSymbol.G8vb, 2, 5);
		checkClef(score, m++, ClefSymbol.F8vb, 6, 17);
		checkClef(score, m++, ClefSymbol.F, 4, 8);
		checkClef(score, m++, ClefSymbol.G, 0, -4);
		checkClef(score, m++, ClefSymbol.C, 8, 8);
		checkClef(score, m++, ClefSymbol.C, 2, 2);
		checkClef(score, m++, ClefSymbol.C, 0, 0);
		checkClef(score, m++, ClefSymbol.PercTwoRects, 4, -2); //in Zong!, we use Perc = Treble
		checkClef(score, m++, ClefSymbol.G8va, 2, -9);
		checkClef(score, m++, ClefSymbol.F8va, 6, 3);
		checkClef(score, m++, ClefSymbol.Tab, 4, -2); //in Zong!, we use Tab = Treble
		checkClef(score, m++, ClefSymbol.None, 4, -2); //in Zong!, we use None = Treble
		checkClef(score, m++, ClefSymbol.G, 2, -2);
	}
	
	private void checkClef(Score score, int measure, ClefSymbol expectedSymbol, int expectedClefLP,
		int expectedC4LP) {
		ClefType clef = score.getMeasure(atMeasure(0, measure)).getClefs().getFirst().getElement().getType();
		assertEquals("measure " + measure, expectedSymbol, clef.getSymbol());
		assertEquals("measure " + measure, expectedClefLP, clef.getLp());
		assertEquals("measure " + measure, expectedC4LP, clef.getLp(pi('C', 0, 4)));
	}

}
