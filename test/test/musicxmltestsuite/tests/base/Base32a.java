package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static musicxmltestsuite.tests.utils.Utils.articulation;
import static musicxmltestsuite.tests.utils.Utils.fermata;

import java.util.List;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.annotation.Ornament;
import com.xenoage.zong.core.music.annotation.OrnamentType;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.position.MP;


@ToDo("several yet unsupported notation elements")
public interface Base32a
	extends Base {

	@Override default String getFileName() {
		return "32a-Notations.xml";
	}
	
	List<Tuple2<MP, ?>> expectedAnnotations = getExpectedAnnotations();
	
	static List<Tuple2<MP, ?>> getExpectedAnnotations() {
		//TODO: not all notations are supported yet. return only those which are supported now
		return alist(
			t(atBeat(0, 0, 0, Companion.fr(0, 4)), fermata(Placement.Above)),
			t(atBeat(0, 0, 0, Companion.fr(1, 4)), new Fermata()),
			//TODO: angled fermata
			//TODO: square fermata
			//t(atBeat(0, 1, 0, fr(0, 4)), fermata(Placement.Below)),
			//TODO: arp.
			//TODO: non.arp.
			//TODO: acc.mark
			t(atBeat(0, 2, 0, Companion.fr(0, 4)), new Articulation(ArticulationType.Accent)),
			t(atBeat(0, 2, 0, Companion.fr(1, 4)), new Articulation(ArticulationType.Marcato)),
			t(atBeat(0, 2, 0, Companion.fr(2, 4)), new Articulation(ArticulationType.Staccato)),
			t(atBeat(0, 2, 0, Companion.fr(3, 4)), new Articulation(ArticulationType.Tenuto)),
			//TODO: det.-leg.
			t(atBeat(0, 3, 0, Companion.fr(1, 4)), new Articulation(ArticulationType.Staccatissimo)),
			//TODO: ... more notations ...
			t(atBeat(0, 6, 0, Companion.fr(0, 4)), new Ornament(OrnamentType.Trill)),
			t(atBeat(0, 6, 0, Companion.fr(1, 4)), new Ornament(OrnamentType.Turn)),
			//TODO: del.turn
			t(atBeat(0, 6, 0, Companion.fr(3, 4)), new Ornament(OrnamentType.InvertedTurn)),
			//TODO: ... more notations ...
			t(atBeat(0, 8, 0, Companion.fr(0, 4)), new Ornament(OrnamentType.Mordent)),
			t(atBeat(0, 8, 0, Companion.fr(1, 4)), new Ornament(OrnamentType.InvertedMordent)),
			//TODO: ... more notations ...
			t(atBeat(0, 21, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.f)),
			t(atBeat(0, 21, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.ppp)),
			t(atBeat(0, 21, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.sfp)),
			//TODO: other dynamic "sfffz"
			t(atBeat(0, 22, 0, Companion.fr(0, 4)), alist(
				articulation(ArticulationType.Staccato, Placement.Above),
				articulation(ArticulationType.Marcato, Placement.Above))),
			t(atBeat(0, 22, 0, Companion.fr(1, 4)), alist(
				articulation(ArticulationType.Staccato, Placement.Above),
				articulation(ArticulationType.Accent, Placement.Below),
				articulation(ArticulationType.Tenuto, Placement.Below))));
	}

}
