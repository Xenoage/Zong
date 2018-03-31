package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for demo code.
 * 
 * Demo code is code that is used for testing or demonstration, not with JUnit,
 * but with the application itself. Anyway this code should be placed in the test-folder,
 * when possible.
 * 
 * To distinguish between JUnit code and demo code this annotation should be used.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Demo {
}
