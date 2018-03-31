package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for methods that are guaranteed to not return null
 * and for fields and variables that may not contain null.
 * 
 * Can be used to mark methods and fields, where it is unclear if
 * null is allowed or not.
 * 
 * When used in combination with the Lombok library, generated constructors
 * and setters will assert non-null values.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.CLASS)
public @interface NonNull {
}
