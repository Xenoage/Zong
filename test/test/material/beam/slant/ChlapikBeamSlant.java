package material.beam.slant;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.beam.slant.Example.example;

import java.util.List;


/**
 * Examples from Ross, Chapter 3, Section 5
 * (p. 98 ff.)
 * 
 * @author Andreas Wenger
 */
public class ChlapikBeamSlant {
	
	public static final List<Example> examples = alist(
		//beames groups
		example("p40 8 r1 1").left(0, 5).middleNotes(2, 3).right(1, 4.5),
		example("p40 8 r1 2").left(4, 3.25).middleNotes(7, 8).right(7, 3.75),
		example("p40 8 r1 3").left(7, 5.5).middleNotes(2, 3).right(9, 6.5),
		example("p40 8 r1 4").left(8, 3).middleNotes(-1, 4).right(6, 3.25).stemDir(Up),
		example("p40 8 r1 5").left(7, 5.5).middleNotes(1, 4).right(7, 5.5),
		example("p40 8 r1 6").left(9, 7.5).middleNotes(-1, 5).right(6, 6),
		//beam position
		example("p41 3 r1 1").left(6, 3.25).middleNotes(6, 6).right(6, 3.25),
		example("p41 3 r1 2").left(7, 3.5).middleNotes(7, 7).right(7, 3.5),
		example("p41 3 r1 3").left(1, 3.5).middleNotes(1, 1).right(1, 3.5),
		//p. 42, r1: we do not use the 30° rule, but instead Ross p. 111 (max 2 spaces)
		example("p42 3 r3 b1").left(9, 4.5).middleNotes(8, 7, 9, 8).right(7, 3.5),
		example("p42 3 r3 b2").left(0, 5).middleNotes(3, 1, 3, 2).right(0, 5),
		//TODO: more in Ross, but Chlapik looks better: example("p42 3 r4 1").left(0, 4).middleNotes(1, 2, 1, 2).right(3, 2.75),
		example("p42 3 r4 2").left(8, 4).middleNotes(7, 6, 7, 6).right(5, 3),
		example("p42 3 r4 3").left(5, 3).middleNotes(6).right(7, 3.5),
		example("p42 3 r4 4").left(-1, 3.5).middleNotes(0).right(1, 3),
		example("p42 3 r4 5", 9, 3.5, 7, 3),
		example("p42 3 r4 6", 5, 2.75, 6, 3),
		example("p42 3 r4 7", -1, 3.5, 1, 3)
	);

}
