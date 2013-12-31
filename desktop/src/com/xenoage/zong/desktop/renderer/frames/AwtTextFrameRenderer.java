package com.xenoage.zong.desktop.renderer.frames;

import java.awt.Color;
import java.awt.Graphics2D;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.desktop.gui.components.TextEditor;
import com.xenoage.zong.desktop.renderer.canvas.AwtCanvas;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FrameRenderer;

/**
 * AWT renderer for a {@link TextFrame}.
 * 
 * @author Andreas Wenger
 */
public class AwtTextFrameRenderer
	extends FrameRenderer {

	/**
	 * {@inheritDoc}
	 */
	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		TextFrame textFrame = (TextFrame) frame;
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);

		float w = frame.getSize().width;
		float h = frame.getSize().height;
		g2d.translate(-w / 2, -h / 2);

		canvas.drawText(textFrame.getTextWithLineBreaks(), null, new Point2f(0, 0), false, w);

		/* //TEST
		g2d.setColor(Color.green);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawRect(0, 0, (int) frame.getSize().width, (int) frame.getSize().height); //*/
	}

	/**
	 * Creates and returns a {@link TextEditor} for the
	 * given {@link TextFrame}.
	 * 
	 * UNNEEDED ?
	 */
	public static TextEditor createTextEditor(TextFrame textFrame) {
		TextEditor editor = new TextEditor(Units.mmToPxInt(textFrame.getSize().width, 1),
			Units.mmToPxInt(textFrame.getSize().height, 1));
		editor.setBackground(new Color(1, 1, 1, 0));
		editor.importFormattedText(textFrame.getText());
		editor.setSize(editor.getPreferredSize());
		return editor;
	}

}
