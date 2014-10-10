package com.xenoage.zong.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;


/**
 * This exception is thrown when an element
 * should be added to a measure, but there
 * were not enough beats for it between
 * the insert position and the end bar line.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter public class MeasureFullException
	extends RuntimeException {

	private final MP mp;
	private final Fraction requestedDuration;


	@Override public String getMessage() {
		return "Measure is full. Requested duration = " + requestedDuration + ". MP: " + mp;
	}

}
