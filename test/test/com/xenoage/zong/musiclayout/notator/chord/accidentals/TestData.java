package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.settings.ChordWidths.defaultChordWidthsNormal;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
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
	
	public static final MusicContext contextC, contextEb, contextAccD4, contextAccG4, contextAccB4,
		contextAccC5, contextAccD5, contextAccsD4B4, contextAccsG5A5;

	public static final float noteOffset;
	
	public static final ChordWidths cw = defaultChordWidthsNormal;

	public static final NoteSuspension susLeft = NoteSuspension.Left;
	public static final NoteSuspension susRight = NoteSuspension.Right;


	static {
		ClefType clefG = ClefType.Companion.getClefTreble();
		contextC = MusicContext.Companion.getSimpleInstance();
		//contextEb: key = Eb major, acc = Fbb5, G##5
		contextEb = new MusicContext(clefG, new TraditionalKey(-3), new Pitch[] {
			Companion.pi(3, -2, 5), Companion.pi(4, 2, 5) }, 5);
		//contextAccD4: key = C major, acc = D#4
		contextAccD4 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(1, 1, 4) }, 5);
		//contextAccG4: key = C major, acc = G#4
		contextAccG4 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(4, 1, 4) }, 5);
		//contextAccB4: key = C major, acc = B#4
		contextAccB4 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(6, 1, 4) }, 5);
		//contextAccC5: key = C major, acc = C#5
		contextAccC5 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(0, 1, 5) }, 5);
		//contextAccD5: key = C major, acc = D#5
		contextAccD5 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(1, 1, 5) }, 5);
		//contextAccsD4B5: key = C major, acc = D#4 and B#4
		contextAccsD4B4 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(1, 1, 4), Companion.pi(6, 1, 4) }, 5);
		//contextAccsG5A5: key = C major, acc = G#5 and A#5
		contextAccsG5A5 = new MusicContext(clefG, new TraditionalKey(0), new Pitch[] {
			Companion.pi(4, 1, 5), Companion.pi(5, 1, 5)}, 5);
		noteOffset = defaultChordWidthsNormal.quarter; //typical quarter note width
	}
	
}
