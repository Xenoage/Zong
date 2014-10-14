package musicxmltestsuite.report;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum TestStatus {
	Complete("Test complete and successful"),
	Incomplete("Test successful, but incomplete (missing features or tests)"),
	Failure("Test fails"),
	SupportedButNotTestedYet("Feature is probably supported, but no tests exists yet"),
	SupportedButTestUnneeded("Feature is supported, but a test would not be useful or the feature is implicitly tested by another test"),
	UnsupportedToDo("Feature is unsupported, but will probably be added in the future"),
	Unsupported("Feature is unsupported, and will probably not be added in the future");
	
	public final String description;
}
