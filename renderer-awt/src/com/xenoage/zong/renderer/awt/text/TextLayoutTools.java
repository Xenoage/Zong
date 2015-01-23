package com.xenoage.zong.renderer.awt.text;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.jse.color.AwtColorUtils.toAwtColor;
import static com.xenoage.utils.jse.font.AwtFontUtils.toAwtFont;
import static com.xenoage.utils.math.geom.Point2f.p;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.renderer.awt.symbol.PathSymbolGraphicAttribute;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;

/**
 * Creates {@link TextLayout}s from {@link FormattedText} elements.
 * 
 * @author Andreas Wenger
 */
public class TextLayoutTools {

	/**
	 * Creates {@link TextLayouts} for the given {@link FormattedText},
	 * including both {@link FormattedTextString}s and {@link FormattedTextSymbol}s.
	 * @param text         the formatted text
	 * @param widthMm      the width of the text frame in mm (needed for non-left alignment)
	 * @param baseline0    true, if the baseline of the first paragraph should be at y=0,
	 *                     false, if y=0 is at the top edge of the first paragraph
	 * @param frc          the font render context needed to layout the text
	 */
	public static TextLayouts create(FormattedText text, float widthMm, boolean baseline0,
		FontRenderContext frc) {
		float offsetX = 0;
		float offsetY = 0;

		ArrayList<TextLayouts.Item> items = alist(text.getParagraphs().size());
		for (FormattedTextParagraph p : text.getParagraphs()) {
			//x correction
			if (p.getAlignment() == Alignment.Center)
				offsetX = (widthMm - p.getMetrics().getWidth()) / 2;
			else if (p.getAlignment() == Alignment.Right)
				offsetX = widthMm - p.getMetrics().getWidth();
			else
				offsetX = 0;

			//top y correction
			if (!baseline0)
				offsetY += p.getMetrics().getAscent();

			TextLayout tl = create(p, frc);
			if (tl != null)
				items.add(new TextLayouts.Item(tl, p(offsetX, offsetY), p.getText().length()));

			//bottom y correction
			if (baseline0)
				offsetY += p.getMetrics().getAscent();
			offsetY += (p.getMetrics().getDescent() + p.getMetrics().getLeading());
		}

		return new TextLayouts(items);
	}

	/**
	 * Creates a {@link TextLayout} for the given {@link FormattedTextParagraph},
	 * including both {@link FormattedTextString}s and {@link FormattedTextSymbol}s.
	 * If impossible (e.g. because the paragraph is empty), null is returned.
	 */
	private static TextLayout create(FormattedTextParagraph p, FontRenderContext frc) {
		//get the raw string
		AttributedString as = new AttributedString(p.getText());
		//set attributes
		int pos = 0;
		int count = 0;
		for (FormattedTextElement e : p.getElements()) {
			int len = e.getLength();
			if (len > 0) {
				if (e instanceof FormattedTextString) {
					as.addAttributes(createAttributesMap(e.getStyle()), pos, pos + len);
					count++;
				}
				else if (e instanceof FormattedTextSymbol) {
					FormattedTextSymbol fts = (FormattedTextSymbol) e;
					Symbol symbol = fts.getSymbol();
					if (symbol instanceof PathSymbol) {
						as.addAttribute(TextAttribute.FOREGROUND,
							toAwtColor(fts.getStyle().getColor()), pos, pos + 1);
						as.addAttribute(TextAttribute.CHAR_REPLACEMENT,
							new PathSymbolGraphicAttribute((PathSymbol) symbol, fts.getScaling()), pos, pos + 1);
						count += 2;
					}
				}
				pos += len;
			}
		}
		if (count == 0)
			return null; //creating a TextLayout would fail
		else
			return new TextLayout(as.getIterator(), frc);
	}

	/**
	 * Creates a Map with the attributes representing
	 * the given style for an AttibutedString.
	 */
	private static Map<TextAttribute, Object> createAttributesMap(FormattedTextStyle style) {
		Map<TextAttribute, Object> ret = new HashMap<TextAttribute, Object>();
		FontInfo fontInfo = notNull(style.getFont(), FontInfo.defaultValue);
		//font name
		Font font = toAwtFont(fontInfo);
		ret.put(TextAttribute.FAMILY, font.getFamily());
		//font size
		ret.put(TextAttribute.SIZE, font.getSize2D());
		//color
		ret.put(TextAttribute.FOREGROUND, toAwtColor(style.getColor()));
		//bold
		FontStyle fontStyle = fontInfo.getStyle();
		ret.put(TextAttribute.WEIGHT, (fontStyle.isSet(FontStyle.Bold) ? TextAttribute.WEIGHT_BOLD
			: TextAttribute.WEIGHT_REGULAR));
		//italic
		ret.put(TextAttribute.POSTURE,
			(fontStyle.isSet(FontStyle.Italic) ? TextAttribute.POSTURE_OBLIQUE
				: TextAttribute.POSTURE_REGULAR));
		//underline
		ret.put(TextAttribute.UNDERLINE,
			(fontStyle.isSet(FontStyle.Underline) ? TextAttribute.UNDERLINE_ON : null));
		//striketrough - TODO
		//ret.put(TextAttribute.STRIKETHROUGH,
		//  (style.underline ? TextAttribute.STRIKETHROUGH_ON : null));
		//superscript
		switch (style.getSuperscript()) {
			case Super:
				ret.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
				break;
			case Sub:
				ret.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
				break;
			default:
				ret.put(TextAttribute.SUPERSCRIPT, null);
		}
		return ret;
	}

}
