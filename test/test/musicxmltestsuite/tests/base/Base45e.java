package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;

public interface Base45e
	extends Base {

	@Override default String getFileName() {
		return "45e-Repeats-Nested-Alternatives.xml";
	}
	
	Barline[] expectedStartBarlines = {
		null,
		null,
		null,
		null,
		Barline.barlineForwardRepeat(BarlineStyle.HeavyLight),
		null,
		null,
		Barline.barlineForwardRepeat(BarlineStyle.HeavyLight),
		null,
		null
	};

	Barline[] expectedEndBarlines = {
		null,
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		null,
		null,
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		null,
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		null,
		Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		Barline.barline(BarlineStyle.LightHeavy),
	};
	
	Volta[] expectedVoltas = {
		null,
		new Volta(1, range(1, 1), "1.", true),
		new Volta(1, range(2, 2), "2.", false),
		null,
		null,
		null,
		new Volta(1, range(1, 1), "1.", true),
		null,
		null,
		null,
	};
	
}
