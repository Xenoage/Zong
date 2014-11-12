package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.volta.Volta;
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
	
	private static <T> void testMeasureElements(List<Tuple2<MP, T>> expectedElements, Score score,
		BiFunction<Measure, Fraction, T> getElementAtBeat) {
		for (Tuple2<MP, T> expectedElement : expectedElements) {
			MP mp = expectedElement.get1();
			Measure measure = score.getMeasure(mp);
			T element = getElementAtBeat.apply(measure, mp.beat);
			assertNotNull(""+mp, element);
			assertEquals(""+mp, expectedElement.get2(), element);
		}
	}
	
	private static <T> void testColumnElements(List<Tuple2<MP, T>> expectedElements, Score score,
		BiFunction<ColumnHeader, Fraction, T> getElementAtBeat) {
		for (Tuple2<MP, T> expectedElement : expectedElements) {
			MP mp = expectedElement.get1();
			ColumnHeader columnHeader = score.getColumnHeader(mp.measure);
			T element = getElementAtBeat.apply(columnHeader, mp.beat);
			assertNotNull(""+mp, element);
			assertEquals(""+mp, expectedElement.get2(), element);
		}
	}
	
	public static void assertEqualsStartBarlines(Barline[] expectedStartBarlines, Score score) {
		testColumnElements(expectedStartBarlines, score, column -> column.getStartBarline());
	}
	
	public static void assertEqualsEndBarlines(Barline[] expectedEndBarlines, Score score) {
		testColumnElements(expectedEndBarlines, score, column -> column.getEndBarline());
	}
	
	public static void assertEqualsVoltas(Volta[] expectedVoltas, Score score) {
		testColumnElements(expectedVoltas, score, column -> column.getVolta());
	}
	
	private static <T> void testColumnElements(T[] expectedElements, Score score,
		Function<ColumnHeader, T> getElement) {
		assertEquals(expectedElements.length, score.getMeasuresCount());
		for (int iMeasure : range(expectedElements.length)) {
			T expectedElement = expectedElements[iMeasure];
			T element = getElement.apply(score.getHeader().getColumnHeader(iMeasure));
			if (expectedElement == null)
				assertNull("Measure "+iMeasure, element);
			else
				assertEquals("Measure "+iMeasure, expectedElements[iMeasure], element);
		}
	}

}
