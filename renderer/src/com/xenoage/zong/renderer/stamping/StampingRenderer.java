package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StampingType;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

import java.util.HashMap;

import static com.xenoage.utils.color.Color.color;

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
		renderers = new HashMap<>();
		renderers.put(StampingType.BarlineStamping, new BarlineRenderer());
		renderers.put(StampingType.BeamStamping, new BeamRenderer());
		renderers.put(StampingType.BracketStamping, new BracketRenderer());
		renderers.put(StampingType.SlurStamping, new SlurRenderer());
		renderers.put(StampingType.EmptySpaceStamping, null);
		renderers.put(StampingType.FlagsStamping, new FlagsRenderer());
		renderers.put(StampingType.KeySignatureStamping, new KeySignatureRenderer());
		renderers.put(StampingType.LegerLineStamping, new LegerLineRenderer());
		renderers.put(StampingType.NormalTimeStamping, new TimeRenderer());
		renderers.put(StampingType.StaffCursorStamping, new StaffCursorRenderer());
		renderers.put(StampingType.StaffStamping, new StaffRenderer());
		renderers.put(StampingType.StaffSymbolStamping, new StaffSymbolRenderer());
		renderers.put(StampingType.StemStamping, new StemRenderer());
		renderers.put(StampingType.SystemCursorStamping, new SystemCursorRenderer());
		renderers.put(StampingType.TestStamping, new TestRenderer());
		renderers.put(StampingType.TextStamping, new TextRenderer());
		renderers.put(StampingType.TupletStamping, new TupletRenderer());
		renderers.put(StampingType.VoltaStamping, new VoltaRenderer());
		renderers.put(StampingType.WedgeStamping, new WedgeRenderer());
		init = true;
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
		if (stamping == null)
			return;
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
		if (stamping.getBoundingShape() instanceof Rectangle2f) {
			Rectangle2f r = (Rectangle2f) stamping.getBoundingShape();
			Color ci = color(0, 0, 255, 100);
			canvas.drawLine(r.nw(), r.ne(), ci, 0.5f);
			canvas.drawLine(r.ne(), r.se(), ci, 0.5f);
			canvas.drawLine(r.se(), r.sw(), ci, 0.5f);
			canvas.drawLine(r.sw(), r.nw(), ci, 0.5f);
		}
	}

}
