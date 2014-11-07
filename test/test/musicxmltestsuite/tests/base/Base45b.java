package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.rangeReverse;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;

public interface Base45b
	extends Base {

	@Override default String getFileName() {
		return "45b-RepeatWithAlternatives.xml";
	}

	Barline[] expectedEndBarlines = {
		null,
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		null,
		Barline.barline(BarlineStyle.LightHeavy),
	};
	
	Volta[] expectedVoltas = {
		null,
		new Volta(1, rangeReverse(1, 1), "1.", true),
		new Volta(1, null, "1.", true),
		null,
	};
	
}
