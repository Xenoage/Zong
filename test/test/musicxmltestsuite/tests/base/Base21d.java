package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static musicxmltestsuite.tests.utils.Utils.articulation;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.fermata;

import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;


public interface Base21d
	extends Base {

	@Override default String getFileName() {
		return "21d-Chords-SchubertStabatMater.xml";
	}
	
	default Chord[] getExpectedChords() {
		Chord[] expectedChords = {
			ch(Companion.fr(1, 1), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('C', 0, 5)),
			ch(Companion.fr(3, 8), Companion.pi('F', 0, 4), Companion.pi('A', -1, 4)),
			ch(Companion.fr(1, 8), Companion.pi('F', 0, 4), Companion.pi('A', -1, 4)),
			ch(Companion.fr(1, 4), Companion.pi('G', 0, 4), Companion.pi('B', -1, 4)),
			ch(Companion.fr(1, 4), Companion.pi('G', 0, 4), Companion.pi('B', -1, 4))
		};
		expectedChords[0].setAnnotations(alist(
			articulation(ArticulationType.Accent, Placement.Below),
			fermata(Placement.Above)));
		return expectedChords;
	}
	
	default List<Tuple2<MP, ? extends Direction>> getExpectedDirections() {
		List<Tuple2<MP, ? extends Direction>> expectedDirections = alist();
		Words largo = new Words(new UnformattedText("Largo"));
		largo.setPositioning(Placement.Above);
		expectedDirections.add(t(mp0, largo));
		Dynamic fp = new Dynamic(DynamicValue.fp);
		fp.setPositioning(Placement.Below);
		expectedDirections.add(t(mp0, fp));
		Dynamic p = new Dynamic(DynamicValue.p);
		fp.setPositioning(Placement.Below);
		expectedDirections.add(t(mp0.withMeasure(1), p));
		return expectedDirections;
	}

}
