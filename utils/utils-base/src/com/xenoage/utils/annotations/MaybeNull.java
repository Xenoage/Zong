package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for methods that may return null
 * and for fields and variables that may contain null.
 * 
 * Can be used to mark methods and fields, where it is unclear if
 * null is allowed or not.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.CLASS)
public @interface MaybeNull {
}
