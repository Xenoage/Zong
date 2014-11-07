package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;

public interface Base45a
	extends Base {

	@Override default String getFileName() {
		return "45a-SimpleRepeat.xml";
	}

	Barline[] expectedEndBarlines = {
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 5),
		Barline.barline(BarlineStyle.LightHeavy),
	};
	
}
