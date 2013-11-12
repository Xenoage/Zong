package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.core.ScoreFactory.create1Staff;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.chord.ChordFactory.graceChord;
import static com.xenoage.zong.core.position.BMP.bmp0;
import static com.xenoage.zong.core.position.IMP.imp;
import static com.xenoage.zong.core.position.IMP.imp0;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.io.TestIO;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.NormalTime;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;


/**
 * Test cases for a {@link BeatOffsetsStrategy}.
 * 
 * @author Andreas Wenger
 */
public class BeatOffsetsStrategyTest
{
	
	private BeatOffsetsStrategy strategy;
	private LayoutSettings layoutSettings;
  
	private final float width_grace = 1f;
  private final float width_1_8 = 1.5f;
  private final float width_1_6 = 1.7f;
  private final float width_1_4 = 2f;
  private final float width_3_8 = 2.5f;
  private final float width_1_2 = 3f;
  private final float width_1_1 = 5f;
  
  private final Fraction dur_1_8 = fr(1, 8);
  private final Fraction dur_1_6 = fr(1, 6);
  private final Fraction dur_1_4 = fr(1, 4);
  private final Fraction dur_3_8 = fr(3, 8);
  private final Fraction dur_1_2 = fr(1, 2);
  private final Fraction dur_1_1 = fr(1, 1);
  
  
  @Before public void setUp()
  {
  	TestIO.initWithSharedDir();
  	strategy = new BeatOffsetsStrategy();
  	layoutSettings = LayoutSettings.load("data/test/layout/LayoutSettingsTest.xml");
  	//strategy.guaranteedMinimalDistance = 0.5f;
  }
  

