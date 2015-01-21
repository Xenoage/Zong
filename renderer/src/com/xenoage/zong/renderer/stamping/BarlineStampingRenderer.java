package com.xenoage.zong.renderer.stamping;

import static com.xenoage.zong.core.music.format.SP.sp;

import java.util.List;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.music.barline.BarlineRepeat;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.musiclayout.stampings.BarlineStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Renderer for a {@link BarlineStamping}.
 *
 * @author Andreas Wenger
 */
public class BarlineStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link BarlineStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		BarlineStamping barlineSt = (BarlineStamping) stamping;

		List<StaffStamping> staves = barlineSt.staves;
		float xPosition = barlineSt.xPosition;
		float xCorrection = 0;

		//lines
		BarlineGroup.Style group = barlineSt.groupStyle;
		BarlineStyle style = barlineSt.barline.getStyle();
		if (group == null || group == BarlineGroup.Style.Single || group == BarlineGroup.Style.Common) {
			//draw single barlines
			for (StaffStamping staff : staves) {
				xCorrection = paintBarline(canvas, args, staff, (staff.linesCount - 1) * 2, staff, 0,
					xPosition, style);
			}
		}
		if (group == BarlineGroup.Style.Mensurstrich || group == BarlineGroup.Style.Common) {
			//draw barlines between staves
			for (int i = 0; i < staves.size() - 1; i++) {
				xCorrection = paintBarline(canvas, args, staves.get(i), 0, staves.get(i + 1),
					(staves.get(i + 1).linesCount - 1) * 2, xPosition, style);
			}
		}

		//repeat dots
		//TODO: xCorrection is the value of the last staff, but this may differ
		//draw repeat dots directly after drawing the corresponding staff!
		BarlineRepeat repeat = barlineSt.barline.getRepeat();
		if (repeat == BarlineRepeat.Forward || repeat == BarlineRepeat.Both) {
			paintRepeatDots(staves, xPosition + xCorrection, 1, canvas, args);
		}
		if (repeat == BarlineRepeat.Backward || repeat == BarlineRepeat.Both) {
			paintRepeatDots(staves, xPosition + xCorrection, -1, canvas, args);
		}

	}

	/**
	 * Draws repeat dots at the given side (-1 or 1) at the given
	 * horizontal position on the given staves.
	 */
	private static void paintRepeatDots(List<StaffStamping> staves, float xPosition, int side,
		Canvas canvas, RendererArgs args) {
		for (StaffStamping staff : staves) {
			int lp1 = (staff.linesCount + 1) / 2;
			int lp2 = lp1 + 2;
			float x = xPosition + staff.is * 1.2f * side;
			paintRepeatDot(canvas, args, staff, sp(x, lp1));
			paintRepeatDot(canvas, args, staff, sp(x, lp2));
		}
	}

	/**
	 * Draws a barline with the given style between the given line of the given
	 * staff and the other given line of the other
	 * given staff, using the given rendering parameters.
	 * The horizontal position correction in px is returned.
	 * 
	 * TIDY TIDY TIDY
	 */
	private static float paintBarline(Canvas canvas, RendererArgs args, StaffStamping staff1,
		float staff1LP, StaffStamping staff2, float staff2LP, float xPosition, BarlineStyle style) {
		Color col = Color.black;
		float lightMm = staff1.getLineWidth() * 1.5f; //barline a little bit thicker than staff line
		float heavyMm = lightMm * 3f;
		float gapMm = lightMm * 1.5f;
		float l = lightMm;
		float h = heavyMm;
		float g = gapMm;
		Color lightColor = col, heavyColor = col;
		/* UNNEEDED... looks good anyway
		//correction for bitmap display
		if (canvas.getFormat() == CanvasFormat.Bitmap)
		{
			//screen
			//light
			BitmapLine screenLine = new BitmapLine(lightMm, col, scaling);
			l = screenLine.widthMm;
			lightColor = screenLine.color;
			//heavy
			screenLine = new BitmapLine(heavyMm, col, scaling);
			h = screenLine.widthMm;
			heavyColor = screenLine.color;
			//gap (for a better look, use bigger gap at low zoom. tried out.)
			float gapPxFloat = Units.mmToPx(gapMm, scaling);
			if (gapPxFloat >= 2)
				g = gapMm;
			else if (gapPxFloat >= 1f)
				g = Units.pxToMm(2, scaling);
			else if (gapPxFloat >= 0.2f)
				g = Units.pxToMm(1, scaling);
			else
				g = 0;
		} */
		//if on the very left or very right side of the staff, don't center
		//the barline but place it completely within the staff
		boolean isLeft = (xPosition <= staff1.position.x + lightMm);
		boolean isRight = (xPosition >= staff1.position.x + staff1.length - lightMm);
		float x = xPosition;
		float c = 0;
		//half of light/heavy/gap
		float l2 = l / 2;
		float h2 = h / 2;
		float g2 = g / 2;
		//TEST
		//paintLine(params, staff1, staff1LP + 4, staff2, staff1LP + 1, x - c, 1, Color.blue);
		//paintLine(params, staff1, staff2LP - 1, staff2, staff2LP - 4, x - c, 1, Color.blue);
		//TEST
		//style = BarlineStyle.HeavyHeavy;
		//draw lines dependent on style
		//all correction values have been found by trying out
		//TODO: Dashed, Dotted
		switch (style) {
			case Regular:
				if (isLeft) {
					c = l2;
					x += c;
				}
				else if (isRight) {
					c = -l2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x, l, lightColor);
				break;
			case Heavy:
				if (isLeft) {
					c = h2;
					x += c;
				}
				else if (isRight) {
					c = -h2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x, h, heavyColor);
				break;
			case LightLight:
				if (isLeft) {
					c = g2 + 2 * l2;
					x += c;
				}
				else if (isRight) {
					c = -l2 - g2 - l2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x - g2 - l2, l, lightColor);
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x + g2 + l2, l, lightColor);
				break;
			case LightHeavy: //heavy is centered (if barline not at the border of the staff)
				if (isLeft) {
					c = 2 * l2 + g + h2;
					x += c;
				}
				else if (isRight) {
					c = -h2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x - h2 - g - l2, l, lightColor);
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x, h, heavyColor);
				break;
			case HeavyLight: //heavy is centered (if barline not at the border of the staff)
				if (isLeft) {
					c = h2;
					x += c;
				}
				else if (isRight) {
					c = -l2 - h2 - g - l2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x, h, heavyColor);
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x + h2 + g + l2, l, lightColor);
				break;
			case HeavyHeavy:
				if (isLeft) {
					c = g2 + 2 * h2;
					x += c;
				}
				else if (isRight) {
					c = -h2 - g2 - h2;
					x += c;
				}
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x - g2 - h2, h, heavyColor);
				paintLine(canvas, args, staff1, staff1LP, staff2, staff2LP, x + g2 + h2, h, heavyColor);
				break;
			case None:
			case Dashed:
			case Dotted:
				//TODO
				break;
		}
		return c;
	}

	/**
	 * Draws the given line with the given width in px.
	 */
	private static void paintLine(Canvas canvas, RendererArgs args, StaffStamping staff1,
		float staff1LinePosition, StaffStamping staff2, float staff2LinePosition, float xMm,
		float widthMm, Color color) {
		if (canvas.getFormat() == CanvasFormat.Raster) {
			float scaling = args.targetScaling;
			BitmapStaff screenStaff1 = staff1.screenInfo.getBitmapStaff(scaling);
			BitmapStaff screenStaff2 = staff2.screenInfo.getBitmapStaff(scaling);
			Point2f p1 = new Point2f(xMm, staff1.position.y + screenStaff1.getLPMm(staff1LinePosition));
			Point2f p2 = new Point2f(xMm, staff2.position.y + screenStaff2.getLPMm(staff2LinePosition));
			canvas.drawLine(p1, p2, color, widthMm);
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			Point2f p1 = new Point2f(xMm, staff1.position.y + staff1.is *
				(staff1.linesCount - 1 - staff1LinePosition / 2));
			Point2f p2 = new Point2f(xMm, staff2.position.y + staff2.is *
				(staff2.linesCount - 1 - staff2LinePosition / 2));
			canvas.drawLine(p1, p2, color, widthMm);
		}
	}

	/**
	 * Paints a repeat dot at the given position.
	 */
	private static void paintRepeatDot(Canvas canvas, RendererArgs args, StaffStamping staff,
		SP position) {
		Symbol dotSymbol = args.symbolPool.getSymbol(CommonSymbol.NoteDot);
		StaffSymbolStampingRenderer.drawWith(dotSymbol, Color.black, position, 1, staff, false,
			canvas, args);
	}

}
