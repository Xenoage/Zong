package musicxmltestsuite.tests.musicxmlin;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.position.MP;
import musicxmltestsuite.tests.base.Base21d;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static musicxmltestsuite.tests.utils.Utils.articulation;
import static musicxmltestsuite.tests.utils.Utils.fermata;
import static org.junit.Assert.*;

public class Test21d
	implements Base21d, MusicXmlInTest {
	
	Score score;
	
	@Before public void before() {
		score = getScore();
	}

	@Test public void testMeasure0() {
		//F4, whole, with accent and fermata
		Chord chord = (Chord) score.getVoice(mp0).getElementAt(_0);
		assertEquals(1, chord.getNotes().size());
		assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(_1, chord.getDuration());
		assertEquals(2, chord.getAnnotations().size());
		assertEquals(articulation(ArticulationType.Accent, Placement.Below), chord
			.getAnnotations().get(0));
		assertEquals(fermata(Placement.Above), chord.getAnnotations().get(1));
		//words "Largo"
		List<MeasureElement> directions = score.getMeasure(mp0).getMeasureElements().getAll(_0);
		assertEquals(3, directions.size()); //clef, words, dynamics
		Words words = (Words) directions.get(1);
		assertEquals("Largo", words.getText().toString());
		assertEquals(Placement.Above, words.getPositioning());
		//dynamics "fp"
		Dynamic dynamics = (Dynamic) directions.get(2);
		assertEquals(DynamicValue.fp, dynamics.getValue());
		assertEquals(Placement.Below, dynamics.getPositioning());
	}
	
	@Test public void testMeasure1() {
		MP m2 = mp0.withMeasure(1);
		//chords
		Chord chord = (Chord) score.getVoice(m2).getElementAt(_0);
		assertEquals(2, chord.getNotes().size());
		assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(pi('A', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(fr(3, 8), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(fr(3, 8));
		assertEquals(2, chord.getNotes().size());
		assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(pi('A', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(fr(1, 8), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(fr(2, 4));
		assertEquals(2, chord.getNotes().size());
		assertEquals(pi('G', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(pi('B', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(fr(1, 4), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(fr(3, 4));
		assertEquals(2, chord.getNotes().size());
		assertEquals(pi('G', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(pi('B', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(fr(1, 4), chord.getDuration());
		//dynamics "p"
		Dynamic dynamics = (Dynamic) score.getMeasure(m2).getMeasureElements().get(_0);
		assertEquals(DynamicValue.p, dynamics.getValue());
		assertEquals(Placement.Below, dynamics.getPositioning());
	}

}
