package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for methods that are guaranteed to not return null
 * and for fields and variables that may not contain null, but
 * may return or contain an empty collection.
 * 
 * Can be used to mark collections, where it is unclear if
 * they always contain at least one element.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.CLASS) 
public @interface MaybeEmpty {
}
