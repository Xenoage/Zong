package com.xenoage.utils.android.font;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;

/**
 * Android implementation of a {@link TextMeasurer}.
 * 
 * TODO: I'm not sure yet about the units Android uses
 * for drawing/measuring text. The computations done here
 * were found by experiments and are not optimal, just a first
 * guess.
 * 
 * @author Andreas Wenger
 */
public class AndroidTextMeasurer
	implements TextMeasurer {

	@Override public TextMetrics measure(FontInfo font, String text) {
		Paint paint = new Paint();
		paint.setTypeface(Typeface.SERIF); //currently only serif is supported
		paint.setTextSize(font.getSize());
		return measure(paint, text);
	}

	public static TextMetrics measure(Paint paint, String text) {
		FontMetrics metrics = new FontMetrics();
		paint.getFontMetrics(metrics);
		float ascent = Units.pxToMm(Math.abs(metrics.ascent), 1);
		float descent = Units.pxToMm(metrics.descent, 1);
		float leading = Units.pxToMm(metrics.leading, 1);
		float width = Units.pxToMm(paint.measureText(text), 1);
		return new TextMetrics(ascent, descent, leading, width);
	}

}
