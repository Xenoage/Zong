package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;

public interface Base45g
	extends Base {

	@Override default String getFileName() {
		return "45g-Repeats-NotEnded.xml";
	}
	
	Barline[] expectedStartBarlines = {
		null,
		Barline.barlineForwardRepeat(BarlineStyle.HeavyLight),
	};

	Barline[] expectedEndBarlines = {
		null,
		Barline.barline(BarlineStyle.LightHeavy),
	};
	
}
