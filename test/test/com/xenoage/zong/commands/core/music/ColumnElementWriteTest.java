package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;


/**
 * Tests for {@link ColumnElementWrite}.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementWriteTest {

	@Test public void test() {
		Score score = ScoreFactory.create1Staff4Measures();
		CommandPerformer cmd = score.getCommandPerformer();
		ColumnHeader column2 = score.getColumnHeader(2);
		//write start barline, middle barline and end barline
		Barline b1 = Barline.barlineRegular();
		Barline b2 = Barline.barlineRegular();
		Barline b3 = Barline.barline(BarlineStyle.LightHeavy);
		cmd.execute(new ColumnElementWrite(b1, column2, null, MeasureSide.Left));
		cmd.execute(new ColumnElementWrite(b2, column2, Companion.fr(1, 4), null));
		cmd.execute(new ColumnElementWrite(b3, column2, null, MeasureSide.Right));
		assertEquals(b1, column2.getStartBarline());
		assertEquals(b2, column2.getMiddleBarlines().get(Companion.fr(1, 4)));
		assertEquals(b3, column2.getEndBarline());
		//overwrite middle barline
		Barline b4 = Barline.barlineRegular();
		cmd.execute(new ColumnElementWrite(b4, column2, Companion.fr(1, 4), null));
		assertEquals(b4, column2.getMiddleBarlines().get(Companion.fr(1, 4)));
		//undo. b2 should be here again
		cmd.undo();
		assertEquals(b2, column2.getMiddleBarlines().get(Companion.fr(1, 4)));
		//undo all steps. the middle barline should not exist any more
		cmd.undoMultipleSteps(3);
		assertEquals(0, column2.getMiddleBarlines().size());
	}

}
