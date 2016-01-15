package com.xenoage.zong.android.io.midi.out;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.MetaEvent;
import com.leff.midi.util.VariableLengthInt;

/**
 * Instantiable {@link MetaEvent}.
 * 
 * @author Andreas Wenger
 */
public class MetaMessage
	extends MetaEvent {

	protected MetaMessage(long tick, int type, byte[] data) {
		super(tick, 0, type, readData(data));
	}
	
	private static VariableLengthInt readData(byte[] bytes) {
		VariableLengthInt d = null;
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			d = new VariableLengthInt(is);
			is.close();
		} catch (IOException e) {
		}
		return d;
	}

	@Override protected int getEventSize() {
		//code copied from GenericMetaEvent
		return 1 + 1 + mLength.getByteCount() + mLength.getValue();
	}

	@Override public int compareTo(MidiEvent other) {
		//code copied from GenericMetaEvent
		if (mTick != other.getTick()) {
			return mTick < other.getTick() ? -1 : 1;
		}
		if (mDelta.getValue() != other.getDelta()) {
			return mDelta.getValue() < other.getDelta() ? 1 : -1;
		}
		return 1;
	}

}
