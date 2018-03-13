package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;

public interface Base45d
	extends Base {

	@Override default String getFileName() {
		return "45d-Repeats-Nested-Alternatives.xml";
	}
	
	Barline[] expectedStartBarlines = new Barline[12];

	Barline[] expectedEndBarlines = {
		null,
		Barline.Companion.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		Barline.Companion.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		Barline.Companion.barline(BarlineStyle.LightHeavy),
	};
	
	Volta[] expectedVoltas = {
		null,
		new Volta(1, range(1, 1), "1.", true),
		new Volta(3, range(2, 2), "2.", true),
		null,
		null,
		new Volta(1, range(3, 3), "3.", false),
		null,
		null,
		null,
		new Volta(1, range(1, 1), "1.", true),
		new Volta(1, range(5, 5), "5.", true),
		null,
	};
	
}