  @Test public void testComputeMeasureBeats()
  {
    //must have 5 beats at 0, 2, 3, 4, 8.
    List<Fraction> beats = strategy.computeVoicesBeats(createVoiceSpacings(createTestScore1Voice())).getLinkedList();
    assertEquals(5, beats.size());
    assertEquals(fr(0, 4), beats.get(0));
    assertEquals(fr(2, 8), beats.get(1));
    assertEquals(fr(3, 8), beats.get(2));
    assertEquals(fr(4, 8), beats.get(3));
    assertEquals(fr(8, 8), beats.get(4));
  }
  
  
  @Test public void testComputeDistance()
  {
    Voice voice = createTestScore1Voice().getVoice(bmp0);
    PVector<SpacingElement> spacings = createTestSpacingElements1Voice();
    LinkedList<BeatOffset> emptyList = new LinkedList<BeatOffset>();
    //distance: the offsets of the notes and rests are interesting,
    //not the ones of the clefs, key signatures and time signatures,
    //so the method has to use the last occurrence of a beat.
    //distance between beat 0 and 4: must be 6
    float distance = strategy.computeMinimalDistance(
      fr(0), fr(4, 4), false, voice, spacings, emptyList, 1);
    assertEquals(6, distance, Delta.DELTA_FLOAT);
    //distance between beat 0 and 5: must be 0 (beat 5 isn't used)
    distance = strategy.computeMinimalDistance(
      fr(0), fr(5, 4), false, voice, spacings, emptyList, 1);
    assertEquals(0, distance, Delta.DELTA_FLOAT);
    //distance between beat 0 and 2: must be 2
    distance = strategy.computeMinimalDistance(
      fr(0), fr(2, 4), false,voice, spacings, emptyList, 1);
    assertEquals(2, distance, Delta.DELTA_FLOAT);
    //distance between beat 5 and 8: must be 0 (beat 5 isn't used)
    distance = strategy.computeMinimalDistance(
      fr(5), fr(8, 4), false, voice, spacings, emptyList, 1);
    assertEquals(0, distance, Delta.DELTA_FLOAT);
  }
  
  
  /**
   * Compute offsets of the common beats.
   */
  @Test public void computeBeatOffsetsTest1()
  {
    Score score = createTestScore3Voices();
    
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	createVoiceSpacings(score), fr(4, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    float is = score.format.interlineSpace;
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
    //^: dominating voice
    //voice 1:   | 4     4     4     4     |     
    //                               ^^^^^
    //voice 2:   | 8  8  8  8  2           |
    //             ^^^^^^^^^^^^
    //voice 3:   | 3   3   3   8  8  4     |
    //                         ^^^^^^
    //used:        *  ** * **  *  *  *     *
    //checked:     *     *     *     *     *
    assertEquals(10, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals((0)*is, beatOffsets[0].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(1, 4), beatOffsets[3].getBeat());
    assertEquals((2*width_1_8)*is, beatOffsets[3].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(2, 4), beatOffsets[6].getBeat());
    assertEquals((4*width_1_8)*is, beatOffsets[6].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(3, 4), beatOffsets[8].getBeat());
    assertEquals((6*width_1_8)*is, beatOffsets[8].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(4, 4), beatOffsets[9].getBeat());
    assertEquals((6*width_1_8+width_1_4)*is, beatOffsets[9].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Compute offsets of the common beats,
   * this time for an incomplete measure.
   */
  @Test public void computeBeatOffsetsTest2()
  {
  	Score score = createTestScore3VoicesIncomplete();
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	createVoiceSpacings(score), fr(4, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    float is = score.format.interlineSpace;
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, x: missing (empty)
    //^: dominating voice
    //voice 1:   | 4     4     4     8  xxx|
    //                         ^^^^^^^^^^^^
    //voice 2:   | 8  8  8  8  4     xxxxxx|
    //             ^^^^^^^^^^^^
    //voice 3:   | 3   3   3   8  xxxxxxxxx|
    //                         
    //used:        *  ** * **  *     *  °  *    (°: total final used beat)   
    //checked:     *     *     *     *  *  *
    
    assertEquals(10, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals((0)*is, beatOffsets[0].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(1, 4), beatOffsets[3].getBeat());
    assertEquals((2*width_1_8)*is, beatOffsets[3].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(2, 4), beatOffsets[6].getBeat());
    assertEquals((4*width_1_8)*is, beatOffsets[6].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(3, 4), beatOffsets[7].getBeat());
    assertEquals((4*width_1_8+width_1_4)*is, beatOffsets[7].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(7, 8), beatOffsets[8].getBeat());
    assertEquals((4*width_1_8+width_1_4+width_1_8)*is, beatOffsets[8].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(4, 4), beatOffsets[9].getBeat());
    assertEquals((4*width_1_8+width_1_4+width_1_4)*is, beatOffsets[9].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Compute offsets of the common beats, when also grace notes are used.
   */
  @Test public void computeBeatOffsetsTestGrace()
  {
    Score score = createTestScore3VoicesGrace();
    
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	createVoiceSpacings(score), fr(4, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    float is = score.format.interlineSpace;
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
    //^: dominating voice
    
    //voice 1: | 4     ...4       4     4     |
    //           ^^^^^^^^^              ^^^^^
    //voice 2: | 8  8     8  8  ..2           |
    //                    ^^^^^^^^
    //voice 3: | 3   3      3    .8  8  4     |
    //                            ^^^^^^
    //used:      *  **    * **    *  *  *     *
    //checked:   *        *       *     *     *
    assertEquals(10, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals((0)*is, beatOffsets[0].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(1, 4), beatOffsets[3].getBeat());
    float offset1 = width_1_4+3*width_grace;
    assertEquals(offset1*is, beatOffsets[3].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(2, 4), beatOffsets[6].getBeat());
    float offset2 = offset1 + 2*width_1_8+2*width_grace;
    assertEquals(offset2*is, beatOffsets[6].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(3, 4), beatOffsets[8].getBeat());
    float offset3 = offset2 + 2*width_1_8;
    assertEquals(offset3*is, beatOffsets[8].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
    assertEquals(fr(4, 4), beatOffsets[9].getBeat());
    float offset4 = offset3 + width_1_4;
    assertEquals(offset4*is, beatOffsets[9].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Test file "BeatOffsetsStrategyTest-1.xml".
   */
  @Test public void computeBeatOffsets_File1()
  {
    Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetsStrategyTest-1.xml");
    
    LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	voiceSpacings, fr(3, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    
    //file must have 5 beat offsets with increasing mm offsets
    assertEquals(5, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals(fr(1, 4), beatOffsets[1].getBeat());
    assertEquals(fr(2, 4), beatOffsets[2].getBeat());
    assertEquals(fr(5, 8), beatOffsets[3].getBeat());
    assertEquals(fr(3, 4), beatOffsets[4].getBeat());
    for (int i = 0; i < beatOffsets.length - 1; i++)
    {
    	assertTrue(beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
    }
    
    //distance between beat 1/4 and 2/4 must be width_1_4
    float is = score.format.interlineSpace;
    assertEquals(width_1_4 * is, beatOffsets[2].getOffsetMm() - beatOffsets[1].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Test file "BeatOffsetsStrategyTest-2.xml".
   */
  @Test public void computeBeatOffsets_File2()
  {
    Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetsStrategyTest-2.xml");
    
    LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	voiceSpacings, fr(3, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    
    //file must have 6 beat offsets with increasing mm offsets
    assertEquals(6, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals(fr(1, 8), beatOffsets[1].getBeat());
    assertEquals(fr(1, 4), beatOffsets[2].getBeat());
    assertEquals(fr(2, 4), beatOffsets[3].getBeat());
    assertEquals(fr(5, 8), beatOffsets[4].getBeat());
    assertEquals(fr(3, 4), beatOffsets[5].getBeat());
    for (int i = 0; i < beatOffsets.length - 1; i++)
    {
    	assertTrue(beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
    }
    
    //distance between beat 1/4 and 2/4 must be width_1_4
    float is = score.format.interlineSpace;
    assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Test file "BeatOffsetsStrategyTest-3.xml".
   */
  @Test public void computeBeatOffsets_File3()
  {
    Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetsStrategyTest-3.xml");
    
    LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	voiceSpacings, fr(5, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    
    //file must have 8 beat offsets with increasing mm offsets
    //special difficulty: last eighth note must be further to the right as preceding quarter
    //in other voice, even the distance between the whole note and the last eighth would be big enough
    assertEquals(8, beatOffsets.length);
    assertEquals(fr(0, 4), beatOffsets[0].getBeat());
    assertEquals(fr(1, 8), beatOffsets[1].getBeat());
    assertEquals(fr(1, 4), beatOffsets[2].getBeat());
    assertEquals(fr(2, 4), beatOffsets[3].getBeat());
    assertEquals(fr(3, 4), beatOffsets[4].getBeat());
    assertEquals(fr(4, 4), beatOffsets[5].getBeat());
    assertEquals(fr(9, 8), beatOffsets[6].getBeat());
    assertEquals(fr(5, 4), beatOffsets[7].getBeat());
    for (int i = 0; i < beatOffsets.length - 1; i++)
    {
    	assertTrue("beat " + (i + 1) + " wrong",
    		beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
    }
    
    //distance between beat 1/4 and 2/4 must be width_1_4
    float is = score.format.interlineSpace;
    assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  /**
   * Test file "BeatOffsetsStrategyTest-4.xml".
   */
  @Test public void computeBeatOffsets_File4()
  {
    Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetsStrategyTest-4.xml");
    
    LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
    BeatOffset[] beatOffsets = strategy.computeBeatOffsetsFromVoiceSpacings(
    	voiceSpacings, fr(3, 4), layoutSettings).toArray(new BeatOffset[0]); //TIDY
    
    //distance between beat 1/4 and 2/4 must be width_1_4
    float is = score.format.interlineSpace;
    assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(), Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  private Score createTestScore1Voice()
  {
    Score score = create1Staff();
    score = score.withFormat(score.format.withInterlineSpace(1));
    Cursor cursor = new Cursor(score, imp0, true);
    cursor = cursor.write(new Clef(ClefType.G));
    cursor = cursor.write((MeasureElement) new TraditionalKey(-3));
    cursor = cursor.write(new NormalTime(6, 4));
    //beats: 0, 2, 3, 4, 8.
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 2)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 2)));
    return cursor.getScore();
  }
  
  
  private PVector<SpacingElement> createTestSpacingElements1Voice()
  {
    return pvec(
      new SpacingElement(null, fr(0, 4), 1), //clef. width: 3
      new SpacingElement(null, fr(0, 4), 4), //key. width: 2
      new SpacingElement(null, fr(0, 4), 6), //time. width: 3
      new SpacingElement(null, fr(0, 4), 9), //note. width: 2
      new SpacingElement(null, fr(2, 4), 11), //note. width: 2
      new SpacingElement(null, fr(3, 4), 13), //note. width: 2
      new SpacingElement(null, fr(4, 4), 15), //note. width: 2
      new SpacingElement(null, fr(8, 4), 17) //note. width: 2
    );
  }
  
  
  private Score createTestScore3Voices()
  {
  	Score score = Score.empty.plusPart(new Part("", null, 2, null));
    score = score.withFormat(score.format.withInterlineSpace(10));
    Cursor cursor = new Cursor(score, imp0, true);
    cursor = cursor.write(new NormalTime(4, 4));
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
    //voice 1: | 4     4     4     4     |   (staff 1)
    //voice 2: | 8  8  8  8  2           |   (staff 1)
    //voice 3: | 3   3   3   8  8  4     |   (staff 2)
    
    //voice 1 (staff 1)
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    
    //voice 2 (staff 1)
    cursor = cursor.withIMP(imp(0, 0, 1, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 2)));
    
    //voice 3 (staff 2)
    cursor = cursor.withIMP(imp(1, 0, 0, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    
    return cursor.getScore();
  }
  
  
  private Score createTestScore3VoicesGrace()
  {
  	Score score = Score.empty.plusPart(new Part("", null, 2, null));
    score = score.withFormat(score.format.withInterlineSpace(10));
    Cursor cursor = new Cursor(score, imp0, true);
    cursor = cursor.write(new NormalTime(4, 4));
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, .: grace note
    //voice 1: | 4     ...4       4     4     |   (staff 1)
    //voice 2: | 8  8     8  8  ..2           |   (staff 1)
    //voice 3: | 3   3      3    .8  8  4     |   (staff 2)
    
    //voice 1 (staff 1)
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    
    //voice 2 (staff 1)
    cursor = cursor.withIMP(imp(0, 0, 1, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 2)));
    
    //voice 3 (staff 2)
    cursor = cursor.withIMP(imp(1, 0, 0, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(graceChord(pi(0, 0, 0)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    
    return cursor.getScore();
  }
  
  
  private Score createTestScore3VoicesIncomplete()
  {
  	Score score = Score.empty.plusPart(new Part("", null, 2, null));
    score = score.withFormat(score.format.withInterlineSpace(2));
    Cursor cursor = new Cursor(score, imp0, true);
    cursor = cursor.write(new NormalTime(4, 4));
    
    //2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, x: missing (empty)
    //voice 1: | 4     4     4     8  xxx|   (staff 1)
    //voice 2: | 8  8  8  8  4     xxxxxx|   (staff 1)
    //voice 3: | 3   3   3   8  xxxxxxxxx|   (staff 2)
    
    //voice 1
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    
    //voice 2
    cursor = cursor.withIMP(imp(0, 0, 1, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 4)));
    
    //voice 3
    cursor = cursor.withIMP(imp(0, 0, 2, 0));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 6)));
    cursor = cursor.write(chord(pi(0, 0, 0), fr(1, 8)));
    
    return cursor.getScore();
  }
  
  
  /**
   * Create {@link VoiceSpacing}s for the first measure column
   * of the given {@link Score}.
   */
  private LinkedList<VoiceSpacing> createVoiceSpacings(Score score)
  {
  	LinkedList<VoiceSpacing> ret = new LinkedList<VoiceSpacing>();
  	for (int iStaff : range(0, score.getStavesCount() - 1))
  	{
	  	Measure measure = score.getMeasure(atMeasure(iStaff, 0));
  		for (Voice voice : measure.voices)
  		{
  			Fraction beat = fr(0);
  			PVector<SpacingElement> se = new PVector<SpacingElement>();
  			float offset = 0;
  			for (VoiceElement e : voice.elements)
  			{
  				//compute width
  				float width = 0;
					if (e.getDuration().equals(_0))
  					width = width_grace;
					else if (e.getDuration().equals(dur_1_8))
  					width = width_1_8;
  				else if (e.getDuration().equals(dur_1_6))
  					width = width_1_6;
  				else if (e.getDuration().equals(dur_1_4))
  					width = width_1_4;
  				else if (e.getDuration().equals(dur_3_8))
  					width = width_3_8;
  				else if (e.getDuration().equals(dur_1_2))
  					width = width_1_2;
  				else if (e.getDuration().equals(dur_1_1))
  					width = width_1_1;
  				//create spacing element with offset
  				se = se.plus(new SpacingElement(e, beat, offset));
  				beat = beat.add(e.getDuration());
  				offset += width;
  			}
  			se = se.plus(new SpacingElement(null, beat, offset));
  			ret.add(new VoiceSpacing(voice, score.format.interlineSpace, se));
  		}
  	}
  	return ret;
  }
  

}
