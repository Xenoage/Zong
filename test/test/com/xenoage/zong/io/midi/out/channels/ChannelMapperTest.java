package com.xenoage.zong.io.midi.out.channels;

import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.instrument.UnpitchedInstrument;
import com.xenoage.zong.core.music.Part;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.io.midi.out.MidiConverter.channel10;
import static com.xenoage.zong.io.midi.out.channels.ChannelMap.unused;
import static org.junit.Assert.*;

/**
 * Tests for {@link ChannelMapper}.
 *
 * @author Andreas Wenger
 */
public class ChannelMapperTest {

	private static final int drums = 0;

	/**
	 * Enough channels: each part gets its own channel.
	 */
	@Test public void testFewParts() {
		int[] midiPrograms =     {  20,   21, 22,     drums, 20,       20};
		int[] partStavesCount =  {   2,    2,  1,         1,  1,        3};
		int[] expectedChannels = {0, 0, 1, 1,  2, channel10,  3,  4, 4, 4};
		val score = createScore(midiPrograms, partStavesCount);
		assertEquals(new ChannelMap(expectedChannels), ChannelMapper.createChannelMap(score));
	}

	/**
	 * More parts than channels: reuse channels for parts with the same MIDI program.
	 */
	@Test public void testManyParts() {
		int c10 = channel10;
		int[] midiPrograms =     {  20,   21, 22, drums, 20, 20, 1, 2, 3, 4, 5, 6,  7, 1, 2, 3,    drums,    5};
		int[] partStavesCount =  {   2,    2,  1,     1,  1,  1, 1, 1, 1, 1, 1, 1,  1, 1, 1, 1,        2,    2};
		int[] expectedChannels = {0, 0, 1, 1,  2,   c10,  0,  0, 3, 4, 5, 6, 7, 8, 10, 3, 4, 5, c10, c10, 7, 7};
		val score = createScore(midiPrograms, partStavesCount);
		assertEquals(new ChannelMap(expectedChannels), ChannelMapper.createChannelMap(score));
	}

	/**
	 * Too many parts: last parts are not used.
	 */
	@Test public void testTooManyParts() {
		int c10 = channel10;
		int un = unused;
		int[] midiPrograms =     {1, 2, 3, 4, 5, 6, 7, 8, 9, drums, 10, 11, 12, 13, 14, 15, 16, 17, drums, 18, 15, 10, 5, 1};
		int[] partStavesCount =  {1, 1, 1, 1, 1, 1, 1, 1, 1,     1,  1,  1,  1,  1,  1,  1,  1,  1,     1,  1,  1,  1, 1, 1};
		int[] expectedChannels = {0, 1, 2, 3, 4, 5, 6, 7, 8,   c10, 10, 11, 12, 13, 14, 15, un, un,   c10, un, 15, 10, 4, 0};
		val score = createScore(midiPrograms, partStavesCount);
		assertEquals(new ChannelMap(expectedChannels), ChannelMapper.createChannelMap(score));
	}

	private Score createScore(int[] midiPrograms, int[] partStavesCount) {
		val score = new Score();
		for (int iPart : range(partStavesCount)) {
			Instrument instrument;
			if (midiPrograms[iPart] == drums)
				instrument = new UnpitchedInstrument("part " + iPart);
			else
				instrument = new PitchedInstrument("part " + iPart, midiPrograms[iPart]);
			new PartAdd(score, new Part("", null,
					partStavesCount[iPart], alist(instrument)), iPart, null).execute();
		}
		return score;
	}


}
