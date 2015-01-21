package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
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
		float stemWidthMm = beam.parentStaff.getLineWidth();

		float x1Mm = beam.x1 - stemWidthMm / 2f;
		float x2Mm = beam.x2 + stemWidthMm / 2f;

		Color color = Color.black;

		float leftYStart = 0;
		float rightYStart = 0;

		StaffStamping staff1 = beam.staff1;
		StaffStamping staff2 = beam.staff2;

		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen
			float staff1YPos = staff1.position.y;
			float staff2YPos = staff2.position.y;
			BitmapStaff screenStaff1 = staff1.screenInfo.getBitmapStaff(scaling);
			BitmapStaff screenStaff2 = staff2.screenInfo.getBitmapStaff(scaling);
			leftYStart = staff1YPos + screenStaff1.getLPMm(beam.lp1);
			rightYStart = staff2YPos + screenStaff2.getLPMm(beam.lp2);
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			leftYStart = staff1.computeYMm(beam.lp1);
			rightYStart = staff2.computeYMm(beam.lp2);
		}

		float beamHeightMm = BeamStamping.beamHeight * staff1.is;

		//TODO: avoid edges at the stem end points

		Point2f sw = new Point2f(x1Mm, leftYStart - 0.5f * beamHeightMm);
		Point2f nw = new Point2f(x1Mm, sw.y + beamHeightMm);
		Point2f se = new Point2f(x2Mm, rightYStart - 0.5f * beamHeightMm);
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
