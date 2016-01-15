package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.symbols.path.ClosePath;
import com.xenoage.zong.symbols.path.LineTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.Path;
import com.xenoage.zong.symbols.path.PathElement;

/**
 * Renderer for a {@link BeamStamping}.
 *
 * @author Andreas Wenger
 */
public class BeamStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link BeamStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		BeamStamping beam = (BeamStamping) stamping;
		float scaling = args.targetScaling;

		//TODO: stem should be thinner than lineWidth?
		float stemWidthMm = beam.staff1.getLineWidthMm();

		StaffStamping staff1 = beam.staff1;
		StaffStamping staff2 = beam.staff2;
		
		float x1Mm = staff1.positionMm.x + beam.sp1.xMm - stemWidthMm / 2f;
		float x2Mm = staff2.positionMm.x + beam.sp2.xMm + stemWidthMm / 2f;

		Color color = Color.black;

		float leftYStart, rightYStart, beamHeightMm;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen
			float staff1YPos = staff1.positionMm.y;
			float staff2YPos = staff2.positionMm.y;
			BitmapStaff screenStaff1 = staff1.getBitmapInfo().getBitmapStaff(scaling);
			BitmapStaff screenStaff2 = staff2.getBitmapInfo().getBitmapStaff(scaling);
			leftYStart = staff1YPos + screenStaff1.getYMm(beam.sp1.lp);
			rightYStart = staff2YPos + screenStaff2.getYMm(beam.sp2.lp);
			beamHeightMm = BeamNotation.lineHeightIs * screenStaff1.interlineSpaceMm;
		}
		else {
			leftYStart = staff1.computeYMm(beam.sp1.lp);
			rightYStart = staff2.computeYMm(beam.sp1.lp);
			beamHeightMm = BeamNotation.lineHeightIs * staff1.is;
		}

		//TODO: avoid edges at the stem end points
		
		//beam sits on or hangs from the vertical position, dependent on stem direction
		float vAdd = (beam.stemDir.getSign() - 1) / 2f * beamHeightMm;

		Point2f sw = new Point2f(x1Mm, leftYStart + vAdd);
		Point2f nw = new Point2f(x1Mm, sw.y + beamHeightMm);
		Point2f se = new Point2f(x2Mm, rightYStart + vAdd);
		Point2f ne = new Point2f(x2Mm, se.y + beamHeightMm);
		
		List<PathElement> elements = alist(4);
		elements.add(new MoveTo(sw));
		elements.add(new LineTo(nw));
		elements.add(new LineTo(ne));
		elements.add(new LineTo(se));
		elements.add(new ClosePath());
		Path path = new Path(elements);

		canvas.fillPath(path, color);
	}

}
