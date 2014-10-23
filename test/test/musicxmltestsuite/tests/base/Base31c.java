package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;

import java.util.List;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.position.MP;

/**
 * Tempo Markings: note=bpm, text (note=bpm), note=note, (note=note), (note=bpm) 
 * 
 * @author Andreas Wenger
 */
@ToDo("not all tempos are supported yet")
public interface Base31c
	extends Base {

	@Override default String getFileName() {
		return "31c-MetronomeMarks.xml";
	}
	
	List<Tuple2<MP, Tempo>> expectedTempos =
		//return only those which are supported now
		alist(
			t(atBeat(0, 0, 0, fr(0, 4)), new Tempo(fr(3, 8), 100))
			//TODO: atBeat(0, 0, 0, fr(3, 4)): longa = 100
			//TODO: atBeat(0, 1, 0, fr(0, 4)): fr(3, 8) = fr(3, 4)
			//TODO: atBeat(0, 1, 0, fr(3, 4)): longa = fr(3, 64)
			//TODO: atBeat(0, 2, 0, fr(0, 4)): fr(3, 8) = fr(3, 4) in parens
			//TODO: atBeat(0, 2, 0, fr(3, 4)): fr(3, 8) = 77 in parens
			);

}
