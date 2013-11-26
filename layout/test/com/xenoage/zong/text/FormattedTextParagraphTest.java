package com.xenoage.zong.text;

import static com.xenoage.zong.text.FormattedTextParagraph.fPara;
import static com.xenoage.zong.text.FormattedTextString.fString;
import static org.junit.Assert.assertEquals;

import javax.swing.JLabel;

import org.junit.Test;

import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.graphics.font.FontInfo;
import com.xenoage.utils.graphics.font.FontStyle;
import com.xenoage.utils.pdlib.PVector;


/**
 * Test cases for a {@link FormattedTextParagraph}
 * 
 * @author Andreas Wenger
 */
public class FormattedTextParagraphTest
{
	
	
	@Test public void testSimpleTextParagraph()
  {
    FormattedTextParagraph paragraph = fPara(
    	fString("This is a simple text.", FormattedTextStyle.defaultStyle));
    assertEquals("This is a simple text.", paragraph.getText());
  }
	
	
	@Test public void testFormattedTextParagraph()
  {
    FormattedTextStyle style = new FormattedTextStyle(
    	new FontInfo((String) null, null, FontStyle.create(FontStyle.Bold)), Color.red, Superscript.Super);
    FormattedTextParagraph paragraph = fPara(fString("This is a formatted text.", style));
    assertEquals("This is a formatted text.", paragraph.getText());
    style = ((FormattedTextString) paragraph.elements.getFirst()).getStyle();
    assertEquals(true, style.fontInfo.getStyle().isSet(FontStyle.Bold));
    assertEquals(false, style.fontInfo.getStyle().isSet(FontStyle.Italic));
    assertEquals(false, style.fontInfo.getStyle().isSet(FontStyle.Underline));
    assertEquals(false, style.fontInfo.getStyle().isSet(FontStyle.Strikethrough));
    assertEquals(Color.red, style.color);
    assertEquals(Superscript.Super, style.superscript);
  }
	
	
	@Test public void testMixedStyledTextParagraph()
  {
    FormattedTextParagraph text = getMixedStyleTextParagraph();
    assertEquals("This is a mixed styled text!", text.getText());
    PVector<FormattedTextElement> elements = text.elements;
    FormattedTextStyle style = ((FormattedTextString) elements.get(0)).getStyle();
    assertEquals(true, style.fontInfo.getStyle().isSet(FontStyle.Italic));
    assertEquals(true, style.fontInfo.getStyle().isSet(FontStyle.Underline));
    assertEquals(14, style.getFontAWT().getSize());
    style = ((FormattedTextString) elements.get(1)).getStyle();
    assertEquals(true, style.fontInfo.getStyle().isSet(FontStyle.Bold));
    assertEquals(true, style.fontInfo.getStyle().isSet(FontStyle.Strikethrough));
    assertEquals(Color.green, style.color);
  }

	
	public static FormattedTextParagraph getMixedStyleTextParagraph()
  {
    FormattedTextStyle style1 = new FormattedTextStyle(
    	new FontInfo(new JLabel().getFont().getName(), 14f,
    		FontStyle.create(FontStyle.Italic, FontStyle.Underline)));
    FormattedTextStyle style2 = new FormattedTextStyle(
    	new FontInfo(new JLabel().getFont().getName(), 14f,
    		FontStyle.create(FontStyle.Bold, FontStyle.Italic, FontStyle.Underline, FontStyle.Strikethrough)),
    		Color.green, null);
    FormattedTextParagraph paragraph = new FormattedTextParagraph(
    	new PVector<FormattedTextElement>(
    		new FormattedTextString("This is ", style1),
    		new FormattedTextString("a mixed styled text!", style2)), FormattedTextParagraph.defaultAlignment);
    return paragraph;
  }
	
	
	@Test public void lineBreakTest()
  {
  	FormattedTextStyle style = new FormattedTextStyle(new FontInfo("Arial", 72f, null));
  	float widthW = new FormattedTextString("w", style).getWidth();
  	float widthMinus = new FormattedTextString("-", style).getWidth();
  	float widthSpace = new FormattedTextString(" ", style).getWidth();
  	
  	// | : where the line break should happen
  	
  	//1st test: "www|ww"
  	FormattedTextParagraph paragraph = fPara(fString("wwwww", style));
  	float width = 3.2f * widthW;
  	PVector<FormattedTextParagraph> lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("www", lines.get(0).getText());
  	assertEquals("ww", lines.get(1).getText());
  	
  	//2nd test: "ww-|www"
  	paragraph = fPara(fString("ww-www", style));
  	width = 3.2f * widthW + widthMinus;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("ww-", lines.get(0).getText());
  	assertEquals("www", lines.get(1).getText());
  	
  	//3rd test: like 2nd test, but with more elements: "w"+"w-|w"+"ww"
  	paragraph = new FormattedTextParagraph(new PVector<FormattedTextElement>(
  		new FormattedTextString("w", style),
  		new FormattedTextString("w-w", style),
  		new FormattedTextString("ww", style)), FormattedTextParagraph.defaultAlignment);
  	width = 3.2f * widthW + widthMinus;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("ww-", lines.get(0).getText());
  	assertEquals("www", lines.get(1).getText());
  	assertEquals(2, lines.get(0).elements.size());
  	assertEquals(2, lines.get(1).elements.size());
  	
  	//4th test: break at the right position: "www www |www"
  	paragraph = fPara(fString("www www www", style));
  	width = 7.2f * widthW + 2 * widthSpace;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("www www ", lines.get(0).getText());
  	assertEquals("www", lines.get(1).getText());
  	
    //5th test: "w|w"
  	paragraph = fPara(fString("ww", style));
  	width = 1.2f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("w", lines.get(0).getText());
  	assertEquals("w", lines.get(1).getText());
  	
    //5th test: "ww", but not even enough space for one w
  	paragraph = fPara(fString("ww", style));
  	width = 0.8f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(0, lines.size());
  	
    //6th test: ". |www"
  	paragraph = fPara(fString(". www", style));
  	width = 3.1f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals(". ", lines.get(0).getText());
  	assertEquals("www", lines.get(1).getText());
  	
  	//7th test: " |www|ww "
  	paragraph = fPara(fString(" wwwww ", style));
  	width = 3.05f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(3, lines.size());
  	assertEquals(" ", lines.get(0).getText());
  	assertEquals("www", lines.get(1).getText());
  	assertEquals("ww ", lines.get(2).getText());
  	
  	//8th test: "ww www  w" -> "w|w |w|w|w  |w"
  	paragraph = fPara(fString("ww www  w", style));
  	width = 1.1f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(6, lines.size());
  	assertEquals("w", lines.get(0).getText());
  	assertEquals("w ", lines.get(1).getText());
  	assertEquals("w", lines.get(2).getText());
  	assertEquals("w", lines.get(3).getText());
  	assertEquals("w  ", lines.get(4).getText());
  	assertEquals("w", lines.get(5).getText());
  	
  	//9th test: like 8th test, but using several elements
  	paragraph = new FormattedTextParagraph(new PVector<FormattedTextElement>(
  		new FormattedTextString("w", style),
  		new FormattedTextString("w ", style),
  		new FormattedTextString("ww", style),
  		new FormattedTextString("w  w", style)), FormattedTextParagraph.defaultAlignment);
  	width = 1.1f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(6, lines.size());
  	assertEquals("w", lines.get(0).getText());
  	assertEquals("w ", lines.get(1).getText());
  	assertEquals("w", lines.get(2).getText());
  	assertEquals("w", lines.get(3).getText());
  	assertEquals("w  ", lines.get(4).getText());
  	assertEquals("w", lines.get(5).getText());
  	
    //10th test: "ww |ww"
  	paragraph = fPara(fString("ww ww", style));
  	width = 2.1f * widthW;
  	lines = paragraph.lineBreak(width);
  	assertEquals(2, lines.size());
  	assertEquals("ww ", lines.get(0).getText());
  	assertEquals("ww", lines.get(1).getText());
  	
  }
	

}
