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
		Barline.barline(BarlineStyle.Regular),
		Barline.barline(BarlineStyle.Dotted),
		Barline.barline(BarlineStyle.Dashed),
		Barline.barline(BarlineStyle.Heavy),
		Barline.barline(BarlineStyle.LightLight),
		Barline.barline(BarlineStyle.LightHeavy),
		Barline.barline(BarlineStyle.HeavyLight),
		Barline.barline(BarlineStyle.HeavyHeavy),
		null, //TODO: tick
		null, //TODO: short
		Barline.barline(BarlineStyle.None),
		null,
	};
	
}
