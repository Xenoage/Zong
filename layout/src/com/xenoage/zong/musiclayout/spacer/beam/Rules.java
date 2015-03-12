package com.xenoage.zong.musiclayout.spacer.beam;


public class Rules {

	boolean isCloseSpacing(float minStemDistanceIs) {
		//Ross, p. 100 and p. 112:
		//we use close spacing, if the distance is less than 3 or 4 spaces
		//we use the average value, 3.5
		return minStemDistanceIs < 3.5;
	}
	
	float getCloseSpacingSlantIs() {
		//Ross, p. 112: beams in close spacing slant only 1/4 to 1/2 space
		return 0.5f; //or 0.25
	}
	
	float getStemLengthIs() {
		//Ross, p. 103: when using normal spacing, shortening the stems is better
		//than lengthening them.
		return getNormalStemLengthIs();
	}

	float getNormalStemLengthIs() {
		//Ross, p. 102: a normal full-length stem is 3.5 spaces long (one octave)
		return 3.5f;
	}
	
	float getMaxSlantIs() {
		//Ross, p. 98: if in doubt, do not exceed a slant of one space
		return 1;
	}
	
	float getSlantIs() {
		//Ross, p. 103: beams outside the staff do not required exact slants,
		//since then wedge printing problem does not exist there
		return 0;
	}
	
}
