package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for methods that are guaranteed to not return null
 * and for fields and variables that may not contain null, and
 * also return or contain an non-empty collection (with at least
 * one item) or string.
 * 
 * Can be used to mark collections, where it is unclear if
 * they always contain at least one element, or strings which
 * are not allowed to be empty.
 * 
 * @deprecated Use {@link NonEmpty}. Renamed for better readability.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.CLASS)
public @interface NeverEmpty {
}
