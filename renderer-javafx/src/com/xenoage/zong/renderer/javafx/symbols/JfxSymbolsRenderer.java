package com.xenoage.zong.renderer.javafx.symbols;

import javafx.scene.canvas.GraphicsContext;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.jse.javafx.color.JfxColorUtils;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.javafx.canvas.JfxCanvas;
import com.xenoage.zong.renderer.symbols.SymbolsRenderer;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * JavaFX implementation of a {@link SymbolsRenderer}.
 * 
 * @author Andreas Wenger
 */
public class JfxSymbolsRenderer
	extends SymbolsRenderer {

	public static final JfxSymbolsRenderer instance = new JfxSymbolsRenderer();


	@Override public void draw(PathSymbol symbol, Canvas canvas, Color color, Point2f position,
		Point2f scaling) {
		GraphicsContext context = JfxCanvas.getGraphicsContext(canvas);
		context.save();
		context.translate(position.x, position.y);
		context.scale(scaling.x, scaling.y);
		
		context.setFill(JfxColorUtils.toJavaFXColor(color));
		JfxPath.drawPath(symbol.getPath(), context);
		context.fill();

		context.restore();
	}

	@Override public void draw(WarningSymbol symbol, Canvas canvas, Color color,
		Point2f position, Point2f scaling) {
		//the warning symbol is not visible on the rendering of the result
		if (canvas.getDecoration() == CanvasDecoration.Interactive)
			return;
		//TODO: paint warning symbol
		//...
	}

}
