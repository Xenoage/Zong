package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;

public interface Base45f
	extends Base {

	@Override default String getFileName() {
		return "45f-Repeats-InvalidEndings.xml";
	}
	
	Barline[] expectedStartBarlines = new Barline[5];

	Barline[] expectedEndBarlines = {
		null,
		null,
		null,
		Barline.Companion.barlineBackwardRepeat(BarlineStyle.LightHeavy, 1),
		Barline.Companion.barline(BarlineStyle.LightHeavy),
	};
	
	Volta[] expectedVoltas = {
		null,
		new Volta(1, range(1, 3), "1." + Volta.Companion.getDash() + "3.", true),
		new Volta(1, range(2, 2), "2.", false),
		null,
		null,
	};
	
}
