package com.xenoage.zong.desktop.utils.text;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.jse.color.AwtColorUtils.fromAwtColor;
import static com.xenoage.utils.jse.color.AwtColorUtils.toAwtColor;
import static com.xenoage.utils.jse.font.AwtFontUtils.toAwtFont;
import static com.xenoage.zong.desktop.utils.text.AlignmentUtils.applyAlignmentToAttributeSet;
import static com.xenoage.zong.desktop.utils.text.AlignmentUtils.fromAttributeSet;

import java.awt.Font;
import java.awt.font.TextLayout;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.Superscript;

/**
 * This class converts a {@link StyledDocument} or a {@link TextLayout}
 * into a {@link FormattedText} or the other way round.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextConverter {

	/**
	 * Creates a {@link FormattedText} from the given {@link StyledDocument}.
	 */
	public static FormattedText fromStyledDocument(StyledDocument styledDoc) {
		CList<FormattedTextParagraph> paragraphs = clist();
		int endPos = styledDoc.getLength();
		AttributeSet attribute = null;
		String textelement = "";
		String letter = "";
		CList<FormattedTextElement> elements = clist();
		Alignment alignment = null;

		//iterate through the letters. If the style has changed, create a new
		//element, otherwise just attach it to the old one, and create a new
		//paragraph, when '\n' is found
		for (int i = 0; i < endPos; i++) {
			try {
				letter = styledDoc.getText(i, 1);
			} catch (BadLocationException e) {
			}
			//line break: create new paragraph
			if (letter.equals("\n")) {
				if (!textelement.equals("")) {
					elements.add(new FormattedTextString(textelement,
						getStyleFromAttributeSet(attribute)));
					textelement = "";
				}
				paragraphs.add(new FormattedTextParagraph(elements, alignment));
				elements = clist();
				alignment = null;
			}
			else if (attribute != styledDoc.getCharacterElement(i).getAttributes()) {
				//set alignment, if not already done
				if (alignment == null) {
					alignment = fromAttributeSet(styledDoc.getParagraphElement(i).getAttributes());
				}
				//style has changed, so save old element and create a new one
				if (!textelement.equals("")) {
					elements.add(new FormattedTextString(textelement,
						getStyleFromAttributeSet(attribute)));
				}
				attribute = styledDoc.getCharacterElement(i).getAttributes();
				textelement = letter;
			}
			else {
				//style stayed the same, so just append
				textelement += letter;
			}
		}
		if (!textelement.equals("")) {
			//save the last string
			elements.add(new FormattedTextString(textelement,
				getStyleFromAttributeSet(attribute)));
		}
		if (elements.size() > 0) {
			//add (non-empty) paragraph
			paragraphs.add(new FormattedTextParagraph(elements, alignment));
		}
		return new FormattedText(paragraphs);
	}

	/**
	 * Creates a {@link StyledDocument} from the given {@link FormattedText}.
	 */
	public static StyledDocument toStyledDocument(FormattedText input) {
		StyledDocument styledDoc = new DefaultStyledDocument();
		//handle all paragraphs
		SimpleAttributeSet lastStyle = new SimpleAttributeSet();
		int line = 0;
		for (FormattedTextParagraph p : input.getParagraphs()) {
			//apply the alignment of the paragraph (apply style to the first letter of the paragraph)
			SimpleAttributeSet paragraphStyle = new SimpleAttributeSet();
			applyAlignmentToAttributeSet(p.getAlignment(), paragraphStyle);
			styledDoc.setParagraphAttributes(styledDoc.getLength(), 1, paragraphStyle, true);
			//handle all elements within this paragraph
			for (FormattedTextElement t : p.getElements()) //TODO: Symbols ?!
			{
				if (t instanceof FormattedTextString) {
					FormattedTextString s = (FormattedTextString) t;
					try {
						lastStyle = getAttributeSetFromStyle(s.getStyle());
						styledDoc.insertString(styledDoc.getLength(), s.getText(), lastStyle);
					} catch (BadLocationException e) {
					}
				}
			}
			//after the paragraph (not after the last): insert a line break
			if (line < input.getParagraphs().size() - 1) {
				try {
					styledDoc.insertString(styledDoc.getLength(), "\n", lastStyle);
				} catch (BadLocationException e) {
				}
			}
			line++;
		}
		return styledDoc;
	}

	private static FormattedTextStyle getStyleFromAttributeSet(AttributeSet attr) {
		if (attr == null) {
			return FormattedTextStyle.defaultStyle;
		}

		//font style
		FontStyle fontStyle = FontStyle.normal;
		if (StyleConstants.isBold(attr)) {
			fontStyle = fontStyle.with(FontStyle.Bold);
		}
		if (StyleConstants.isItalic(attr)) {
			fontStyle = fontStyle.with(FontStyle.Italic);
		}
		if (StyleConstants.isUnderline(attr)) {
			fontStyle = fontStyle.with(FontStyle.Underline);
		}
		if (StyleConstants.isStrikeThrough(attr)) {
			fontStyle = fontStyle.with(FontStyle.Strikethrough);
		}

		//superscript
		Superscript superscript = Superscript.Normal;
		if (StyleConstants.isSuperscript(attr)) {
			superscript = Superscript.Super;
		}
		if (StyleConstants.isSubscript(attr)) {
			superscript = Superscript.Sub;
		}

		//font
		FontInfo fontInfo = new FontInfo(StyleConstants.getFontFamily(attr),
			(float) StyleConstants.getFontSize(attr), fontStyle);

		return new FormattedTextStyle(fontInfo, fromAwtColor(StyleConstants.getForeground(attr)), superscript);
	}

	private static SimpleAttributeSet getAttributeSetFromStyle(FormattedTextStyle style) {
		SimpleAttributeSet attr = new SimpleAttributeSet();
		if (style == null) {
			return attr;
		}

		//font style
		FontStyle fontStyle = style.getFont().getStyle();
		StyleConstants.setBold(attr, fontStyle.isSet(FontStyle.Bold));
		StyleConstants.setItalic(attr, fontStyle.isSet(FontStyle.Italic));
		StyleConstants.setUnderline(attr, fontStyle.isSet(FontStyle.Underline));
		StyleConstants.setStrikeThrough(attr, fontStyle.isSet(FontStyle.Strikethrough));

		//color
		StyleConstants.setForeground(attr, toAwtColor(style.getColor()));

		//font
		Font font = toAwtFont(style.getFont());
		StyleConstants.setFontFamily(attr, font.getFamily());
		StyleConstants.setFontSize(attr, font.getSize());

		//superscript
		if (style.getSuperscript() == Superscript.Super) {
			StyleConstants.setSuperscript(attr, true);
		}
		else if (style.getSuperscript() == Superscript.Sub) {
			StyleConstants.setSubscript(attr, true);
		}

		return attr;
	}

}
