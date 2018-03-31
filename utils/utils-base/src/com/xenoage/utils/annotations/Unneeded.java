package com.xenoage.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for code, that is never called, and may be removed later.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Unneeded {
}
