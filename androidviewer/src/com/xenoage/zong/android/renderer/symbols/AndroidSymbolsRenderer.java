package com.xenoage.zong.android.renderer.symbols;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.xenoage.utils.android.color.AndroidColorUtils;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.symbols.SymbolsRenderer;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * Android renderer for {@link Symbol}s.
 * 
 * @author Andreas Wenger
 */
public class AndroidSymbolsRenderer
	extends SymbolsRenderer {

	public static final AndroidSymbolsRenderer instance = new AndroidSymbolsRenderer();


	/**
	 * Draws the given {@link PathSymbol} on the given {@link AndroidGraphicsContext}
	 * with the given color at the given position and the given scaling.
	 */
	@Override public void draw(PathSymbol symbol, com.xenoage.zong.renderer.canvas.Canvas canvas,
		Color color, Point2f position, Point2f scaling) {
		Canvas c = AndroidCanvas.getCanvas(canvas);
		c.save();
		c.translate(position.x, position.y);
		c.scale(scaling.x, scaling.y);

		Paint paint = new Paint();
		paint.setColor(AndroidColorUtils.createColor(color));
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		c.drawPath((Path) symbol.path, paint);
		Log.w("", symbol.boundingRect.toString());
		c.restore();
	}

	/**
	 * Draws the given {@link WarningSymbol} on the given {@link AndroidGraphicsContext}
	 * with the given color at the given position and the given scaling.
	 */
	@Override public void draw(WarningSymbol symbol, com.xenoage.zong.renderer.canvas.Canvas canvas,
		Color color, Point2f position, Point2f scaling) {
		//the warning symbol is not supported yet
	}

}
