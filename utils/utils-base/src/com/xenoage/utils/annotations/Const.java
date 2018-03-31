package com.xenoage.utils.annotations;


/**
 * Annotation for immutable classes.
 * 
 * Instances of classes with this annotation may be shared,
 * because their values can never change.
 * 
 * It is not forbidden that the class contains collections, whose contents
 * are still mutable from a technical point of view, but there may be no public
 * modifiers to make manipulation simple.
 *
 * @author Andreas Wenger
 */
public @interface Const {
}
