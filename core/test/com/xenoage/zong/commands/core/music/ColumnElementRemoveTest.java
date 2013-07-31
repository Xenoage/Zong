package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.math.Fraction.fr;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;


/**
 * Tests for {@link ColumnElementWrite}.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementRemoveTest {

	@Test public void test() {
		Score score = ScoreFactory.create1Staff4Measures();
		CommandPerformer cmd = score.getCommandPerformer();
		ColumnHeader column2 = score.getColumnHeader(2);
		//write middle barline
		Barline b = Barline.barline(BarlineStyle.LightHeavy);
		cmd.execute(new ColumnElementWrite(b, column2, fr(1, 4), null));
		//remove it
		cmd.execute(new ColumnElementRemove(column2, b));
		assertEquals(0, column2.getMiddleBarlines().size());
		//undo. should be here again
		cmd.undo();
		assertEquals(b, column2.getMiddleBarlines().get(fr(1, 4)));
	}

}
