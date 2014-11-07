package musicxmltestsuite.tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.function.BiFunction;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.position.MP;



public class ScoreTest {
	
	public static void testClefs(List<Tuple2<MP, Clef>> expectedClefs, Score score) {
		testMeasureElements(expectedClefs, score, (measure, beat) -> measure.getClefs().get(beat));
	}
	
	public static void testDirections(List<Tuple2<MP, Direction>> expectedDirections, Score score) {
		testMeasureElements(expectedDirections, score, (measure, beat) -> measure.getDirections().get(beat));
	}
	
	public static void testKeys(List<Tuple2<MP, Key>> expectedKeys, Score score) {
		testColumnElements(expectedKeys, score, (column, beat) -> column.getKeys().get(beat));
	}
	
	public static <T> void testMeasureElements(List<Tuple2<MP, T>> expectedElements, Score score,
		BiFunction<Measure, Fraction, T> getElementAtBeat) {
		for (Tuple2<MP, T> expectedElement : expectedElements) {
			MP mp = expectedElement.get1();
			Measure measure = score.getMeasure(mp);
			T element = getElementAtBeat.apply(measure, mp.beat);
			assertNotNull(""+mp, element);
			assertEquals(""+mp, expectedElement.get2(), element);
		}
	}
	
	public static <T> void testColumnElements(List<Tuple2<MP, T>> expectedElements, Score score,
		BiFunction<ColumnHeader, Fraction, T> getElementAtBeat) {
		for (Tuple2<MP, T> expectedElement : expectedElements) {
			MP mp = expectedElement.get1();
			ColumnHeader columnHeader = score.getColumnHeader(mp.measure);
			T element = getElementAtBeat.apply(columnHeader, mp.beat);
			assertNotNull(""+mp, element);
			assertEquals(""+mp, expectedElement.get2(), element);
		}
	}

}
