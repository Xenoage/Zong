package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.music.Pitch;


public interface Base72b
	extends Base {

	@Override default String getFileName() {
		return "72b-TransposingInstruments-Full.xml";
	}
	
	Transpose[] expectedTransposes = {
		new Transpose(3, 2, 0, false),
		new Transpose(-2, -1, 0, false),
		new Transpose(-3, -2, 0, false),
		new Transpose(-7, -4, 0, false),
		new Transpose(-9, -5, 0, false),
		new Transpose(9, 5, 0, false),
		new Transpose(-2, -1, 0, false),
			Transpose.Companion.getNoTranspose(),
		new Transpose(2, 1, 0, false),
		new Transpose(6, 3, 3, false),
			Transpose.Companion.getNoTranspose(),
	};
	
	Pitch expectedSoundPitch = Companion.pi('C', 0, 5);
	
	Pitch[] expectedNotatedPitches = {
		Companion.pi('A', 0, 4),
		Companion.pi('D', 0, 5),
		Companion.pi('E', -1, 5),
		Companion.pi('G', 0, 5),
		Companion.pi('A', 0, 5),
		Companion.pi('E', -1, 4),
		Companion.pi('D', 0, 5),
		expectedSoundPitch,
		Companion.pi('B', -1, 4),
		Companion.pi('F', 1, 1),
		expectedSoundPitch,
	};
	
	

}
