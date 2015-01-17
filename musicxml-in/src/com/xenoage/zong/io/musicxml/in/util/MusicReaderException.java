package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.zong.io.musicxml.in.readers.ScoreReader;
import com.xenoage.zong.io.musicxml.in.readers.Context;

/**
 * Exception within a {@link ScoreReader}.
 * 
 * @author Andreas Wenger
 */
public final class MusicReaderException
	extends RuntimeException {

	private final Context context;


	public MusicReaderException(String message, Context context) {
		super(message);
		this.context = context;
	}

	public MusicReaderException(Throwable cause, Context context) {
		super(cause);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

}
