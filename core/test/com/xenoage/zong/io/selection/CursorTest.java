package com.xenoage.zong.io.selection;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mpe0;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.util.demo.ScoreRevolutionary;


/**
 * Test cases for a {@link Cursor}.
 * 
 * @author Andreas Wenger
 */
public class CursorTest {

	/**
	 * Tests the consistency of the {@link ScoreRevolutionary} demo,
	 * which is written with a {@link Cursor}.
	 */
	@Test public void testScoreRevolutionary() {
		ScoreRevolutionary.createScore();
	}


	@Test public void write_MeasureElement_Test() {
		Score score = ScoreFactory.create1Staff();
		Cursor cursor = new Cursor(score, mpe0, true);
		cursor.write(new Rest(fr(1, 4)));
		cursor.write(new Rest(fr(1, 4)));
		cursor.write(new Rest(fr(1, 4)));
		//write clef at 1/4
		Clef clef1 = new Clef(ClefType.F);
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write(clef1);
		BeatEList<Clef> clefs = score.getMeasure(atMeasure(0, 0)).getClefs();
		assertEquals(1, clefs.size());
		assertEquals(beatE(clef1, fr(1, 4)), clefs.getFirst());
		//write clef at 2/4
		Clef clef2 = new Clef(ClefType.G);
		cursor.setMP(atElement(0, 0, 0, 2));
		cursor.write(clef2);
		clefs = score.getMeasure(atMeasure(0, 0)).getClefs();
		assertEquals(2, clefs.size());
		assertEquals(beatE(clef1, fr(1, 4)), clefs.getFirst());
		assertEquals(beatE(clef2, fr(2, 4)), clefs.getElements().get(1));
		//overwrite clef at 1/4
		Clef clef3 = new Clef(ClefType.G);
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write(clef3);
		clefs = score.getMeasure(atMeasure(0, 0)).getClefs();
		assertEquals(2, clefs.size());
		assertEquals(beatE(clef3, fr(1, 4)), clefs.getFirst());
		assertEquals(beatE(clef2, fr(2, 4)), clefs.getElements().get(1));
		//write key at 1/4
		Key key = new TraditionalKey(5, Mode.Major);
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write((MeasureElement) key);
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getClefs().size()); //clefs must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getPrivateKeys().size());
		//write direction at 1/4
		Direction direction1 = new Dynamics(DynamicsType.ff);
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write((MeasureElement) direction1);
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getClefs().size()); //clefs must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getPrivateKeys().size()); //key must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getDirections().size());
		//write another direction at 1/4, which does not replace the first one
		Direction direction2 = new Coda();
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write((MeasureElement) direction2);
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getClefs().size()); //clefs must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getPrivateKeys().size()); //key must still be there
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getDirections().size()); //now two directions
		//write instrument change at 1/4
		InstrumentChange instrChange = new InstrumentChange(Instrument.defaultValue);
		cursor.setMP(atElement(0, 0, 0, 1));
		cursor.write(instrChange);
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getClefs().size()); //clefs must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getPrivateKeys().size()); //key must still be there
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).getDirections().size()); //directions must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).getInstrumentChanges().size());
		//check all added elements
		BeatEList<MeasureElement> all = score.getMeasure(atMeasure(0, 0)).getMeasureElements();
		assertEquals(6, all.size());
		assertEquals(clef3, all.getElements().get(0).element);
		assertEquals(key, all.getElements().get(1).element);
		assertEquals(direction1, all.getElements().get(2).element);
		assertEquals(direction2, all.getElements().get(3).element);
		assertEquals(instrChange, all.getElements().get(4).element);
		assertEquals(clef2, all.getElements().get(5).element);
	}

}
