package musicxmltestsuite.tests.base;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.direction.*;
import com.xenoage.zong.core.music.direction.Pedal.Type;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;
import musicxmltestsuite.tests.utils.ToDo;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;


@ToDo("several yet unsupported direction elements")
public interface Base31a
	extends Base {

	@Override default String getFileName() {
		return "31a-Directions.xml";
	}
	
	List<Tuple2<MP, ?>> expectedDirections = getExpectedDirections();
	
	static List<Tuple2<MP, ?>> getExpectedDirections() {
		//TODO: not all directions are supported yet. return only those which are supported now
		Wedge w1, w2;
		return alist(
			//TODO: 4 rehearsal marks
			//t(atBeat(0, 1, 0, fr(0, 4)), new Segno()), - moved to beginning of measure
			//t(atBeat(0, 1, 0, fr(1, 4)), new Coda()), - moved to end of measure
			t(atBeat(0, 1, 0, Companion.fr(2, 4)), new Words(new UnformattedText("words"))),
			//TODO: eyeglasses
			t(atBeat(0, 2, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.p)),
			t(atBeat(0, 2, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.pp)),
			t(atBeat(0, 2, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.ppp)),
			t(atBeat(0, 2, 0, Companion.fr(3, 4)), new Dynamic(DynamicValue.pppp)),
			t(atBeat(0, 3, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.ppppp)),
			t(atBeat(0, 3, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.pppppp)),
			t(atBeat(0, 3, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.f)),
			t(atBeat(0, 3, 0, Companion.fr(3, 4)), new Dynamic(DynamicValue.ff)),
			t(atBeat(0, 4, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.fff)),
			t(atBeat(0, 4, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.ffff)),
			t(atBeat(0, 4, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.fffff)),
			t(atBeat(0, 4, 0, Companion.fr(3, 4)), new Dynamic(DynamicValue.ffffff)),
			t(atBeat(0, 5, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.mp)),
			t(atBeat(0, 5, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.mf)),
			t(atBeat(0, 5, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.sf)),
			t(atBeat(0, 5, 0, Companion.fr(3, 4)), new Dynamic(DynamicValue.sfp)),
			t(atBeat(0, 6, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.sfpp)),
			t(atBeat(0, 6, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.fp)),
			t(atBeat(0, 6, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.rf)),
			t(atBeat(0, 6, 0, Companion.fr(3, 4)), new Dynamic(DynamicValue.rfz)),
			t(atBeat(0, 7, 0, Companion.fr(0, 4)), new Dynamic(DynamicValue.sfz)),
			t(atBeat(0, 7, 0, Companion.fr(1, 4)), new Dynamic(DynamicValue.sffz)),
			t(atBeat(0, 7, 0, Companion.fr(2, 4)), new Dynamic(DynamicValue.fz)),
			//TODO: dynamics with additional text
			t(atBeat(0, 8, 0, Companion.fr(0, 4)), w1 = new Wedge(WedgeType.Crescendo)),
			t(atBeat(0, 8, 0, Companion.fr(1, 4)), new WedgeEnd(w1)),
			//TODO: dashes
			//TODO: bracket
			//TODO: octave shift
			t(atBeat(0, 10, 0, Companion.fr(0, 4)), new Pedal(Type.Start)),
			//TODO: pedal change
			t(atBeat(0, 10, 0, Companion.fr(3, 4)), new Pedal(Type.Stop)),
			t(atBeat(0, 11, 0, Companion.fr(0, 4)), new Tempo(Companion.fr(1, 4), 60)),
			//TODO: harp-pedals, damp, damp-all, scordatura, accordion-registration
			t(atBeat(0, 13, 0, Companion.fr(0, 4)), alist(
				new Words(new UnformattedText("subito")), //we do not test formatting here
				//new Words(new UnformattedText(" ")), //TODO: currently this second element is ignored
				new Dynamic(DynamicValue.p))),
			t(atBeat(0, 13, 0, Companion.fr(1, 4)), alist(
				new Dynamic(DynamicValue.ppp),
				w2 = new Wedge(WedgeType.Crescendo))),
			t(atBeat(0, 13, 0, Companion.fr(2, 4)), alist(
				new WedgeEnd(w2),
				new Dynamic(DynamicValue.ffff))));
	}

}
