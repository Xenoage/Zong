package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.unknown;

import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.position.MP;

public interface Base43e
	extends Base {

	@Override default String getFileName() {
		return "43e-Multistaff-ClefDynamics.xml";
	}
	
	List<Tuple2<MP, Clef>> expectedClefs = getExpectedClefs();
	List<Tuple2<MP, Direction>> expectedDirections = getExpectedDirections();
	List<Tuple2<MP, Key>> expectedKeys = getExpectedKeys();

	static List<Tuple2<MP, Clef>> getExpectedClefs() {
		List<Tuple2<MP, Clef>> clefs = alist();
		clefs.add(t(atBeat(0, 0, unknown, _0), new Clef(ClefType.clefTreble)));
		clefs.add(t(atBeat(0, 2, unknown, _0), new Clef(new ClefType(ClefSymbol.C, 2))));
		clefs.add(t(atBeat(1, 0, unknown, _0), new Clef(ClefType.clefBass)));
		clefs.add(t(atBeat(1, 1, unknown, _0), new Clef(ClefType.clefTreble)));
		return clefs;
	}
	
	static List<Tuple2<MP, Direction>> getExpectedDirections() {
		Wedge wedge;
		List<Tuple2<MP, Direction>> directions = alist();
		directions.add(t(atBeat(0, 0, unknown, _0), new Dynamic(DynamicValue.ffff)));
		directions.add(t(atBeat(0, 0, unknown, fr(3, 4)), new Dynamic(DynamicValue.p)));
		directions.add(t(atBeat(1, 0, unknown, _0), wedge = new Wedge(WedgeType.Crescendo)));
		directions.add(t(atBeat(1, 0, unknown, fr(2, 4)), wedge.getWedgeEnd()));
		return directions;
	}
	
	static List<Tuple2<MP, Key>> getExpectedKeys() {
		List<Tuple2<MP, Key>> keys = alist();
		keys.add(t(atBeat(unknown, 0, unknown, _0), new TraditionalKey(0)));
		keys.add(t(atBeat(unknown, 1, unknown, _0), new TraditionalKey(2)));
		return keys;
	}
	
}
