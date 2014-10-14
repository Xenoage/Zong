package musicxmltestsuite.report;


public enum TestStatus {
	/** Test complete and successful. */
	Complete,
	/** Test successful, but incomplete. */
	Incomplete,
	/** Test fails. */
	Failure,
	/** Feature is supported, but no tests exists yet. */
	SupportedButNotTestedYet,
	/** Feature is supported, but the test is not useful and so there is none. */
	SupportedButTestUnneeded,
	/** Feature is unsupported, but will probably be added in the future. */
	UnsupportedToDo,
	/** Feature is unsupported, and will probably not be added in the future. */
	Unsupported,
}
