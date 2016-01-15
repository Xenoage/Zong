package material.stem.length;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.stem.length.Example.example;

import java.util.List;

import lombok.Getter;
import material.RossSuite;

/**
 * Examples from Ross, Chapter 3, Section 1
 * (p. 86 ff.)
 * 
 * @author Andreas Wenger
 */
public class RossStemLength
	implements RossSuite<Example> {

	@Getter List<Example> examples = alist(
		//normal length
		example("p86 r1 1", 0, Up, 3.5),
		example("p86 r1 2", 4, Down, 3.5),
		example("p86 r2 1", 9, Down, 3.5),
		//shortened outside of staff
		example("p86 r3 1", 5, Up, 2.5),
		example("p86 r3 2", 6, Up, 2.5),
		example("p86 r3 3", 7, Up, 2.5),
		example("p86 r3 4", 8, Up, 2.5),
		example("p86 r3 5", 9, Up, 2.5),
		example("p86 r3 6", 2, Down, 2.5),
		example("p86 r3 7", 1, Down, 2.5),
		example("p86 r3 8", 0, Down, 2.5),
		example("p86 r3 9", -1, Down, 2.5),
		example("p86 r3 10", -2, Down, 2.5),
		//special cases
		example("p86 r6 1", 4, Up, 3),
		example("p86 r6 1", 3, Down, 3),
		//lengthened to middle line
		example("p86 r7 1", 12, Down, 4).toMiddleLine(),
		example("p86 r7 2", 14, Down, 5).toMiddleLine(),
		example("p86 r7 3", 15, Down, 5.5).toMiddleLine(),
		example("p86 r7 4", 17, Down, 6.5).toMiddleLine(),
		example("p86 r7 5", -4, Up, 4).toMiddleLine(),
		example("p86 r7 5", -6, Up, 5).toMiddleLine(),
		example("p86 r7 5", -7, Up, 5.5).toMiddleLine(),
		example("p86 r7 5", -9, Up, 6.5).toMiddleLine()
	);
		
}
