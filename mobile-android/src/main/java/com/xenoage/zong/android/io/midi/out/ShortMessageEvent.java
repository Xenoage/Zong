package com.xenoage.zong.android.io.midi.out;

import com.leff.midi.event.ChannelEvent;

/**
 * Instantiable {@link ChannelEvent}.
 * 
 * @author Andreas Wenger
 */
public class ShortMessageEvent
	extends ChannelEvent {

	public ShortMessageEvent(long tick, int type, int channel, int param1, int param2) {
		super(tick, type, channel, param1, param2);
	}

}
