package com.xenoage.utils.kernel.functional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An informative annotation type used to indicate that an interface type declaration
 * is intended to be a functional interface as defined by the Java Language Specification.
 *
 * This class is normally provided by Java 8, but since we also want to support older
 * Android versions, we have to provide the implementation here at the moment.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {
}
