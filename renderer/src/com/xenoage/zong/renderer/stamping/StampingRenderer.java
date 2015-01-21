package com.xenoage.zong.renderer.stamping;

import java.util.HashMap;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StampingType;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for all kinds of {@link Stamping}s.
 * Subclasses implement the specific stamping algorithms.
 * 
 * @author Andreas Wenger
 */
public abstract class StampingRenderer {

	private static HashMap<StampingType, StampingRenderer> renderers;
	private static boolean init = false;


	private static void init() {
		init = true;
		renderers = new HashMap<StampingType, StampingRenderer>();
		renderers.put(StampingType.BarlineStamping, new BarlineStampingRenderer());
		renderers.put(StampingType.BeamStamping, new BeamStampingRenderer());
		renderers.put(StampingType.BracketStamping, new BracketStampingRenderer());
		renderers.put(StampingType.SlurStamping, new SlurStampingRenderer());
		renderers.put(StampingType.EmptySpaceStamping, null);
		renderers.put(StampingType.FlagsStamping, new FlagsStampingRenderer());
		renderers.put(StampingType.KeySignatureStamping, new KeySignatureStampingRenderer());
		renderers.put(StampingType.LegerLineStamping, new LegerLineStampingRenderer());
		renderers.put(StampingType.NormalTimeStamping, new NormalTimeStampingRenderer());
		renderers.put(StampingType.StaffCursorStamping, new StaffCursorStampingRenderer());
		renderers.put(StampingType.StaffStamping, new StaffStampingRenderer());
		renderers.put(StampingType.StaffSymbolStamping, new StaffSymbolStampingRenderer());
		renderers.put(StampingType.StemStamping, new StemStampingRenderer());
		renderers.put(StampingType.SystemCursorStamping, new SystemCursorStampingRenderer());
		renderers.put(StampingType.TestStamping, new TestStampingRenderer());
		renderers.put(StampingType.TextStamping, new TextStampingRenderer());
		renderers.put(StampingType.TupletStamping, new TupletStampingRenderer());
		renderers.put(StampingType.VoltaStamping, new VoltaStampingRenderer());
		renderers.put(StampingType.WedgeStamping, new WedgeStampingRenderer());
	}

	/**
	 * Registers the given {@link StampingRenderer} for the given type of stamping.
	 */
	public static void registerRenderer(StampingType type, StampingRenderer renderer) {
		if (!init)
			init();
		renderers.put(type, renderer);
	}

	/**
	 * Draws the given {@link Stamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 * The correct renderer is automatically selected.
	 */
	public static void drawAny(Stamping stamping, Canvas canvas, RendererArgs args) {
		if (!init)
			init();
		StampingRenderer renderer = renderers.get(stamping.getType());
		if (renderer != null)
			renderer.draw(stamping, canvas, args);
	}

	/**
	 * Draws the given {@link Stamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	public abstract void draw(Stamping stamping, Canvas canvas, RendererArgs args);

	/**
	 * Draws the bounding shape of the given stamping, if it is a
	 * {@link Rectangle2f}.
	 */
	public void drawBoundingShape(Stamping stamping, Canvas canvas) {
		if (stamping.boundingShape != null && stamping.boundingShape instanceof Rectangle2f) {
			Rectangle2f r = (Rectangle2f) stamping.boundingShape;
			Color ci = new Color(0, 0, 255, 100);
			canvas.drawLine(r.nw(), r.ne(), ci, 0.5f);
			canvas.drawLine(r.ne(), r.se(), ci, 0.5f);
			canvas.drawLine(r.se(), r.sw(), ci, 0.5f);
			canvas.drawLine(r.sw(), r.nw(), ci, 0.5f);
		}
	}

}
