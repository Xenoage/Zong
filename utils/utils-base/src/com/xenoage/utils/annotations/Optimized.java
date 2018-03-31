package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for entities which were optimized for performance
 * reasons or for saving memory. When this annotation is present,
 * the reader of the source code knows why the design of the code
 * may not optimal but was tuned for some reason.
 * 
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Optimized {
	
	/**
	 * Reason for an optimization.
	 */
	public enum Reason {
		Performance,
		MemorySaving,
		Unknown
	}
	
	Reason value() default Reason.Unknown;
	
}
