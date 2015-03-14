package com.xenoage.zong.musiclayout.spacer.beam;


public class Rules {
	
	float getStemLengthIs() {
		//Ross, p. 103: when using normal spacing, shortening the stems is better
		//than lengthening them.
		return 3.5f;
	}
	
	float getSlantIs() {
		//Ross, p. 103: beams outside the staff do not required exact slants,
		//since then wedge printing problem does not exist there
		return 0;
	}
	
}
