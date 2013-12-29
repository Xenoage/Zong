package com.xenoage.zong.renderer.canvas;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.swing.color.AWTColorTools;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.slur.AWTSlurRenderer;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;
import com.xenoage.zong.renderer.symbols.AWTSymbolsRenderer;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.util.text.TextLayoutTools;
import com.xenoage.zong.util.text.TextLayouts;


/**
 * This class contains methods for painting
 * on an AWT context.
 *
 * @author Andreas Wenger
 */
public class AWTCanvas
  extends Canvas
{
	
	//the AWT graphics context
	private final Graphics2D g2d;
  
  
  /**
   * Creates an {@link AWTCanvas} with the given size in mm for the given context,
   * format, decoration mode and itegrity.
   */
  public AWTCanvas(Graphics2D g2d, Size2f sizeMm, CanvasFormat format,
  	CanvasDecoration decoration, CanvasIntegrity integrity)
  {
  	super(sizeMm, format, decoration, integrity);
    this.g2d = g2d;
  }
  
  
  /**
   * Gets the {@link Graphics2D} graphics context.
   */
  @Override public Graphics2D getGraphicsContext()
  {
  	return g2d;
  }
  
  
  /**
   * Convenience method: Gets the {@link Graphics2D} graphics context from
   * the given {@link Canvas}. If it is not a {@link AWTCanvas}, a {@link ClassCastException}
   * is thrown.
   */
  public static Graphics2D getGraphics2D(Canvas canvas)
  {
  	return ((AWTCanvas) canvas).getGraphicsContext();
  }
  
  
  /**
   * {@inheritDoc}
   * Text selection is ignored.
   */
  @Override public void drawText(FormattedText text, TextSelection selection,
  	Point2f position, boolean yIsBaseline, float frameWidth)
  {
  	//do not ignore text selection in interactive mode
  	if (decoration == CanvasDecoration.Interactive) {
  		//TODO
  	}
  	
  	FontRenderContext frc = g2d.getFontRenderContext();
  	TextLayouts textLayouts = TextLayoutTools.create(text, frameWidth, yIsBaseline, frc);
  	
  	AffineTransform oldTransform = g2d.getTransform();
  	g2d.translate(position.x, position.y);
  	textLayouts.draw(g2d);
  	g2d.setTransform(oldTransform);
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override public void drawSymbol(Symbol symbol, ColorInfo color,
    Point2f position, Point2f scaling)
  {
    AWTSymbolsRenderer.instance.draw(symbol, this, color, position, scaling);
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override public void drawLine(Point2f p1, Point2f p2, 
    ColorInfo color, float lineWidth)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    g2d.draw(new Line2D.Float(p1.x, p1.y, p2.x, p2.y));
  }


  /**
   * {@inheritDoc}
   */
  @Override public void drawStaff(Point2f pos, float length, int lines,
    ColorInfo color, float lineWidth, float interlineSpace)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    for (int i = 0; i < lines; i++)
    {
      g2d.fill(new Rectangle2D.Float(
      	pos.x, pos.y + i * interlineSpace - lineWidth / 2, length, lineWidth));
    }
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override public void drawSimplifiedStaff(Point2f pos, float length, float height,
    ColorInfo color)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    g2d.fill(new Rectangle2D.Float(pos.x, pos.y, length, height));
  }
  
  
  /**
   * {@inheritDoc}
   */
  public void fillEllipse(Point2f pCenter, 
    float width, float height, ColorInfo color)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    //use float coordinates to allow Java2D to optimize quality
    g2d.fill(new Ellipse2D.Float(
      pCenter.x - width / 2,
      pCenter.y - height / 2,
      width, height));
  }


  /**
   * {@inheritDoc}
   */
  @Override public void drawBeam(Point2f[] points, ColorInfo color, float interlineSpace)
  {
    Rectangle2D beamSymbol = new Rectangle2D.Float(-1f, -0.25f, 2f, 0.5f);
    
    g2d.setColor(AWTColorTools.createColor(color));
    
    AffineTransform g2dTransform = g2d.getTransform();
    
    float imageWidth = points[2].x - points[0].x;
    float imageHeight = points[3].y - points[0].y;
    float beamGrowthHeight = points[2].y - points[0].y;
    
    g2d.translate(points[0].x + imageWidth / 2, points[0].y + imageHeight / 2);
    g2d.shear(0, beamGrowthHeight / imageWidth);
    g2d.scale(imageWidth / beamSymbol.getWidth(),
      (points[1].y - points[0].y) / beamSymbol.getHeight());
    //g2d.fillRect(-5000000, -50000, 10000000, 100000);
    g2d.fill(beamSymbol);
    
    g2d.setTransform(g2dTransform);
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override public void drawCurvedLine(Point2f p1, Point2f p2, Point2f c1, Point2f c2,
  	float interlineSpace, ColorInfo color)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    SimpleSlurShape slurShape = new SimpleSlurShape(p1, p2, c1, c2, interlineSpace);
    g2d.fill(AWTSlurRenderer.getShape(slurShape));
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override public void fillRect(Rectangle2f rect, ColorInfo color)
  {
    g2d.setColor(AWTColorTools.createColor(color));
    g2d.fill(new Rectangle2D.Float(
      rect.position.x, rect.position.y, rect.size.width, rect.size.height));
  }
  

}
