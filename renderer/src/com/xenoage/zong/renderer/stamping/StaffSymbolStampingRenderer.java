package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.symbol.SymbolsRenderer;
import com.xenoage.zong.symbols.Symbol;

/**
 * Renderer for symbol stampings
 * that belong to a staff.
 *
 * @author Andreas Wenger
 */
public class StaffSymbolStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link StaffSymbolStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		StaffSymbolStamping s = (StaffSymbolStamping) stamping;
		drawWith(s.symbol, s.color, s.position, s.scaling, s.parentStaff, s.mirrorV, canvas, args);
	}

	/**
	 * Paints the given symbol using the given information and rendering parameters.
	 */
	public static void drawWith(Symbol symbol, Color color, SP position, float scaling,
		StaffStamping parentStaff, boolean mirrorV, Canvas canvas, RendererArgs args) {
		float viewScaling = args.targetScaling;
		float symbolScaling = scaling * parentStaff.is;

		float yPosition;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			BitmapStaff ss = parentStaff.screenInfo.getBitmapStaff(viewScaling);
			yPosition = parentStaff.position.y + ss.getLPMm(position.lp);
			symbolScaling *= ss.heightScaling;

			/*
			//TEST
			int x = Units.mmToPx(positionX, viewScaling);
			int yOffset = ss.getYOffsetPx();
			int yTopLine = Units.mmToPx(
			  parentStaff.getPosition().y + parentStaff.getLineWidth() / 2,
			  viewScaling) + yOffset;
			for (int i = 0; i < 2; i++)
			  params.renderTarget.drawLine(
			  	new Point2i(x + i, yTopLine),
			  	new Point2i(x + i, yTopLine + ss.getHeightPx()), Color.red, 1); */
		}
		else {
			yPosition = parentStaff.computeYMm(position.lp);
		}
		Point2f correctedPosition = new Point2f(position.xMm, yPosition);

		SymbolsRenderer.draw(symbol, canvas, (color != null ? color : Color.black), correctedPosition,
			new Point2f(symbolScaling, (mirrorV ? -1 : 1) * symbolScaling));
	}

}
