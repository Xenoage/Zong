package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Test data for the tests with {@link AccidentalsNotation}s.
 * 
 * @author Andreas Wenger
 */
public class TestData {
	
	public MusicContext contextC, contextEb;
	public MusicContext contextAccD4, contextAccG4, contextAccB4, contextAccC5, contextAccD5;
	public MusicContext contextAccsD4B4;//, contextAccsG5A5;

	public float noteOffset;
	
	public ChordWidths cw = ChordWidths.defaultValue;

	public NoteSuspension susNone = NoteSuspension.None;
	public NoteSuspension susLeft = NoteSuspension.Left;
	public NoteSuspension susRight = NoteSuspension.Right;


	public TestData() {
		ClefType clefG = ClefType.clefTreble;
		contextC = MusicContext.simpleInstance;
		//contextEb: key = Eb major, acc = Fbb5, G##5
		contextEb = new MusicContext(new Clef(clefG), new TraditionalKey(-3), new Pitch[] {
			pi(3, -2, 5), pi(4, 2, 5) }, 5);
		//contextAccD4: key = C major, acc = D#4
		contextAccD4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 4) }, 5);
		//contextAccG4: key = C major, acc = G#4
		contextAccG4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(4, 1, 4) }, 5);
		//contextAccB4: key = C major, acc = B#4
		contextAccB4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(6, 1, 4) }, 5);
		//contextAccC5: key = C major, acc = C#5
		contextAccC5 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(0, 1, 5) }, 5);
		//contextAccD5: key = C major, acc = D#5
		contextAccD5 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 5) }, 5);
		//contextAccsD4B5: key = C major, acc = D#4 and B#4
		contextAccsD4B4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 4), pi(6, 1, 4) }, 5);
		//contextAccsG5A5: key = C major, acc = G#5 and A#5
		/*TODO contextAccsG5A5 = new MusicContext(
		  new Clef(clefG, null), new TraditionalKey(0),
		  new Pitch[]{pi4, 1, 5), pi5, 1, 5)});*/
		noteOffset = 1.2f; //typical quarter note width
	}
	
}
