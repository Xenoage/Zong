package com.xenoage.zong.musicxml.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for types that do not completely meet the MusicXML
 * specification, since some values are missing.
 * 
 * Items which are deprecated since MusicXML 2.0 need not to be
 * marked as incomplete.
 *
 * @author Andreas Wenger
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface IncompleteMusicXML
{
	
	/**
	 * Comma-separated list of locally completely missing elements, attributes, ...
	 */
	String missing() default "";
	
	/**
	 * Comma-separated list of locally partly implemented elements, attributes, ...
	 */
	String partly() default "";
	
	/**
	 * Comma-separated list of incompletly implemented child elements.
	 * This is used, if a children, a children of a children, and so on, is incomplete.
	 * That means, if any element of the whole tree is incomplete, all elements from there
	 * up to the root are marked with this annotation.
	 */
	String children() default "";
	
}
