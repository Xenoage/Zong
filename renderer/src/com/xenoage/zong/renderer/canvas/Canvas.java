package com.xenoage.zong.renderer.canvas;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.symbols.path.Path;

/**
 * Base class for a canvas into which can be rendered
 * (e.g. an offscreen buffer, a printer document, and so on).
 * 
 * Platform-specific implementations have to be created,
 * e.g. for AWT or Android.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public abstract class Canvas {

	/** The size of the canvas in mm. */
	protected final Size2f sizeMm;
	/** The rendering format, like raster or vector. */
	protected final CanvasFormat format;
	/** The decoration mode. */
	protected final CanvasDecoration decoration;
	/** The completeness of the rendering. */
	protected final CanvasIntegrity integrity;


	/**
	 * Gets the graphics context, like a Graphics2D object in AWT.
	 */
	public abstract Object getGraphicsContext();

	/**
	 * Draws a line.
	 * @param p1              starting point of the line in mm
	 * @param p2              ending point of the line in mm
	 * @param color           color of the line
	 * @param lineWidth       width of the line in mm
	 */
	public abstract void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth);

	/**
	 * Draws a staff.
	 * @param pos               position of the upper left corner in mm
	 * @param length            length of the staff in mm
	 * @param lines             number of lines
	 * @param color             color of the lines
	 * @param lineWidth         width of the line in mm
	 * @param interlineSpace    space between lines in mm
	 */
	public abstract void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace);

	/**
	 * Draws a simplified staff (only a filled rectangle).
	 * @param pos               position of the upper left corner in mm
	 * @param length            length of the staff in mm
	 * @param height            height of the staff in mm
	 * @param color             color of the lines
	 */
	public abstract void drawSimplifiedStaff(Point2f pos, float length, float height, Color color);

	/**
	 * Draws the given {@link FormattedText} with the given {@link TextSelection} (or null)
	 * at the given position.
	 * @param yIsBaseline  if true, the y-position is the baseline of the first paragraph
	 */
	public abstract void drawText(FormattedText text, @MaybeNull TextSelection selection,
		Point2f position, boolean yIsBaseline, float width);
	
	/**
	 * Fills a path using the given color.
	 */
	public abstract void fillPath(Path path, Color color);

	/**
	 * Fills a rectangle.
	 * @param rect    the rectangle with coordinates in mm
	 * @param color   the color of the rectangle
	 */
	public abstract void fillRect(Rectangle2f rect, Color color);
	
	/**
	 * Draws an image.
	 * Dependent on the {@link CanvasIntegrity} of this canvas, a placeholder
	 * may be drawn instead of the image.
	 * @param rect       the rectangle with coordinates in mm
	 * @param imagePath  the file path of the image
	 */
	public abstract void drawImage(Rectangle2f rect, String imagePath);
	
	/**
	 * Saves the current transformation state on the stack.
	 * It can be restored by calling 
	 */
	public abstract void transformSave();
	
	/**
	 * Restores the last transformation state on the stack, which was stored by calling
	 * {@link #transformSave()}. If there is none, nothing happens.
	 */
	public abstract void transformRestore();
	
	/**
	 * Applies the given translation to all following operations.
	 */
	public abstract void transformTranslate(float x, float y);
	
	/**
	 * Applies the given scaling to all following operations.
	 */
	public abstract void transformScale(float x, float y);
	
	/**
	 * Applies the given rotation in degrees in counterclockwise direction
	 * to all following operations.
	 */
	public abstract void transformRotate(float angle);
	
}
