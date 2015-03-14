package material.stem.length;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static material.stem.length.Example.example;

import java.util.List;

import lombok.Getter;
import material.ZongSuite;

/**
 * Stem lengths for all notes in both stem directions.
 * 
 * @author Andreas Wenger
 */
public class ZongStemLength
	implements ZongSuite<Example> {

	@Getter List<Example> examples = alist(
		//all combinations of notes and stem dirs from LP -6 to 14 in a 5 line staff
		example("-6 Up", -6, Up, 5).toMiddleLine(),
		example("-5 Up", -5, Up, 4.5).toMiddleLine(),
		example("-4 Up", -4, Up, 4).toMiddleLine(),
		example("-3 Up", -3, Up, 3.5).toMiddleLine(),
		example("-2 Up", -2, Up, 3.5),
		example("-1 Up", -1, Up, 3.5),
		example("0 Up", 0, Up, 3.5),
		example("1 Up", 1, Up, 3.5),
		example("2 Up", 2, Up, 3.5),
		example("3 Up", 3, Up, 3.5),
		example("4 Up", 4, Up, 3), //Ross p. 86, row 6
		example("5 Up", 5, Up, 2.5),
		example("6 Up", 6, Up, 2.5),
		example("7 Up", 7, Up, 2.5),
		example("8 Up", 8, Up, 2.5),
		example("9 Up", 9, Up, 2.5),
		example("10 Up", 10, Up, 2.5),
		example("11 Up", 11, Up, 2.5),
		example("12 Up", 12, Up, 2.5),
		example("13 Up", 13, Up, 2.5),
		example("14 Up", 14, Up, 2.5),
		example("-6 Down", -6, Down, 2.5),
		example("-5 Down", -5, Down, 2.5),
		example("-4 Down", -4, Down, 2.5),
		example("-3 Down", -3, Down, 2.5),
		example("-2 Down", -2, Down, 2.5),
		example("-1 Down", -1, Down, 2.5),
		example("0 Down", 0, Down, 2.5),
		example("1 Down", 1, Down, 2.5),
		example("2 Down", 2, Down, 2.5),
		example("3 Down", 3, Down, 3), //Ross p. 86, row 6
		example("4 Down", 4, Down, 3.5),
		example("5 Down", 5, Down, 3.5),
		example("6 Down", 6, Down, 3.5),
		example("7 Down", 7, Down, 3.5),
		example("8 Down", 8, Down, 3.5),
		example("9 Down", 9, Down, 3.5),
		example("10 Down", 10, Down, 3.5),
		example("11 Down", 11, Down, 3.5).toMiddleLine(),
		example("12 Down", 12, Down, 4).toMiddleLine(),
		example("13 Down", 13, Down, 4.5).toMiddleLine(),
		example("14 Down", 14, Down, 5).toMiddleLine()
	);
		
}
