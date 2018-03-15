package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.Pitch;


public interface Base01a
	extends Base {

	@Override default String getFileName() {
		return "01a-Pitches-Pitches.xml";
	}

	default Pitch[] getExpectedPitches() {
		Pitch[] expectedPitches = new Pitch[24 * 4 + 6];
		int iPitch = 0;
		for (int alter : new int[] { 0, 1, -1 }) {
			Pitch nextPitch = Companion.pi('G', alter, 2);
			for (int i = 0; i < 8 * 4; i++) {
				expectedPitches[iPitch++] = nextPitch;
				int newStep = nextPitch.getStep() + 1;
				int newOctave = nextPitch.getOctave();
				if (newStep > 6) {
					newStep = 0;
					newOctave += 1;
				}
				nextPitch = Companion.pi(newStep, alter, newOctave);
			}
		}
		expectedPitches[iPitch++] = Companion.pi(0, 2, 5);
		expectedPitches[iPitch++] = Companion.pi(0, -2, 5);
		expectedPitches[iPitch++] = Companion.pi(0, 1, 5);
		expectedPitches[iPitch++] = Companion.pi(0, 1, 5);
		expectedPitches[iPitch++] = Companion.pi(0, 1, 5);
		expectedPitches[iPitch++] = Companion.pi(0, 1, 5);
		return expectedPitches;
	}
	
	default int[] getExpectedLPs() {
		int[] expectedLPs = new int[24 * 4 + 6];
		for (int i1 : range(3))
			for (int i2 : range(8 * 4))
				expectedLPs[i1 * 8 * 4 + i2] = -12 + i2;
		for (int i : range(4))
			expectedLPs[3 * 8 * 4 + i] = 5;
		return expectedLPs;
	}

}
