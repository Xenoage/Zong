package com.xenoage.zong.core.music.time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;


/**
 * A time signature.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public class TimeSignature
	implements ColumnElement {
	
	/** Implicit senza misura object. Do not use it within a score,
	 * but only as a return value to indicate that there is no time signature. */
	public static final TimeSignature implicitSenzaMisura = new TimeSignature(TimeType.timeSenzaMisura);

	/** The time signature type. */
	@Getter @Setter @NonNull private TimeType type;

	/** Back reference: the parent column header, or null, if not part of a score. */
	@Getter @Setter private ColumnHeader parent = null;

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Time;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
