package material.stem.length;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.stem.length.Example.example;

import java.util.List;

import lombok.Getter;
import material.ChlapikSuite;

/**
 * Examples from Chlapik, "Das Stielen", section 5 and 6
 * (p. 39)
 * 
 * @author Andreas Wenger
 */
public class ChlapikStemLength
	implements ChlapikSuite<Example> {

	@Getter List<Example> examples = alist(
		//lengthened to middle line
		example("p39 5 7", -3, Up, 3.5).toMiddleLine(),
		example("p39 5 8", -5, Up, 4.5).toMiddleLine(),
		example("p39 5 9", -6, Up, 5).toMiddleLine(),
		example("p39 5 10", 11, Down, 3.5).toMiddleLine(),
		example("p39 5 11", 13, Down, 4.5).toMiddleLine(),
		example("p39 5 12", 14, Down, 5).toMiddleLine(),
		//normal length
		example("p39 6 1", 0, Up, 3.5),
		example("p39 6 2", 1, Up, 3.5),
		example("p39 6 3", 2, Up, 3.5),
		example("p39 6 4", 3, Up, 3.5),
		example("p39 6 5", 4, Down, 3.5),
		example("p39 6 6", 5, Down, 3.5),
		example("p39 6 7", 6, Down, 3.5),
		example("p39 6 8", 7, Down, 3.5)
	);
		
}
