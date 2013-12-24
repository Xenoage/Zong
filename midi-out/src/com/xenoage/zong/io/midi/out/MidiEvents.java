package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.core.position.MP;

/**
 * Some constants related to MIDI in Zong!.
 * 
 * @author Andreas Wenger
 */
public class MidiEvents {
	
	/** Control message to mark the end of the MIDI file. */
	public static final int eventPlaybackEnd = 117;
	/** Control message used for mapping ticks to {@link MP}s.
	 * Java Sound is only able to use listeners for controller events,
	 * but not for normal note-on events. So we add a control-event on
	 * every used musical position. Control-event number
	 * {@value #eventPlaybackControl} is used, because it is undefined
	 * in the midi-documentation and so it doesn't affect anything.
	 * See http://www.midi.org/techspecs/midimessages.php */
	public static final int eventPlaybackControl = 119;

}
