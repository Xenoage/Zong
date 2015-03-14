package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.lineHeightIs;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Hang;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Sit;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Straddle;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.WhiteSpace;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link Anchor}.
 * 
 * @author Andreas Wenger
 */
public class AnchorTest {
	
	@Test public void fromLpTest() {
		float lineHeightLp = lineHeightIs * 2;
		//some simple tests
		assertEquals(Sit, Anchor.fromLp(2, Down)); //sitting on 2nd line
		assertEquals(WhiteSpace, Anchor.fromLp(2.5f, Down)); //whitespace in 2nd space
		assertEquals(Hang, Anchor.fromLp(3, Down)); //hanging below 3rd line
		assertEquals(Straddle, Anchor.fromLp(3.5f, Down)); //staddling 3rd line
		assertEquals(Sit, Anchor.fromLp(2 + lineHeightLp, Up)); //sitting on 2nd line
		assertEquals(WhiteSpace, Anchor.fromLp(2.5f + lineHeightLp, Up)); //whitespace in 2nd space
		assertEquals(Hang, Anchor.fromLp(3 + lineHeightLp, Up)); //hanging below 3rd line
		assertEquals(Straddle, Anchor.fromLp(3.5f + lineHeightLp, Up)); //staddling 3rd line
		//test all
		for (int baseLp = -4; baseLp <= 12; baseLp += 2) {
			//downstem
			assertEquals(Sit, Anchor.fromLp(baseLp, Down));
			assertEquals(Straddle, Anchor.fromLp(baseLp - 0.5f, Down));
			assertEquals(Hang, Anchor.fromLp(baseLp - 1, Down));
			assertEquals(WhiteSpace, Anchor.fromLp(baseLp - 1.5f, Down));
			//upstem
			assertEquals(Sit, Anchor.fromLp(baseLp + lineHeightLp, Up));
			assertEquals(Straddle, Anchor.fromLp(baseLp + lineHeightLp - 0.5f, Up));
			assertEquals(Hang, Anchor.fromLp(baseLp + lineHeightLp - 1, Up));
			assertEquals(WhiteSpace, Anchor.fromLp(baseLp + lineHeightLp - 1.5f, Up));
		}
	}

}
