package com.xenoage.zong.android.renderer.symbols;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.xenoage.utils.android.color.AndroidColorUtils;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.android.renderer.path.AndroidPath;
import com.xenoage.zong.renderer.symbol.SymbolsRenderer;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * Android renderer for {@link Symbol}s.
 *
 * TODO: needed? Code is trivial, integrate it into {@link AndroidCanvas}
 * 
 * @author Andreas Wenger
 */
public class AndroidSymbolsRenderer {

	public static final AndroidSymbolsRenderer androidSymbolsRenderer =
			new AndroidSymbolsRenderer();


	/**
	 * Draws the given {@link PathSymbol} on the given {@link Canvas}
	 * with the given color at the given position and the given scaling.
	 */
	public void draw(PathSymbol symbol, Canvas canvas,
		Color color, Point2f position, Point2f scaling) {

		canvas.save();
		canvas.translate(position.x, position.y);
		canvas.scale(scaling.x, scaling.y);

		Paint paint = AndroidColorUtils.createPaintFill(color);
		Path path = AndroidPath.createPath(symbol.path);
		canvas.drawPath(path, paint);

		canvas.restore();
	}

	/**
	 * Draws the given {@link WarningSymbol} on the given {@link Canvas}
	 * with the given color at the given position and the given scaling.
	 */
	public void draw(WarningSymbol symbol, Canvas canvas,
		Color color, Point2f position, Point2f scaling) {
		//the warning symbol is not supported yet
	}

}
