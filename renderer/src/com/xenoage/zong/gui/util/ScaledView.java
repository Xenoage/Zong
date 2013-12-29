/**
 * 
 */
package com.xenoage.zong.gui.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.Position;

/**
 * @author Uli
 *
 */
public class ScaledView extends BoxView
{
	public ScaledView(Element elem, int axis)
	{
		super(elem, axis);
	}

	public double getZoomFactor()
	{
		Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
		if (scale != null)
		{
			return scale.doubleValue();
		}

		return 1;
	}

	@Override public void paint(Graphics g, Shape allocation)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		double zoomFactor = getZoomFactor();
		AffineTransform old = g2d.getTransform();
		g2d.scale(zoomFactor, zoomFactor);
		super.paint(g2d, allocation);
		g2d.setTransform(old);
	}

	@Override public float getMinimumSpan(int axis)
	{
		float f = super.getMinimumSpan(axis);
		f *= getZoomFactor();
		return f;
	}

	@Override public float getMaximumSpan(int axis)
	{
		float f = super.getMaximumSpan(axis);
		f *= getZoomFactor();
		return f;
	}

	@Override public float getPreferredSpan(int axis)
	{
		float f = super.getPreferredSpan(axis);
		f *= getZoomFactor();
		return f;
	}

	@Override protected void layout(int width, int height)
	{
		super.layout(new Double(width / getZoomFactor()).intValue(),
				new Double(height * getZoomFactor()).intValue());
	}

	@Override public Shape modelToView(int pos, Shape a, Position.Bias b)
			throws BadLocationException
	{
		double zoomFactor = getZoomFactor();
		Rectangle alloc;
		alloc = a.getBounds();
		Shape s = super.modelToView(pos, alloc, b);
		alloc = s.getBounds();
		alloc.x *= zoomFactor;
		alloc.y *= zoomFactor;
		alloc.width *= zoomFactor;
		alloc.height *= zoomFactor;

		return alloc;
	}

	@Override public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
	{
		double zoomFactor = getZoomFactor();
		Rectangle alloc = a.getBounds();
		x /= zoomFactor;
		y /= zoomFactor;
		alloc.x /= zoomFactor;
		alloc.y /= zoomFactor;
		alloc.width /= zoomFactor;
		alloc.height /= zoomFactor;

		return super.viewToModel(x, y, alloc, bias);
	}

}