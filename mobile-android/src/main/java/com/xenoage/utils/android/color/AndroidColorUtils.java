package com.xenoage.utils.android.color;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.xenoage.utils.annotations.Optimized;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import static com.xenoage.utils.color.Color.color;

/**
 * Useful methods for working with Android colors.
 * 
 * @author Andreas Wenger
 */
public class AndroidColorUtils {

	public static final Paint black = createPaintFill(com.xenoage.utils.color.Color.black);


	/**
	 * Creates a new {@link com.xenoage.utils.color.Color}.
	 * @param color      the Android color
	 */
	public static com.xenoage.utils.color.Color createColorInfo(int color) {
		return color(Color.red(color), Color.green(color),
				Color.blue(color), Color.alpha(color));
	}

	/**
	 * Gets the Android color from the given {@link com.xenoage.utils.color.Color}.
	 */
	public static int createColor(com.xenoage.utils.color.Color colorInfo) {
		return Color.argb(colorInfo.a, colorInfo.r, colorInfo.g, colorInfo.b);
	}

	/**
	 * Gets the Android {@link Paint} for fillings from the given {@link com.xenoage.utils.color.Color}.
	 */
	@Optimized(Performance)
	public static Paint createPaintFill(com.xenoage.utils.color.Color colorInfo) {
		//optimized: black is so frequently used, that we share a single instance
		if (colorInfo == com.xenoage.utils.color.Color.black)
			return black;

		Paint paint = new Paint();
		paint.setColor(createColor(colorInfo));
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		return paint;
	}

	/**
	 * Gets the Android {@link Paint} for colorizing from the given {@link com.xenoage.utils.color.Color}.
	 */
	public static Paint createPaintColorize(com.xenoage.utils.color.Color colorInfo) {
		Paint paint = new Paint();
		float r = colorInfo.r / 255f;
		float g = colorInfo.g / 255f;
		float b = colorInfo.b / 255f;
		float a = colorInfo.a / 255f;
		ColorMatrix cm = new ColorMatrix(new float[] { r, 0, 0, 0, 0, 0, g, 0, 0, 0, 0, 0, b, 0, 0, 0,
			0, 0, a, 0 });
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		paint.setAntiAlias(true);
		return paint;
	}

}
