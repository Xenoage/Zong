package com.xenoage.zong.musiclayout.spacer.beam;

//TODO
public class Rules {
	
	float getStemLengthIs() {
		//TODO: Ross, p. 103: when using normal spacing, shortening the stems is better
		//than lengthening them.
		return 3.5f;
	}
	
	float getSlantIs() {
		//TODO: Ross, p. 103: beams outside the staff do not require exact slants,
		//since then wedge printing problem does not exist there
		return 0;
	}
	
}
