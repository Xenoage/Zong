package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.Pitch;


public interface Base01b
	extends Base {

	@Override default String getFileName() {
		return "01b-Pitches-Intervals.xml";
	}

	default Pitch[] getExpectedPitches() {
		Pitch[] expectedPitches = new Pitch[41 * 2];
		Pitch pi1 = Companion.pi(0, 0, 5);
		Pitch pi2 = Companion.pi(0, 0, 5);
		for (int i = 0; i < expectedPitches.length / 2; i++) {
			expectedPitches[i * 2 + 0] = pi1;
			expectedPitches[i * 2 + 1] = pi2;
			pi1 = incHalftoneWithEnharmonicChange(pi1);
			pi2 = decHalftoneWithEnharmonicChange(pi2);
		}
		return expectedPitches;
	}

	default Pitch incHalftoneWithEnharmonicChange(Pitch p) {
		int step = p.getStep();
		int alter = p.getAlter();
		int octave = p.getOctave();
		if (alter == 1) {
			//next step
			alter = -1;
			step += 1;
			if (step > 6) {
				step = 0;
				octave += 1;
			}
		}
		else {
			alter += 1;
		}
		return Companion.pi(step, alter, octave);
	}

	default Pitch decHalftoneWithEnharmonicChange(Pitch p) {
		int step = p.getStep();
		int alter = p.getAlter();
		int octave = p.getOctave();
		if (alter == -1) {
			//next step
			alter = 1;
			step -= 1;
			if (step < 0) {
				step = 6;
				octave -= 1;
			}
		}
		else {
			alter -= 1;
		}
		return Companion.pi(step, alter, octave);
	}

}
