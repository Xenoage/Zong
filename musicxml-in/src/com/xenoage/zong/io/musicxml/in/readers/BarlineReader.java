package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.MeasureSide.Left;
import static com.xenoage.zong.core.music.MeasureSide.Right;
import static com.xenoage.zong.core.music.barline.Barline.barline;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;

import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlEnding;
import com.xenoage.zong.musicxml.types.attributes.MxlRepeat;
import com.xenoage.zong.musicxml.types.enums.MxlBackwardForward;
import com.xenoage.zong.musicxml.types.enums.MxlRightLeftMiddle;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopDiscontinue;


public class BarlineReader {

	/**
	 * Reads the given barline element.
	 */
	public static void readBarline(MusicReaderContext context, MxlBarline mxlBarline) {
		MxlRightLeftMiddle location = mxlBarline.getLocation();
		MxlRepeat repeat = mxlBarline.getRepeat();
		int measureIndex = context.getMp().measure;
		BarlineStyle style = null;
		if (mxlBarline.getBarStyle() != null)
			style = BarlineStyleReader.read(mxlBarline.getBarStyle().getBarStyle());
		Barline barline = null;
		if (repeat != null) {
			//repeat barline
			if (repeat.getDirection() == MxlBackwardForward.Forward) {
				style = notNull(style, BarlineStyle.HeavyLight);
				barline = barlineForwardRepeat(style);
			}
			else if (repeat.getDirection() == MxlBackwardForward.Backward) {
				style = notNull(style, BarlineStyle.LightHeavy);
				int times = notNull(repeat.getTimes(), 1).intValue();
				barline = barlineBackwardRepeat(style, times);
			}
		}
		else if (style != null) {
			//regular barline
			barline = barline(style);
		}
		if (barline != null) {
			//side / beat
			MeasureSide side = null;
			Fraction beat = null;
			if (location == MxlRightLeftMiddle.Left)
				side = Left;
			else if (location == MxlRightLeftMiddle.Right)
				side = Right;
			else if (location == MxlRightLeftMiddle.Middle)
				beat = context.getMp().beat;
			//write barline
			if (barline != null) {
				new ColumnElementWrite(barline,
					context.getScore().getColumnHeader(measureIndex), beat, side).execute();
			}
		}
		if (mxlBarline.getEnding() != null) {
			readEnding(context, mxlBarline.getEnding());
		}
	}
	
	private static void readEnding(MusicReaderContext context, MxlEnding mxlEnding) {
		MxlStartStopDiscontinue type = mxlEnding.getType();
		if (type == MxlStartStopDiscontinue.Start) {
			Range range = readEndingRange(mxlEnding.getNumber());
			context.openVolta(range, null);
		}
		else if (type == MxlStartStopDiscontinue.Stop || type == MxlStartStopDiscontinue.Discontinue) {
			boolean rightHook = (type == MxlStartStopDiscontinue.Stop);
			Tuple2<Volta, Integer> volta = context.closeVolta(rightHook);
			if (volta != null)
				context.writeColumnElement(volta.get2(), volta.get1());
		}
	}
	
	private static Range readEndingRange(String number) {
		number = number.trim();
		if (number.length() == 0)
			return null;
		//we allow only consecutive endings, so find minimum and maximum value
		String[] numbers = number.split(",");
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (String n : numbers) {
			int i = Integer.parseInt(n.trim());
			min = Math.min(min, i);
			max = Math.max(max, i);
		}
		return range(min, max);
	}
	
}
