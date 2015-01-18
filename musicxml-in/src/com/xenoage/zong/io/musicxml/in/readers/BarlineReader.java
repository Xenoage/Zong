package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.zong.core.music.MeasureSide.Left;
import static com.xenoage.zong.core.music.MeasureSide.Right;
import static com.xenoage.zong.core.music.barline.Barline.barline;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.musicxml.Equivalents;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlEnding;
import com.xenoage.zong.musicxml.types.attributes.MxlRepeat;
import com.xenoage.zong.musicxml.types.enums.MxlBackwardForward;
import com.xenoage.zong.musicxml.types.enums.MxlRightLeftMiddle;

/**
 * Reads a {@link Barline}, including its {@link Volta},
 * from a {@link MxlBarline}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class BarlineReader {
	
	private final MxlBarline mxlBarline;
	
	private Barline barline;
	private MeasureSide side;
	
	
	public void readToContext(Context context) {
		read();
		//write barline
		if (barline != null)
			context.writeColumnElement(barline, side);
		//write volta
		MxlEnding mxlEnding = mxlBarline.getEnding();
		if (mxlEnding != null)
			new VoltaReader(mxlEnding).readToContext(context);
	}

	private void read() {
		MxlRightLeftMiddle location = mxlBarline.getLocation();
		MxlRepeat repeat = mxlBarline.getRepeat();
		BarlineStyle style = null;
		if (mxlBarline.getBarStyle() != null)
			style = Equivalents.barlineStyles.get1(mxlBarline.getBarStyle().getBarStyle());
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
			if (location == MxlRightLeftMiddle.Left)
				side = Left;
			else if (location == MxlRightLeftMiddle.Right)
				side = Right;
		}
	}
	
}
