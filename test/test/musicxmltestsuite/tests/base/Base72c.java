package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.music.Pitch;


public interface Base72c
	extends Base {

	@Override default String getFileName() {
		return "72c-TransposingInstruments-Change.xml";
	}
	
	Transpose expectedTransposes[] = {
		new Transpose(3, 2, 0, false),
		new Transpose(-2, -1, 0, false),
		new Transpose(-2, -1, 0, false),
	};
	
	Pitch[] expectedSoundPitches = {
		Companion.pi('E', -1, 5),
		Companion.pi('B', -1, 4),
		Companion.pi('B', -1, 4),
	};
	
	Pitch expectedNotatedPitch = Companion.pi('C', 0, 4);
	
	

}
