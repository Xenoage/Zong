package com.xenoage.zong.renderer.gwtcanvas;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.LayoutRenderer;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.gwtcanvas.canvas.GwtCanvas;

import static com.xenoage.utils.math.geom.Point2i.origin;

/**
 * Like {@link LayoutRenderer}, but more specific rendering methods
 * for a GWT HTML5 canvas.
 *
 * @author Andreas Wenger
 */
public class GwtCanvasLayoutRenderer {
	
	/**
	 * Paints the given page of the given {@link Layout} at the given zoom level
	 * into the given {@link CanvasElement}, which is resized to include the whole page.
	 */
	public static void paintToCanvas(Layout layout, int pageIndex, float zoom,
																	 com.google.gwt.canvas.client.Canvas canvas) {
		Context2d context = canvas.getContext2d();

		//compute size
		Page page = layout.getPages().get(pageIndex);
		Size2f pageSize = page.getFormat().getSize();
		int width = Units.mmToPxInt(pageSize.width, zoom);
		int height = Units.mmToPxInt(pageSize.height, zoom);

		//resize canvas and coordinate space
		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		int coordSpaceFactor = 2; //double resolution: smoother
		canvas.setCoordinateSpaceWidth(width * coordSpaceFactor);
		canvas.setCoordinateSpaceHeight(height * coordSpaceFactor);
		context.scale(coordSpaceFactor, coordSpaceFactor);

		//white page
    context.setFillStyle("white");
    context.fillRect(0, 0, width, height);

    //paint layout
		LayoutRenderer.paintToCanvas(layout, pageIndex, zoom, origin,
			new GwtCanvas(context, CanvasFormat.Raster,
				CanvasDecoration.Interactive, CanvasIntegrity.Perfect));
	}

}
