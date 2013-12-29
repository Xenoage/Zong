package com.xenoage.zong.renderer.symbols;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.swing.color.AWTColorTools;
import com.xenoage.zong.desktop.renderer.canvas.AWTCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.WarningSymbol;


/**
 * AWT renderer for {@link Symbol}s.
 * 
 * @author Andreas Wenger
 */
public class AWTSymbolsRenderer
	extends SymbolsRenderer
{
	
	public static final AWTSymbolsRenderer instance = new AWTSymbolsRenderer();
	
	
	/**
   * Draws the given {@link PathSymbol} on the given {@link AWTCanvas}
   * with the given color at the given position and the given scaling.
   */
  @Override public void draw(PathSymbol symbol, Canvas canvas, ColorInfo color,
  	Point2f position, Point2f scaling)
  {
  	Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
    AffineTransform g2dTransform = g2d.getTransform();
    g2d.translate(position.x, position.y);
    g2d.scale(scaling.x, scaling.y);
    g2d.setColor(AWTColorTools.createColor(color));
    g2d.fill((Shape) symbol.path);
    
    /* TEST
    g2d.setColor(Color.green);
    g2d.setStroke(new BasicStroke(0.1f));
    g2d.draw(new Line2D.Float(0, -1, 0, +1)); //*/
    
    g2d.setTransform(g2dTransform);
  }
  
  
  /**
   * Draws the given {@link WarningSymbol} on the given {@link AWTCanvas}
   * with the given color at the given position and the given scaling.
   */
  @Override public void draw(WarningSymbol symbol, Canvas canvas, ColorInfo color,
    Point2f position, Point2f scaling)
  {
  	//the warning symbol is not visible on the rendering of the result
  	if (canvas.getDecoration() == CanvasDecoration.Interactive)
  		return;
  	//TODO: paint warning symbol
  	//Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
  	//...
  }
	

}
