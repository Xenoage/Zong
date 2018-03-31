package com.xenoage.utils.log;

/**
 * Logging level.
 * 
 * @author Andreas Wenger
 */
public enum Level {

	/**
	 * Error message is shown, program has to be closed.
	 * For example, if inconsistent score data is found.
	 */
	Fatal("FATAL   "),

	/**
	 * Error message is shown, program can continue.
	 * For example, if a command fails that should always work.
	 */
	Error("ERROR   "),

	/**
	 * Just a warning, which is shown on the screen, too.
	 * For example, if a file can not be opened because it doesn't exist.
	 */
	Warning("WARNING "),

	/**
	 * An event, which is not interesting for the user, but should be logged.
	 * For example, if a opened file has an invalid format, but can be opened anyway.
	 */
	Remark("REMARK  ");

	private String fixedString;


	private Level(String fixedString) {
		this.fixedString = fixedString;
	}

	/**
	 * Returns true, if this log level is included in the given other level.
	 * E.g. {@link Level#Remark} includes all other levels, but {@link Level#Error}
	 * does not include {@link Level#Warning}.
	 */
	public boolean isIncludedIn(Level topLevel) {
		return this.ordinal() <= topLevel.ordinal();
	}

	/**
	 * Returns a 8 character string for this log level.
	 */
	public String getFixedString() {
		return fixedString;
	}

}
