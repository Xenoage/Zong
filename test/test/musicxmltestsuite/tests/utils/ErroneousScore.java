package musicxmltestsuite.tests.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for tests with erroneous scores.
 * These may not be loaded correctly.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ErroneousScore {

	String value() default "";
	
}
