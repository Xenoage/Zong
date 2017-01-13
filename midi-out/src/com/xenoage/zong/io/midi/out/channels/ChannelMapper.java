package com.xenoage.zong.io.midi.out.channels;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.Part;
import lombok.val;

import java.util.HashMap;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.zong.io.midi.out.MidiConverter.channel10;
import static com.xenoage.zong.io.midi.out.channels.ChannelMap.unused;

/**
 * Creates the mapping from staff indices to MIDI channel numbers.
 *
 * If possible, each part gets its own channel. If there are too many parts,
 * the parts with the same MIDI program share the channel. If there are still
 * too many parts, the remaining parts are ignored (i.e. they are mapped to
 * the channel number {@link ChannelMap#unused}).
 * Channel 10 is always reserved for drums/percussion/metronome.
 *
 * @author Andreas Wenger
 */
public class ChannelMapper {

	private static final int maxChannelsCount = 16;


	public static ChannelMap createChannelMap(Score score) {
		int[] staffChannels;

		//get number of parts which have not channel 10
		int melodicPartsCount = 0;
		for (Part part : score.getStavesList().getParts()) {
			if (isPitched(part)) {
				melodicPartsCount++;
			}
		}
		//find out if there are enough channels for all parts
		if (melodicPartsCount < maxChannelsCount)
			//each part gets its own channel
			staffChannels = createChannelForEachPart(score);
		else
			//try to reuse channels
			staffChannels = createReusedChannels(score);

		return new ChannelMap(staffChannels);
	}

	/**
	 * Each pitched part gets its own channel.
	 * All unpitched parts get channel 10.
	 */
	private static int[] createChannelForEachPart(Score score) {
		int[] staffChannels = new int[score.getStavesCount()];
		int iChannel = 0;
		for (Part part : score.getStavesList().getParts()) {
			boolean isPitched = isPitched(part);
			int channel = (isPitched ? iChannel : channel10);
			for (int iStaff : score.getStavesList().getPartStaffIndices(part).getRange())
				staffChannels[iStaff] = channel;
			//after pitched part: increment channel number
			if (isPitched)
				iChannel = iChannel + 1 + (iChannel + 1 == channel10 ? 1 : 0); //don't use channel 10
		}
		return staffChannels;
	}

	/**
	 * Share channel for parts with the same instrument (MIDI program).
	 * If none is left, the part is ignored (channel index = {@link ChannelMap#unused}).
	 * All unpitched parts get channel 10.
	 */
	private static int[] createReusedChannels(Score score) {

		int[] staffChannels = new int[score.getStavesCount()];
		int nextFreeChannel = 0;
		HashMap<Integer, Integer> programToDeviceMap = map();

		for (Part part : score.getStavesList().getParts()) {
			boolean isPitched = isPitched(part);

			//find channel
			int channel;
			if (isPitched) {
				//pitched part: create new channel or reuse existing channel
				val pitchedInstr = (PitchedInstrument) part.getFirstInstrument();
				int program = pitchedInstr.getMidiProgram();
				channel = notNull(programToDeviceMap.get(program), nextFreeChannel);
				if (channel >= maxChannelsCount) {
					//no more channel left for this part
					channel = unused;
				}
				else if (channel == nextFreeChannel) {
					//new channel created: increment next channel number and remember program
					nextFreeChannel = nextFreeChannel + 1 + (nextFreeChannel + 1 == channel10 ? 1 : 0); //don't use channel 10
					programToDeviceMap.put(program, channel);
				}
			}
			else {
				//unpitched part: always use channel 10
				channel = channel10;
			}

			//apply channel to all staves
			for (int iStaff : score.getStavesList().getPartStaffIndices(part).getRange()) {
				staffChannels[iStaff] = channel;
			}
		}

		return staffChannels;
	}


	/**
	 * Returns true, iff the given part starts with a pitched instrument.
	 */
	private static boolean isPitched(Part part) {
		return (part.getFirstInstrument() instanceof PitchedInstrument);
	}

}
