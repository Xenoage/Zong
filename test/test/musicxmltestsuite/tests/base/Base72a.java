package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.music.Pitch;


public interface Base72a
	extends Base {

	@Override default String getFileName() {
		return "72a-TransposingInstruments.xml";
	}
	
	Transpose[] expectedTransposes = {
		new Transpose(-2, -1, 0, false),
		new Transpose(-9, -5, 0, false),
			Transpose.Companion.getNoTranspose()
	};
	
	Pitch[] expectedSoundPitches = {
		Companion.pi('C', 0, 4), Companion.pi('D', 0, 4), Companion.pi('E', 0, 4), Companion.pi('F', 0, 4),
		Companion.pi('G', 0, 4), Companion.pi('A', 0, 4), Companion.pi('B', 0, 4), Companion.pi('C', 0, 5),
	};
	
	Pitch[][] expectedNotatedPitches = {
		{
			Companion.pi('D', 0, 4), Companion.pi('E', 0, 4), Companion.pi('F', 1, 4), Companion.pi('G', 0, 4),
			Companion.pi('A', 0, 4), Companion.pi('B', 0, 4), Companion.pi('C', 1, 5), Companion.pi('D', 0, 5),
		},
		{
			Companion.pi('A', 0, 4), Companion.pi('B', 0, 4), Companion.pi('C', 1, 5), Companion.pi('D', 0, 5),
			Companion.pi('E', 0, 5), Companion.pi('F', 1, 5), Companion.pi('G', 1, 5), Companion.pi('A', 0, 5),
		},
		expectedSoundPitches
	};
	
	

}
