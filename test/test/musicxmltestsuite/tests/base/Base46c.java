package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.unknown;

import java.util.List;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.position.MP;

@ToDo("implicit measures not supported yet")
public interface Base46c
	extends Base {

	@Override default String getFileName() {
		return "46c-Midmeasure-Clef.xml";
	}
	
	List<Tuple2<MP, Clef>> expectedClefs = getExpectedClefs();

	static List<Tuple2<MP, Clef>> getExpectedClefs() {
		List<Tuple2<MP, Clef>> clefs = alist();
		clefs.add(t(atBeat(0, 0, unknown, Companion.get_0()), new Clef(ClefType.clefTreble)));
		clefs.add(t(atBeat(0, 2, unknown, Companion.get_0()), new Clef(new ClefType(ClefSymbol.C, 2))));
		clefs.add(t(atBeat(0, 3, unknown, Companion.fr(2, 4)), new Clef(ClefType.clefTreble)));
		return clefs;
	}
	
}
