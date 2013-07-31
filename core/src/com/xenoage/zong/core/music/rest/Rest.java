package com.xenoage.zong.core.music.rest;

import static com.xenoage.utils.base.NullUtils.throwNullArg;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.base.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;


/**
 * Class for a rest.
 *
 * @author Andreas Wenger
 */
public final class Rest
	implements VoiceElement {

	/** The duration of this rest. */
	@Getter @Setter @NonNull private Fraction duration;
	/** True, if this rest has cue size, otherwise false. */
	@Getter @Setter private boolean cue = false;
	
	/** Back reference: the parent voice, or null if not part of a score. */
	@Getter @Setter private Voice parent = null;


	public Rest(Fraction duration) {
		throwNullArg(duration);
		this.duration = duration;
	}


	@Override public String toString() {
		return "rest(dur:" + duration + ")";
	}

}
