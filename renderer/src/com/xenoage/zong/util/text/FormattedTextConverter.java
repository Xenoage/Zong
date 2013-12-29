package com.xenoage.zong.util.text;

import static com.xenoage.utils.base.collections.CollectionUtils.alist;
import static com.xenoage.utils.base.collections.CollectionUtils.map;
import static com.xenoage.utils.graphics.font.FontStyle.Bold;
import static com.xenoage.utils.graphics.font.FontStyle.Italic;
import static com.xenoage.utils.graphics.font.FontStyle.Underline;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static java.awt.font.TextAttribute.FAMILY;
import static java.awt.font.TextAttribute.FOREGROUND;
import static java.awt.font.TextAttribute.POSTURE;
import static java.awt.font.TextAttribute.POSTURE_OBLIQUE;
import static java.awt.font.TextAttribute.POSTURE_REGULAR;
import static java.awt.font.TextAttribute.SIZE;
import static java.awt.font.TextAttribute.SUPERSCRIPT;
import static java.awt.font.TextAttribute.SUPERSCRIPT_SUB;
import static java.awt.font.TextAttribute.SUPERSCRIPT_SUPER;
import static java.awt.font.TextAttribute.UNDERLINE;
import static java.awt.font.TextAttribute.UNDERLINE_ON;
import static java.awt.font.TextAttribute.WEIGHT;
import static java.awt.font.TextAttribute.WEIGHT_BOLD;
import static java.awt.font.TextAttribute.WEIGHT_REGULAR;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GraphicAttribute;
import java.awt.font.ShapeGraphicAttribute;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.xenoage.utils.graphics.font.FontInfo;
import com.xenoage.utils.graphics.font.FontStyle;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.utils.swing.color.AWTColorTools;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.text.FormattedTextElement;
import com.xenoage.zong.text.FormattedTextParagraph;
import com.xenoage.zong.text.FormattedTextString;
import com.xenoage.zong.text.FormattedTextStyle;
import com.xenoage.zong.text.FormattedTextSymbol;
import com.xenoage.zong.text.Superscript;


