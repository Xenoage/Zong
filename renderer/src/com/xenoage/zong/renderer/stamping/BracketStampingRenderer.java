package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.math.geom.Point2f.p;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.musiclayout.stampings.BracketStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.symbol.SymbolsRenderer;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Renderer for a bracket stamping.
 * 
 * At the moment braces and square brackets
 * are supported.
 *
 * @author Andreas Wenger
 */
public class BracketStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link BracketStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		BracketStamping bracket = (BracketStamping) stamping;

		BracketGroup.Style style = bracket.groupStyle;
		if (style == null || style == BracketGroup.Style.Brace) {
			paintBrace(bracket, canvas, args);
		}
		else if (style == BracketGroup.Style.Bracket) {
			paintBracket(bracket, canvas, args);
		}
	}

	/**
	 * Draws a brace, using the given bracket stamping and rendering parameters.
	 */
	private static void paintBrace(BracketStamping bracket, Canvas canvas, RendererArgs args) {
		float interlineSpace = bracket.firstStaff.is;

		float y1 = getStaffTopY(bracket.firstStaff, canvas, args);
		float y2 = getStaffBottomY(bracket.lastStaff, canvas, args);
		Point2f pCenter = new Point2f(bracket.positionX, (y1 + y2) / 2);

		Symbol braceSymbol = args.symbolPool.getSymbol(CommonSymbol.BracketBrace);
		float symbolScaling = (y2 - y1) / braceSymbol.boundingRect.size.height;
		
		SymbolsRenderer.draw(braceSymbol, canvas, Color.black, pCenter,
			new Point2f(interlineSpace * 1.2f, symbolScaling));
	}

	/**
	 * Draws a bracket, using the given bracket stamping and rendering parameters.
	 */
	private static void paintBracket(BracketStamping bracket, Canvas canvas, RendererArgs args) {
		float interlineSpace = bracket.firstStaff.is;

		float y1 = getStaffTopY(bracket.firstStaff, canvas, args);
		float y2 = getStaffBottomY(bracket.lastStaff, canvas, args);
		Point2f p1Mm = new Point2f(bracket.positionX, y1);

		Symbol bracketLineSymbol = args.symbolPool.getSymbol(CommonSymbol.BracketBracketLine);
		Symbol bracketEndSymbol = args.symbolPool.getSymbol(CommonSymbol.BracketBracketEnd);

		float lineYCorrection = bracketEndSymbol.boundingRect.size.height / 2; //to avoid gaps
		float lineYScaling = (y2 - y1 + lineYCorrection) / bracketLineSymbol.boundingRect.size.height;

		SymbolsRenderer.draw(bracketLineSymbol, canvas, Color.black, p(p1Mm.x, p1Mm.y - lineYCorrection / 2),
			new Point2f(interlineSpace, lineYScaling));
		SymbolsRenderer.draw(bracketEndSymbol, canvas, Color.black, p1Mm, new Point2f(interlineSpace,
			-interlineSpace));
		p1Mm = new Point2f(p1Mm.x, y2);
		SymbolsRenderer.draw(bracketEndSymbol, canvas, Color.black, p1Mm, new Point2f(interlineSpace,
			interlineSpace));

	}

	/**
	 * Gets the vertical position in px of the topmost line
	 * of the given staff.
	 * TIDY: move into other class?
	 */
	private static float getStaffTopY(StaffStamping staff, Canvas canvas, RendererArgs args) {
		float scaling = args.targetScaling;
		float ret = staff.position.y;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen
			BitmapStaff screenStaff = staff.screenInfo.getBitmapStaff(scaling);
			ret += screenStaff.yOffsetMm;
		}
		return ret;
	}

	/**
	 * Gets the vertical position in px of the lowest line
	 * of the given staff.
	 * TIDY: move into other class?
	 */
	private static float getStaffBottomY(StaffStamping staff, Canvas canvas, RendererArgs args) {
		float scaling = args.targetScaling;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen
			BitmapStaff screenStaff = staff.screenInfo.getBitmapStaff(scaling);
			return staff.position.y + screenStaff.lp0Mm;
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			//render with high quality
			return staff.position.y + staff.is * (staff.linesCount - 1) + staff.getLineWidth();
		}
		return 0;
	}

}
