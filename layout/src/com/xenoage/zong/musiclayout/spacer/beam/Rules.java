package com.xenoage.zong.musiclayout.spacer.beam;


public class Rules {
	
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
