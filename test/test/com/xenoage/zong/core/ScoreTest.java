package com.xenoage.zong.core;

import static com.xenoage.utils.kernel.Tuple3.t3;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.atVoice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.VoiceAdd;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;


/**
 * Tests for {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class ScoreTest {


	@Test public void getMeasureBeatsTest() {
		Score score = ScoreFactory.create1Staff4Measures();
		//first measure: no time, 1/4 used
		score.getVoice(atVoice(0, 0, 0)).addElement(new Rest(fr(1, 4)));
		//second measure: 3/4 time, 2/4 used
		score.getColumnHeader(1).setTime(new Time(TimeType.time_3_4));
		score.getVoice(atVoice(0, 1, 0)).addElement(new Rest(fr(1, 4)));
		score.getVoice(atVoice(0, 1, 0)).addElement(new Rest(fr(1, 4)));
		//third measure: still 3/4 time, 1/4 used
		score.getVoice(atVoice(0, 2, 0)).addElement(new Rest(fr(1, 4)));
		//check
		assertEquals(fr(1, 4), score.getMeasureBeats(0));
		assertEquals(fr(3, 4), score.getMeasureBeats(1));
		assertEquals(fr(3, 4), score.getMeasureBeats(2));
	}
	
	
	@Test public void getMeasureFilledBeatsTest() {
		//create a little test score, where the first measure only
		//uses 2/4, the second 4/4, the third 0/4, the fourth 3/4.
		Score score = ScoreFactory.create1Staff4Measures();
		score.getVoice(atVoice(0, 0, 0)).addElement(new Rest(fr(1, 4)));
		score.getVoice(atVoice(0, 0, 0)).addElement(new Rest(fr(1, 4)));
		score.getVoice(atVoice(0, 1, 0)).addElement(new Rest(fr(2, 4)));
		score.getVoice(atVoice(0, 1, 0)).addElement(new Rest(fr(2, 4)));
		score.getVoice(atVoice(0, 3, 0)).addElement(new Rest(fr(3, 4)));
		//test method
		assertEquals(fr(2, 4), score.getMeasureFilledBeats(0));
		assertEquals(fr(4, 4), score.getMeasureFilledBeats(1));
		assertEquals(fr(0, 4), score.getMeasureFilledBeats(2));
		assertEquals(fr(3, 4), score.getMeasureFilledBeats(3));
	}
	
	
	@Test public void clipToMeasureTest() {
		Score score = ScoreFactory.create1Staff4Measures();
		score.getHeader().getColumnHeader(0).setTime(new Time(TimeType.time_3_4));

		//position before measure
		MP pos = atBeat(1, 1, 0, fr(2, 4));
		MP clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(_0));

		//position at the beginning of the measure
		pos = atBeat(1, 2, 0, _0);
		clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(_0));

		//position in the measure
		pos = atBeat(1, 2, 0, fr(2, 4));
		clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(fr(2, 4)));

		//position at the end of the measure
		pos = atBeat(1, 2, 0, fr(3, 4));
		clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(fr(3, 4)));

		//position after the end of the measure
		pos = atBeat(1, 2, 0, fr(4, 4));
		clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(fr(3, 4)));

		//position after the measure
		pos = atBeat(1, 3, 0, fr(2, 4));
		clippedPos = score.clipToMeasure(2, pos);
		assertEquals(clippedPos.measure, 2);
		assertTrue(clippedPos.beat.equals(fr(3, 4)));
	}
	
	
	@Test public void getKeyTest() {
		Tuple3<Score, List<Clef>, List<Key>> testData = createTestScoreClefsKeys();
		Score score = testData.get1();
		List<Key> keys = testData.get3();
		assertTrue(keys.get(0) != score.getKey(atBeat(0, 0, 0, fr(0, 4)), Before).element);
		assertTrue(keys.get(0) == score.getKey(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt).element);
		assertTrue(keys.get(0) == score.getKey(atBeat(0, 0, 0, fr(1, 4)), Before).element);
		assertTrue(keys.get(0) == score.getKey(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt).element);
		assertTrue(keys.get(0) == score.getKey(atBeat(0, 0, 0, fr(2, 4)), Before).element);
		assertTrue(keys.get(1) == score.getKey(atBeat(0, 0, 0, fr(2, 4)), BeforeOrAt).element);
		assertTrue(keys.get(1) == score.getKey(atBeat(0, 0, 0, fr(3, 4)), Before).element);
		assertTrue(keys.get(1) == score.getKey(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt).element);
		assertTrue(keys.get(1) == score.getKey(atBeat(0, 1, 0, fr(1, 4)), Before).element);
		assertTrue(keys.get(2) == score.getKey(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt).element);
		assertTrue(keys.get(2) == score.getKey(atBeat(0, 1, 0, fr(2, 4)), Before).element);
	}
	
	
	/**
	 * Creates a score with a single staff, two measures,
	 * and a number of clefs, keys and chords.
	 * The score, the list of clefs and the list of keys is returned.
	 */
	private Tuple3<Score, List<Clef>, List<Key>> createTestScoreClefsKeys()
	{
		//create two measures:
		//clef-g, key-Gmaj, C#1/4, clef-f, Db1/4, clef-g, key-Fmaj, Cnat1/4 |
		//C#1/4, key-Cmaj, clef-f, C2/4.
		Score score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 1).execute();
		List<Clef> clefs = new ArrayList<Clef>();
		List<Key> keys = new ArrayList<Key>();
		Clef c;
		Key k;
		//measure 0
		Measure measure = score.getMeasure(atMeasure(0, 0));
		new Clef(ClefType.G);
		clefs.add(c = new Clef(ClefType.G));
    new MeasureElementWrite(c, measure, fr(0, 4)).execute();
    keys.add(k = new TraditionalKey(1, Mode.Major));
    new MeasureElementWrite(k, measure, fr(0, 4)).execute();
    new VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 0), chord(pi(0, 1, 4), fr(1, 4)), null).execute();
    clefs.add(c = new Clef(ClefType.F));
    new MeasureElementWrite(c, measure, fr(1, 4)).execute();
    new VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 1), chord(pi(1, -1, 4), fr(1, 4)), null).execute();
    clefs.add(c = new Clef(ClefType.G));
    new MeasureElementWrite(c, measure, fr(2, 4)).execute();
    keys.add(k = new TraditionalKey(-1, Mode.Major));
    new MeasureElementWrite(k, measure, fr(2, 4)).execute();
    new VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 2), chord(pi(0, 0, 4), fr(1, 4)), null).execute();
    //measure 1
    measure = score.getMeasure(atMeasure(0, 1));
    new VoiceElementWrite(measure.getVoice(0), atElement(0, 1, 0, 0), chord(pi(0, 1, 4), fr(1, 4)), null).execute();
    keys.add(k = new TraditionalKey(0, Mode.Major));
    new MeasureElementWrite(k, measure, fr(1, 4)).execute();
    clefs.add(c = new Clef(ClefType.F));
    new MeasureElementWrite(c, measure, fr(1, 4)).execute();
    new VoiceElementWrite(measure.getVoice(0), atElement(0, 1, 0, 1), chord(pi(0, 0, 4), fr(2, 4)), null).execute();
    return t3(score, clefs, keys);
	}
	
	
	@Test public void getAccidentalsTest() {
		Score score = createTestScoreAccidentals();
		Map<Pitch, Integer> acc;
		//measure 0
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(0, 4)), Before);
		assertEquals(0, acc.size());
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt);
		assertEquals(1, acc.size());
		assertEquals((Integer) 0, acc.get(pi(3, 0, 4)));
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(1, 4)), Before);
		assertEquals(1, acc.size());
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt);
		assertEquals(1, acc.size());
		//measure 1
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(0, 4)), Before);
		assertEquals(0, acc.size());
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt);
		assertEquals(1, acc.size());
		assertEquals((Integer) 1, acc.get(pi(3, 0, 4)));
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(1, 4)), Before);
		assertEquals(0, acc.size()); //0, because key changed
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt);
		assertEquals(2, acc.size());
		assertEquals((Integer) 1,  acc.get(pi(3, 0, 4)));
		assertEquals((Integer) (-1), acc.get(pi(6, 0, 4)));
	}


	/**
	 * Creates a score with a single staff, two measures with each two voices,
	 * with key changes and chords with accidentals.
	 */
	private Score createTestScoreAccidentals() {
		//create two measures with two voices:
		//           measure 0    | measure 1
		//beat:      0      1     | 0        1
		//key:       Gmaj         | *Fmaj    Cmaj
		//voice 0:   C4           | F#4      Bb4 Bb4
		//voice 1:   Fnat4        | Bb4      F#4 F#4
		//(*) is a private key (not in measure column)
		Score score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 1).execute();
		new VoiceAdd(score.getMeasure(atMeasure(0, 0)), 1).execute();
		new VoiceAdd(score.getMeasure(atMeasure(0, 1)), 1).execute();
		//keys
		new ColumnElementWrite(new TraditionalKey(1, Mode.Major), score.getColumnHeader(0), fr(0, 4), null).execute();
		new MeasureElementWrite(new TraditionalKey(-1, Mode.Major), score.getMeasure(atMeasure(0, 1)), fr(0, 4)).execute();
		new ColumnElementWrite(new TraditionalKey(0, Mode.Major), score.getColumnHeader(1), fr(1, 4), null).execute();
		//measure 0, voice 0
		Voice voice = score.getVoice(MP.atVoice(0, 0, 0));
		voice.addElement(chord(pi(0, 0, 4), fr(2, 4)));
		//measure 1, voice 0
		voice = score.getVoice(MP.atVoice(0, 1, 0));
		voice.addElement(chord(pi(3, 1, 4), fr(1, 4)));
		voice.addElement(chord(pi(6, -1, 4), fr(1, 8)));
		voice.addElement(chord(pi(6, -1, 4), fr(1, 8)));
		//measure 0, voice 1
		voice = score.getVoice(MP.atVoice(0, 0, 1));
		voice.addElement(chord(pi(3, 0, 4), fr(2, 4)));
		//measure 1, voice 1
		voice = score.getVoice(MP.atVoice(0, 1, 1));
		voice.addElement(chord(pi(6, -1, 4), fr(1, 4)));
		voice.addElement(chord(pi(3, 1, 4), fr(1, 8)));
		voice.addElement(chord(pi(3, 1, 4), fr(1, 8)));
		return score;
	}
	
	
	@Test public void getClefTest() {
		Tuple3<Score, List<Clef>, List<Key>> testData = createTestScoreClefsKeys();
		Score score = testData.get1();
		List<Clef> clefs = testData.get2();
		assertTrue(clefs.get(0) != score.getClef(atBeat(0, 0, 0, fr(0, 4)), Before));
		assertTrue(clefs.get(0) == score.getClef(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt));
		assertTrue(clefs.get(0) == score.getClef(atBeat(0, 0, 0, fr(1, 4)), Before));
		assertTrue(clefs.get(1) == score.getClef(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt));
		assertTrue(clefs.get(1) == score.getClef(atBeat(0, 0, 0, fr(2, 4)), Before));
		assertTrue(clefs.get(2) == score.getClef(atBeat(0, 0, 0, fr(2, 4)), BeforeOrAt));
		assertTrue(clefs.get(2) == score.getClef(atBeat(0, 0, 0, fr(3, 4)), Before));
		assertTrue(clefs.get(2) == score.getClef(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt));
		assertTrue(clefs.get(2) == score.getClef(atBeat(0, 1, 0, fr(1, 4)), Before));
		assertTrue(clefs.get(3) == score.getClef(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt));
		assertTrue(clefs.get(3) == score.getClef(atBeat(0, 1, 0, fr(2, 4)), Before));
	}
	
	
	@Test public void getMusicContextTest() {
		Tuple3<Score, List<Clef>, List<Key>> testData = createTestScoreClefsKeys();
		Score score = testData.get1();
		List<Clef> clefs = testData.get2();
		List<Key> keys = testData.get3();
		//measure 1: before 0/4: no accidentals
		MusicContext musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt, Before);
		assertEquals(0, musicContext.getAccidentals().size());
		//measure 1: at 1/8: G clef, F key and C#4 accidental
		musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(1, 8)), BeforeOrAt, Before);
		Assert.assertEquals(clefs.get(2), musicContext.getClef());
		Assert.assertEquals(keys.get(1), musicContext.getKey());
		assertEquals(1, musicContext.getAccidentals().size());
		assertEquals(1, (int) musicContext.getAccidentals().get(pi(0, 0, 4)));
		//measure 1: at 1/4: F clef, C key and no accidentals
		musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt, Before);
		Assert.assertEquals(clefs.get(3), musicContext.getClef());
		Assert.assertEquals(keys.get(2), musicContext.getKey());
		assertEquals(0, musicContext.getAccidentals().size());
	}

}
