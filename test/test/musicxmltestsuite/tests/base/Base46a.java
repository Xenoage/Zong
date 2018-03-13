package musicxmltestsuite.tests.base;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;

@ToDo("tick and short barline not yet implemented")
public interface Base46a
	extends Base {

	@Override default String getFileName() {
		return "46a-Barlines.xml";
	}

	Barline[] expectedEndBarlines = {
		null,
		Barline.Companion.barline(BarlineStyle.Regular),
		Barline.Companion.barline(BarlineStyle.Dotted),
		Barline.Companion.barline(BarlineStyle.Dashed),
		Barline.Companion.barline(BarlineStyle.Heavy),
		Barline.Companion.barline(BarlineStyle.LightLight),
		Barline.Companion.barline(BarlineStyle.LightHeavy),
		Barline.Companion.barline(BarlineStyle.HeavyLight),
		Barline.Companion.barline(BarlineStyle.HeavyHeavy),
		null, //TODO: tick
		null, //TODO: short
		Barline.Companion.barline(BarlineStyle.None),
		null,
	};
	
}
