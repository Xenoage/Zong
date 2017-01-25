package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.zong.core.music.direction.DynamicValue;

import static com.xenoage.zong.core.music.direction.DynamicValue.*;

/**
 * Map {@link DynamicValue} to concrete volume values.
 *
 * These values are currently predefined. Later, we could load them
 * from used-defined settings.
 *
 * @author Andreas Wenger
 */
public class DynamicsInterpretation {

	/**
	 * Gets the concrete loudness of the given {@link DynamicValue}
	 * at its beginning. This is a value between 0 (silence) and 1 (maximum loudness).
	 */
	public float getVolume(DynamicValue dynamics) {
		switch (dynamics) {
			case pppppp: return 0.02f;
			case ppppp: return 0.04f;
			case pppp: return 0.06f;
			case ppp: return 0.8f;
			case pp: return 0.1f;
			case p: return 0.3f;
			case mp: return 0.45f;
			case mf: return 0.55f;
			case f: return 0.7f;
			case ff: return 0.9f;
			case fff: return 0.92f;
			case ffff: return 0.94f;
			case fffff: return 0.96f;
			case ffffff: return 0.98f;
			//the other values imply dynamic changes within the note,
			//we return their start volume
			case sffz: return getVolume(ff);
			case sf: case sfp: case sfpp: case fp: case rf: case rfz: case sfz: return getVolume(f);
		}
		throw new IllegalArgumentException(""+dynamics);
	}

}
