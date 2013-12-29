package com.xenoage.zong.renderer.canvas;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.text.FormattedText;


/**
 * Base class for a canvas into which can be rendered
 * (e.g. an offscreen buffer, a printer document, and so on).
 * 
 * There may be implementations for AWT or OpenGL for example.
 *
 * @author Andreas Wenger
 */
public abstract class Canvas
{
	
	protected final Size2f sizeMm;
	protected final CanvasFormat format;
	protected final CanvasDecoration decoration;
	protected final CanvasIntegrity integrity;
	
	
	public Canvas(Size2f sizeMm, CanvasFormat format, CanvasDecoration decoration, CanvasIntegrity integrity)
	{
		this.sizeMm = sizeMm;
		this.format = format;
		this.decoration = decoration;
		this.integrity = integrity;
	}
	
	
	/**
	 * Gets the graphics context, like a Graphics2D object in AWT
	 * or a GL object in OpenGL.
	 */
	public abstract Object getGraphicsContext();
	
	
	/**
	 * Gets the size of the canvas in mm.
	 */
	public Size2f getSizeMm()
	{
		return sizeMm;
	}
	
	
	/**
	 * Gets the rendering format, like bitmap or vector.
	 */
	public CanvasFormat getFormat()
	{
		return format;
	}
	
	
	/**
	 * Gets the decoration mode.
	 */
	public CanvasDecoration getDecoration()
	{
		return decoration;
	}
	
	
	/**
	 * Gets the completeness of the rendering.
	 */
	public CanvasIntegrity getIntegrity()
	{
		return integrity;
	}
  
  
  /**
   * Draws a line.
   * @param p1              starting point of the line
   * @param p2              ending point of the line
   * @param color           color of the line
   * @param lineWidth       width of the line in px
   */
  public abstract void drawLine(Point2f p1, Point2f p2,
  	ColorInfo color, float lineWidth);
  
  
  /**
   * Draws a staff.
   * @param pos               position of the upper left corner in mm
   * @param length            length of the staff in mm
   * @param lines             number of lines
   * @param color             color of the lines
   * @param lineWidth         width of the line in mm
   * @param interlineSpace    space between lines in mm
   */
  public abstract void drawStaff(Point2f pos, float length, int lines,
  	ColorInfo color, float lineWidth, float interlineSpace);
  
  
  /**
   * Draws a simplified staff (only a filled rectangle).
   * @param pos               position of the upper left corner in px
   * @param length            length of the staff in px
   * @param height            height of the staff in px
   * @param color             color of the lines
   */
  public abstract void drawSimplifiedStaff(Point2f pos, float length, float height,
  	ColorInfo color);
  
  
  /**
   * Draws the given {@link FormattedText} with the given {@link TextSelection} (or null)
   * at the given position. The y-position is the baseline of the first paragraph.
   */
  public abstract void drawText(FormattedText text, @MaybeNull TextSelection selection,
  	Point2f position, boolean yIsBaseline, float width);

  
  /**
   * Draws the given Symbol using the given color to the given
   * position with the given scaling.
   */
  public abstract void drawSymbol(Symbol symbol, ColorInfo color,
    Point2f position, Point2f scaling);
  
  
  /**
   * Draws a beam using the given quad and color.
   * @param points  the four points in clockwise order,
   *                beginning with the lower left point
   * @param color   the color of the beam
   * @param interlineSpace  the interline space in mm
   */
  public abstract void drawBeam(Point2f[] points, ColorInfo color, float interlineSpace);
  
  
  /**
   * Draws a tie/slur using the given Bézier curve.
   * @param p1      the starting point
   * @param p2      the ending point
   * @param c1      the first control point
   * @param c2      the second control point
   * @param interlineSpace  the interline space in mm
   * @param color   the color of the slur
   */
  public abstract void drawCurvedLine(Point2f p1, Point2f p2, Point2f c1, Point2f c2,
  	float interlineSpace, ColorInfo color);
  
  
  /**
   * Fills a rectangle.
   * @param rect    the rectangle
   * @param color   the color of the rectangle
   */
  public abstract void fillRect(Rectangle2f rect, ColorInfo color);
  
  
}
