package com.xenoage.utils.font.android;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;

import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.graphics.font.FontInfo;
import com.xenoage.utils.graphics.font.TextMeasurer;


/**
 * This class provides a method to calculate the width
 * of a given String with a given font.
 * 
 * TODO: I'm not sure yet about the units Android uses
 * for drawing/measuring text. The computations done here
 * were found by experiments and are not optimal, just a first
 * guess.
 * 
 * @author Andreas Wenger
 */
public class AndroidTextMeasurer
	implements TextMeasurer
{
	
	private String text;
	private Paint paint;
	private FontMetrics metrics;
	
	
	/**
	 * Creates a measurer for the given {@link FontInfo}.
	 */
	public AndroidTextMeasurer(FontInfo font, String text)
	{
		init(font, text);
	}
	
	
	@Override public void init(FontInfo font, String text)
	{
		this.text = text;
		this.paint = new Paint();
		this.paint.setTypeface(Typeface.SERIF); //currently only serif is supported
		this.paint.setTextSize(font.getSize());
		metrics = new FontMetrics();
		paint.getFontMetrics(metrics);
	}
	
	
	/**
	 * Gets the ascent of this font in mm.
	 */
	@Override public float getAscent()
	{
		return Units.pxToMm(Math.abs(metrics.ascent), 1);
	}
	
	
	/**
	 * Gets the descent of this font in mm.
	 */
	@Override public float getDescent()
	{
		return Units.pxToMm(metrics.descent, 1);
	}
	
	
	/**
	 * Gets the leading of this font in mm.
	 */
	@Override public float getLeading()
	{
		return Units.pxToMm(metrics.leading, 1);
	}
	
	
	/**
	 * Measure the width of this text in mm.
	 */
	@Override public float getWidth()
	{
		return Units.pxToMm(paint.measureText(text), 1);
	}

	
}
