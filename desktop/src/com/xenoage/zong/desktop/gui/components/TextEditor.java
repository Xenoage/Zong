package com.xenoage.zong.desktop.gui.components;

import static com.xenoage.zong.desktop.utils.text.FormattedTextConverter.fromStyledDocument;
import static com.xenoage.zong.desktop.utils.text.FormattedTextConverter.toStyledDocument;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.View;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.desktop.gui.components.util.ScaledEditorKit;

/**
 * This is a Swing component which allows to edit
 * styled text.
 * 
 * TODO: move into editor package after eliminating all
 * usages in the viewer.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class TextEditor
	extends JTextPane {

	private int width;
	private int height;


	/**
	 * Creates an unscaled {@link TextEditor}.
	 */
	public TextEditor() {
	}

	/**
	 * Creates a scaled {@link TextEditor} with the given width and height.
	 */
	public TextEditor(int width, int height) {
		this.width = width;
		this.height = height;
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setEditorKit(new ScaledEditorKit());
		this.getDocument().putProperty("i18n", Boolean.TRUE);
		this.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * Creates and returns TextEditor for use in a dialog.
	 * It has the given background color and has no border.
	 */
	public static TextEditor createForDialog(Size2i size, Color backgroundColor) {
		TextEditor ret = new TextEditor();
		ret.width = size.width;
		ret.height = size.height;
		ret.setPreferredSize(new Dimension(size.width, size.height));
		ret.setBackground(backgroundColor);
		ret.setBorder(BorderFactory.createEmptyBorder());
		return ret;
	}

	/**
	 * Imports a {@link FormattedText} and shows it on
	 * this TextEditor.
	 */
	public void importFormattedText(FormattedText input) {
		setStyledDocument(toStyledDocument(input));
	}

	/**
	 * Creates a {@link FormattedText} from the content of this text box
	 * and returns it.
	 */
	public FormattedText exportFormattedText() {
		return fromStyledDocument(getStyledDocument());
	}

	@Override public void repaint(int x, int y, int width, int height) {
		super.repaint(0, 0, getWidth(), getHeight());
	}

	public void setZoom(float zoom) {
		this.getDocument().putProperty("ZOOM_FACTOR", (double) zoom);
		this.repaint();
	}

	@Override public Dimension getPreferredSize() {
		Double z = (Double) this.getDocument().getProperty("ZOOM_FACTOR");
		float zoom = 1;
		if (z != null) {
			zoom = z.floatValue();
		}
		return new Dimension((int) (zoom * (float) width), (int) (zoom * (float) height));

	}

	/**
	 * Computes the optimum size of this {@link TextEditor} in px.
	 */
	public Size2f computeOptimumSize() {
		View v = this.getUI().getRootView(this);
		v.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		return new Size2f(v.getPreferredSpan(View.X_AXIS), v.getPreferredSpan(View.Y_AXIS));
	}

}
