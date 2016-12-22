package com.xenoage.zong.io.musicxml.in.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * Reacts on error messages when loading a MusicXML document.
 * Errors can be silently ignored, logged or an exception can be thrown.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class ErrorHandling {
	
	public enum Level {
		/** Ignore errors as far as possible. Try to read the MusicXML document anyway,
		 * and ignore or add information when needed. */
		Ignore,
		/** Log errors as warnings. Try to read the MusicXML document anyway,
		 * and ignore or add information when needed. */
		LogErrors,
		/** Throw a {@link MusicReaderException} as soon as an error is detected. */
		ThrowException;
	}
	
	/** How to deal with errors when loading MusicXML. */
	@Getter private final Level level;
	
	/** List of error messages collected when using {@link Level#LogErrors} */
	@Getter private List<String> errorMessages = alist();
	
	
	public void reportError(String message) {
		if (level == Level.LogErrors)
			errorMessages.add(message);
		else if (level == Level.ThrowException)
			throw new RuntimeException(message);
	}
	
}
