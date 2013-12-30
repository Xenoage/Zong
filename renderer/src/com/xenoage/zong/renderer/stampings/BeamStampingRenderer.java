package com.xenoage.zong.renderer.stampings;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;


/**
 * Renderer for a {@link BeamStamping}.
 *
 * @author Andreas Wenger
 */
public class BeamStampingRenderer
	extends StampingRenderer
{


	/**
	 * Draws the given {@link BeamStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args)
	{
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
		} else if (canvas.getFormat() == CanvasFormat.Vector) {
			leftYStart = staff1.computeYMm(beam.lp1);
			rightYStart = staff2.computeYMm(beam.lp2);
		}

		float beamHeightMm = BeamStamping.beamHeight * staff1.is;

		//TODO: avoid edges at the stem end points

		//left lower point
		Point2f[] points = new Point2f[4];
		points[0] = new Point2f(x1Mm, leftYStart - 0.5f * beamHeightMm);
		//left upper point
		points[1] = new Point2f(x1Mm, points[0].y + beamHeightMm);
		//right lower point
		points[2] = new Point2f(x2Mm, rightYStart - 0.5f * beamHeightMm);
		//right upper point
		points[3] = new Point2f(x2Mm, points[2].y + beamHeightMm);

		canvas.drawBeam(points, /* Color.green /*/color /**/, staff1.is);

	}


}
