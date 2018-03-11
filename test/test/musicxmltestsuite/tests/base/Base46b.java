package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.unknown;

import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.position.MP;

public interface Base46b
	extends Base {

	@Override default String getFileName() {
		return "46b-MidmeasureBarline.xml";
	}
	
	List<Tuple2<MP, Barline>> expectedMiddleBarlines = getExpectedMiddleBarlines();
	
	static List<Tuple2<MP, Barline>> getExpectedMiddleBarlines() {
		List<Tuple2<MP, Barline>> ret = alist();
		ret.add(t(atBeat(1, 0, unknown, Companion.fr(2, 4)), Barline.barline(BarlineStyle.Dotted)));
		return ret;
	}
	
}
