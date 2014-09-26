package com.xenoage.zong.webapp.renderer.symbols;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.gwt.color.GwtColorUtils;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.symbols.SymbolsRenderer;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.WarningSymbol;
import com.xenoage.zong.webapp.renderer.canvas.GwtCanvas;
import com.xenoage.zong.webapp.symbols.GwtPathSymbol;

/**
 * GWT implementation of a {@link SymbolsRenderer}.
 * 
 * @author Andreas Wenger
 */
public class GwtSymbolsRenderer
	extends SymbolsRenderer {

	public static final GwtSymbolsRenderer instance = new GwtSymbolsRenderer();


	@Override public void draw(PathSymbol symbol, Canvas canvas, Color color, Point2f position,
		Point2f scaling) {
		Context2d context = GwtCanvas.getCanvas(canvas).getContext2d();
		context.save();
		
		context.translate(position.x, position.y);
		context.scale(scaling.x, scaling.y);
		context.setFillStyle(GwtColorUtils.createColor(color));
		
		((GwtPathSymbol) symbol.path).draw(context);
		context.fill();

		context.restore();
	}

	@Override public void draw(WarningSymbol symbol, Canvas canvas, Color color,
		Point2f position, Point2f scaling) {
		//TODO: paint warning symbol
	}

}
