package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;

import java.util.List;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Pedal.Type;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeEnd;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;


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
			t(atBeat(0, 1, 0, fr(0, 4)), new Segno()),
			t(atBeat(0, 1, 0, fr(1, 4)), new Coda()),
			t(atBeat(0, 1, 0, fr(2, 4)), new Words(new UnformattedText("words"))),
			//TODO: eyeglasses
			t(atBeat(0, 2, 0, fr(0, 4)), new Dynamics(DynamicsType.p)),
			t(atBeat(0, 2, 0, fr(1, 4)), new Dynamics(DynamicsType.pp)),
			t(atBeat(0, 2, 0, fr(2, 4)), new Dynamics(DynamicsType.ppp)),
			t(atBeat(0, 2, 0, fr(3, 4)), new Dynamics(DynamicsType.pppp)),
			t(atBeat(0, 3, 0, fr(0, 4)), new Dynamics(DynamicsType.ppppp)),
			t(atBeat(0, 3, 0, fr(1, 4)), new Dynamics(DynamicsType.pppppp)),
			t(atBeat(0, 3, 0, fr(2, 4)), new Dynamics(DynamicsType.f)),
			t(atBeat(0, 3, 0, fr(3, 4)), new Dynamics(DynamicsType.ff)),
			t(atBeat(0, 4, 0, fr(0, 4)), new Dynamics(DynamicsType.fff)),
			t(atBeat(0, 4, 0, fr(1, 4)), new Dynamics(DynamicsType.ffff)),
			t(atBeat(0, 4, 0, fr(2, 4)), new Dynamics(DynamicsType.fffff)),
			t(atBeat(0, 4, 0, fr(3, 4)), new Dynamics(DynamicsType.ffffff)),
			t(atBeat(0, 5, 0, fr(0, 4)), new Dynamics(DynamicsType.mp)),
			t(atBeat(0, 5, 0, fr(1, 4)), new Dynamics(DynamicsType.mf)),
			t(atBeat(0, 5, 0, fr(2, 4)), new Dynamics(DynamicsType.sf)),
			t(atBeat(0, 5, 0, fr(3, 4)), new Dynamics(DynamicsType.sfp)),
			t(atBeat(0, 6, 0, fr(0, 4)), new Dynamics(DynamicsType.sfpp)),
			t(atBeat(0, 6, 0, fr(1, 4)), new Dynamics(DynamicsType.fp)),
			t(atBeat(0, 6, 0, fr(2, 4)), new Dynamics(DynamicsType.rf)),
			t(atBeat(0, 6, 0, fr(3, 4)), new Dynamics(DynamicsType.rfz)),
			t(atBeat(0, 7, 0, fr(0, 4)), new Dynamics(DynamicsType.sfz)),
			t(atBeat(0, 7, 0, fr(1, 4)), new Dynamics(DynamicsType.sffz)),
			t(atBeat(0, 7, 0, fr(2, 4)), new Dynamics(DynamicsType.fz)),
			//TODO: dynamics with additional text
			t(atBeat(0, 8, 0, fr(0, 4)), w1 = new Wedge(WedgeType.Crescendo)),
			t(atBeat(0, 8, 0, fr(1, 4)), new WedgeEnd(w1)),
			//TODO: dashes
			//TODO: bracket
			//TODO: octave shift
			t(atBeat(0, 10, 0, fr(0, 4)), new Pedal(Type.Start)),
			//TODO: pedal change
			t(atBeat(0, 10, 0, fr(3, 4)), new Pedal(Type.Stop)),
			t(atBeat(0, 11, 0, fr(0, 4)), new Tempo(fr(1, 4), 60)),
			//TODO: harp-pedals, damp, damp-all, scordatura, accordion-registration
			t(atBeat(0, 13, 0, fr(0, 4)), alist(
				new Words(new UnformattedText("subito")), //we do not test formatting here
				//new Words(new UnformattedText(" ")), //TODO: currently this second element is ignored
				new Dynamics(DynamicsType.p))),
			t(atBeat(0, 13, 0, fr(1, 4)), alist(
				new Dynamics(DynamicsType.ppp),
				w2 = new Wedge(WedgeType.Crescendo))),
			t(atBeat(0, 13, 0, fr(2, 4)), alist(
				new WedgeEnd(w2),
				new Dynamics(DynamicsType.ffff))));
	}

}
