package com.xenoage.zong.io.midi.out.channels;

import com.xenoage.utils.annotations.Const;
import lombok.Data;

/**
 * Map from staff index to channel number.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class ChannelMap {

	/** Value for staves for which no channel could be found. */
	public static final int unused = -1;

	//index: staff index, value: channel number
	private final int[] staffChannels;

	/**
	 * Gets the channel number of the given staff index.
	 * May be {@link #unused} if there is no channel for this staff.
	 */
	public int getChannel(int staff) {
		return staffChannels[staff];
	}
}
