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
		Chord chord = (Chord) score.getVoice(mp0).getElementAt(Companion.get_0());
		assertEquals(1, chord.getNotes().size());
		assertEquals(Companion.pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(Companion.get_1(), chord.getDuration());
		assertEquals(2, chord.getAnnotations().size());
		assertEquals(articulation(ArticulationType.Accent, Placement.Below), chord
			.getAnnotations().get(0));
		assertEquals(fermata(Placement.Above), chord.getAnnotations().get(1));
		//words "Largo"
		List<MeasureElement> directions = score.getMeasure(mp0).getMeasureElements().getAll(Companion.get_0());
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
		Chord chord = (Chord) score.getVoice(m2).getElementAt(Companion.get_0());
		assertEquals(2, chord.getNotes().size());
		assertEquals(Companion.pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(Companion.pi('A', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(Companion.fr(3, 8), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(Companion.fr(3, 8));
		assertEquals(2, chord.getNotes().size());
		assertEquals(Companion.pi('F', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(Companion.pi('A', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(Companion.fr(1, 8), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(Companion.fr(2, 4));
		assertEquals(2, chord.getNotes().size());
		assertEquals(Companion.pi('G', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(Companion.pi('B', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(Companion.fr(1, 4), chord.getDuration());
		chord = (Chord) score.getVoice(m2).getElementAt(Companion.fr(3, 4));
		assertEquals(2, chord.getNotes().size());
		assertEquals(Companion.pi('G', 0, 4), chord.getNotes().get(0).getPitch());
		assertEquals(Companion.pi('B', -1, 4), chord.getNotes().get(1).getPitch());
		assertEquals(Companion.fr(1, 4), chord.getDuration());
		//dynamics "p"
		Dynamic dynamics = (Dynamic) score.getMeasure(m2).getMeasureElements().get(Companion.get_0());
		assertEquals(DynamicValue.p, dynamics.getValue());
		assertEquals(Placement.Below, dynamics.getPositioning());
	}

}