/**
 * This class converts a {@link StyledDocument} or a {@link TextLayout}
 * into a {@link FormattedText} or the other way round.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextConverter
{
	
	/**
	 * Creates a {@link FormattedText} from the given {@link StyledDocument}.
	 */
	public static FormattedText fromStyledDocument(StyledDocument styledDoc)
	{
		PVector<FormattedTextParagraph> paragraphs = pvec();
		int endPos = styledDoc.getLength();
		AttributeSet attribute = null;
		String textelement = "";
		String letter = "";
		PVector<FormattedTextElement> elements = pvec();
		Alignment alignment = null;

		//iterate through the letters. If the style has changed, create a new
		//element, otherwise just attach it to the old one, and create a new
		//paragraph, when '\n' is found
		for (int i = 0; i < endPos; i++)
		{
			try
			{
				letter = styledDoc.getText(i, 1);
			}
			catch (BadLocationException e)
			{
			}
			//line break: create new paragraph
			if (letter.equals("\n"))
			{
				if (!textelement.equals(""))
				{
					elements = elements.plus(new FormattedTextString(
						textelement, getStyleFromAttributeSet(attribute)));
					textelement = "";
				}
				paragraphs = paragraphs.plus(new FormattedTextParagraph(elements, alignment));
				elements = pvec();
				alignment = null;
			}
			else if (attribute != styledDoc.getCharacterElement(i).getAttributes())
			{
				//set alignment, if not already done
				if (alignment == null)
				{
					alignment = Alignment.fromAttributeSet(
						styledDoc.getParagraphElement(i).getAttributes());
				}
				//style has changed, so save old element and create a new one
				if (!textelement.equals(""))
				{
					elements = elements.plus(new FormattedTextString(
						textelement, getStyleFromAttributeSet(attribute)));
				}
				attribute = styledDoc.getCharacterElement(i).getAttributes();
				textelement = letter;
			}
			else
			{
				//style stayed the same, so just append
				textelement += letter;
			}
		}
		if (!textelement.equals(""))
		{
			//save the last string
			elements = elements.plus(new FormattedTextString(
				textelement, getStyleFromAttributeSet(attribute)));
		}
		if (elements.size() > 0)
		{
			//add (non-empty) paragraph
			paragraphs = paragraphs.plus(new FormattedTextParagraph(elements, alignment));
		}
		return new FormattedText(paragraphs);
	}
	
	
	/**
	 * Creates a {@link StyledDocument} from the given {@link FormattedText}.
	 */
	public static StyledDocument toStyledDocument(FormattedText input)
	{
		StyledDocument styledDoc = new DefaultStyledDocument();
		//handle all paragraphs
		SimpleAttributeSet lastStyle = new SimpleAttributeSet();
		int line = 0;
		for (FormattedTextParagraph p : input.paragraphs)
		{
			//apply the alignment of the paragraph (apply style to the first letter of the paragraph)
			SimpleAttributeSet paragraphStyle = new SimpleAttributeSet();
			p.alignment.applyOnAttributeSet(paragraphStyle);
			styledDoc.setParagraphAttributes(styledDoc.getLength(), 1, paragraphStyle, true);
			//handle all elements within this paragraph
			for (FormattedTextElement t : p.elements) //TODO: Symbols ?!
			{
				if (t instanceof FormattedTextString)
				{
					FormattedTextString s = (FormattedTextString) t;
					try
					{
						lastStyle = getAttributeSetFromStyle(s.getStyle());
						styledDoc.insertString(styledDoc.getLength(), s.getText(), lastStyle);
					}
					catch (BadLocationException e)
					{
					}
				}
			}
			//after the paragraph (not after the last): insert a line break
			if (line < input.paragraphs.size() - 1)
			{
				try
				{
					styledDoc.insertString(styledDoc.getLength(), "\n", lastStyle);
				}
				catch (BadLocationException e)
				{
				}
			}
			line++;
		}
		return styledDoc;
	}
	
	
	/**
	 * Converts the given {@link FormattedText} into a list of
	 * {@link TextLayout}s, one for each paragraph. Line breaking is not done
	 * within this method, so each paragraph from the given text is transformed into a
	 * paragraph in the returned text.
	 * Since {@link TextLayout}s have no alignment, the alignment of the given text
	 * paragraphs is ignored.
	 */
	public PVector<TextLayout> toTextLayout(FormattedText text, FontRenderContext frc)
  {
		PVector<TextLayout> ret = pvec();
  	for (FormattedTextParagraph pSrc : text.paragraphs)
  	{
  		StringBuilder textDest = new StringBuilder();
  		ArrayList<Integer> textLengthDest = alist();
  		ArrayList<Map<TextAttribute, Object>> styleDest = alist();
  	  for (FormattedTextElement eSrc : pSrc.elements)
  	  {
  	  	if (eSrc instanceof FormattedTextString)
  	  	{
  	  		//string
  	  		FormattedTextString fts = (FormattedTextString) eSrc;
  	  		textDest.append(fts.getText());
  	  		textLengthDest.add(fts.getText().length());
  	  		styleDest.add(getTextAttributes(fts.getStyle()));
  	  	}
  	  	else
  	  	{
  	  		//symbol
  	  		FormattedTextSymbol fts = (FormattedTextSymbol) eSrc;
  	  		Symbol symbol = fts.getSymbol();
  	  		GraphicAttribute graphicAttr = null;
  	  		if (symbol instanceof PathSymbol) //TODO: support other symbols as well
  	  		{
  	  			PathSymbol pathSymbol = (PathSymbol) symbol;
  	  			graphicAttr = new ShapeGraphicAttribute(
	  	  			(Shape) pathSymbol.path, GraphicAttribute.ROMAN_BASELINE, ShapeGraphicAttribute.FILL);
  	  		}
  	  		if (graphicAttr != null)
  	  		{
  	  			String c = "\ufffc"; //Unicode object replacement character
  	  			textDest.append(c);
  	  			Map<TextAttribute, Object> map = map();
  	  			map.put(TextAttribute.CHAR_REPLACEMENT, graphicAttr);
  	  			styleDest.add(map);
  	  			textLengthDest.add(1);
  	  		}
  	  	}
  	  }
  	  //combine all collected elements into a TextLayout
  	  AttributedString asDest = new AttributedString(textDest.toString());
  	  int pos = 0;
  	  for (int i : range(styleDest))
  	  {
  	  	int length = textLengthDest.get(i);
  	  	asDest.addAttributes(styleDest.get(i), pos, pos + length);
  	  	pos += length;
  	  }
  	  ret = ret.plus(new TextLayout(asDest.getIterator(), frc));
    }
  	return ret;
  }
	
	
	/**
   * Converts a {@link FormattedTextStyle} into a map of {@link TextAttribute}s.
   */
  private static Map<TextAttribute, Object> getTextAttributes(
    FormattedTextStyle style)
  {
    Map<TextAttribute, Object> ret = new HashMap<TextAttribute, Object>();
    //font name
    Font font = style.getFontAWT();
    ret.put(FAMILY, font.getFamily());
    //font size
    ret.put(SIZE, font.getSize2D());
    //color
    ret.put(FOREGROUND, style.color);
    //bold
    FontStyle fontStyle = style.fontInfo.getStyle();
    ret.put(WEIGHT, (fontStyle.isSet(Bold) ? WEIGHT_BOLD : WEIGHT_REGULAR));
    //italic
    ret.put(POSTURE, (fontStyle.isSet(Italic) ? POSTURE_OBLIQUE : POSTURE_REGULAR));
    //underline
    ret.put(UNDERLINE, (fontStyle.isSet(Underline) ? UNDERLINE_ON : null));
    //striketrough - TODO
    //ret.put(TextAttribute.STRIKETHROUGH,
    //  (style.underline ? TextAttribute.STRIKETHROUGH_ON : null));
    //superscript
    switch (style.superscript)
    {
    	case Super:
    		ret.put(SUPERSCRIPT, SUPERSCRIPT_SUPER);
    		break;
    	case Sub:
    		ret.put(SUPERSCRIPT, SUPERSCRIPT_SUB);
    		break;
    	default:
    		ret.put(SUPERSCRIPT, null);
    }
    return ret;
  }
	

	private static FormattedTextStyle getStyleFromAttributeSet(AttributeSet attr)
	{
		if (attr == null)
		{
			return new FormattedTextStyle();
		}
		
		//font style
		FontStyle fontStyle = FontStyle.normal;
		if (StyleConstants.isBold(attr))
		{
			fontStyle = fontStyle.with(FontStyle.Bold);
		}
		if (StyleConstants.isItalic(attr))
		{
			fontStyle = fontStyle.with(FontStyle.Italic);
		}
		if (StyleConstants.isUnderline(attr))
		{
			fontStyle = fontStyle.with(FontStyle.Underline);
		}
		if (StyleConstants.isStrikeThrough(attr))
		{
			fontStyle = fontStyle.with(FontStyle.Strikethrough);
		}
		
		//superscript
		Superscript superscript = Superscript.Normal;
		if (StyleConstants.isSuperscript(attr))
		{
			superscript = Superscript.Super;
		}
		if (StyleConstants.isSubscript(attr))
		{
			superscript = Superscript.Sub;
		}
		
		//font
		FontInfo fontInfo = new FontInfo(StyleConstants.getFontFamily(attr),
			(float) StyleConstants.getFontSize(attr), fontStyle);
		
		return new FormattedTextStyle(fontInfo,
			AWTColorTools.createColorInfo(StyleConstants.getForeground(attr)), superscript);
	}
	
	
	private static SimpleAttributeSet getAttributeSetFromStyle(FormattedTextStyle style)
	{
		SimpleAttributeSet attr = new SimpleAttributeSet();
		if (style == null)
		{
			return attr;
		}
		
		//font style
		FontStyle fontStyle = style.fontInfo.getStyle();
		StyleConstants.setBold(attr, fontStyle.isSet(FontStyle.Bold));
		StyleConstants.setItalic(attr, fontStyle.isSet(FontStyle.Italic));
		StyleConstants.setUnderline(attr, fontStyle.isSet(FontStyle.Underline));
		StyleConstants.setStrikeThrough(attr, fontStyle.isSet(FontStyle.Strikethrough));
		
		//color
		StyleConstants.setForeground(attr, AWTColorTools.createColor(style.color));
		
		//font
		Font font = style.getFontAWT();
		StyleConstants.setFontFamily(attr, font.getFamily());
		StyleConstants.setFontSize(attr, font.getSize());
		
		//superscript
		if (style.superscript == Superscript.Super)
		{
			StyleConstants.setSuperscript(attr, true);
		}
		else if (style.superscript == Superscript.Sub)
		{
			StyleConstants.setSubscript(attr, true);
		}
		
		return attr;
	}

}
