package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;

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
		new Volta(1, range(1, 1), "1.", true),
		new Volta(1, range(2, 2), "2.", false),
		null,
	};
	
}
