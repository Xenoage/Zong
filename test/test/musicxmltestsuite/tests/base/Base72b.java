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
		Transpose.noTranspose,
		new Transpose(2, 1, 0, false),
		new Transpose(6, 3, 3, false),
		Transpose.noTranspose,
	};
	
	Pitch expectedSoundPitch = pi('C', 0, 5);
	
	Pitch[] expectedNotatedPitches = {
		pi('A', 0, 4),
		pi('D', 0, 5),
		pi('E', -1, 5),
		pi('G', 0, 5),
		pi('A', 0, 5),
		pi('E', -1, 4),
		pi('D', 0, 5),
		expectedSoundPitch,
		pi('B', -1, 4),
		pi('F', 1, 1),
		expectedSoundPitch,
	};
	
	

}
