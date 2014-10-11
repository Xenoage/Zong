package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.Pitch;

/**
 * All pitches from G to c”” in ascending steps; First without accidentals,
 * then with a sharp and then with a flat accidental.
 * Double alterations and cautionary accidentals are tested at the end. 
 * 
 * @author Andreas Wenger
 */
public interface Base01a
	extends Base {

	@Override default String getFileName() {
		return "01a-Pitches-Pitches.xml";
	}

	default Pitch[] getExpectedPitches() {
		Pitch[] expectedPitches = new Pitch[24 * 4 + 6];
		int iPitch = 0;
		for (int alter : new int[] { 0, 1, -1 }) {
			Pitch nextPitch = pi('G', alter, 2);
			for (int i = 0; i < 8 * 4; i++) {
				expectedPitches[iPitch++] = nextPitch;
				int newStep = nextPitch.getStep() + 1;
				int newOctave = nextPitch.getOctave();
				if (newStep > 6) {
					newStep = 0;
					newOctave += 1;
				}
				nextPitch = pi(newStep, alter, newOctave);
			}
		}
		expectedPitches[iPitch++] = pi(0, 2, 5);
		expectedPitches[iPitch++] = pi(0, -2, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		return expectedPitches;
	}

}
