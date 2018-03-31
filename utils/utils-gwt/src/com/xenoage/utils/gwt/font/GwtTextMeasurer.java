package com.xenoage.utils.gwt.font;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;

/**
 * GWT implementation of a {@link TextMeasurer}.
 *
 * @author Andreas Wenger
 */
public class GwtTextMeasurer
		implements TextMeasurer {

	private static CanvasElement canvas = null;

	@Override public TextMetrics measure(FontInfo font, String text) {
		Context2d context = getContext();
		context.setFont(GwtFontUtils.getCssFont(font));
		com.google.gwt.canvas.dom.client.TextMetrics metrics = context.measureText(text);
		float width = Units.pxToMm((float) metrics.getWidth(), 1);
		//we do not know the ascent from HTML. we use the width of "W" instead, which should be similar.
		//as the descent and the leading we usethe third of the ascent.n ha
		metrics = context.measureText("W");
		float ascent = Units.pxToMm((float) metrics.getWidth(), 1);
		return new TextMetrics(ascent, ascent / 3, ascent / 3, width);
	}

	private static Context2d getContext() {
		if (canvas == null)
			canvas = (CanvasElement) Document.get().createElement("canvas");
		return canvas.getContext2d();
	}

}
