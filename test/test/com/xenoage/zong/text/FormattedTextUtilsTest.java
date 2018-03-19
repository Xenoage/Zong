package com.xenoage.zong.text;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextParagraph.fPara;
import static com.xenoage.zong.core.text.FormattedTextUtils.insert;
import static com.xenoage.zong.core.text.FormattedTextUtils.merge;
import static com.xenoage.zong.core.text.FormattedTextUtils.split;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.FormattedTextUtils;

/**
 * Tests for {@link FormattedTextUtils}.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextUtilsTest {

	@Test public void cleanTest() {
		FormattedText text = createText1Para();
		FormattedText textCleaned = FormattedTextUtils.clean(text);
		assertEquals(1, textCleaned.getParagraphs().size());
		assertEquals(2, textCleaned.getParagraphs().getFirst().getElements().size());
		assertEquals(new FormattedTextString("Hallo ", text.getParagraphs().getFirst().getElements().get(0)
			.getStyle()), textCleaned.getParagraphs().getFirst().getElements().get(0));
		assertEquals(new FormattedTextString("Andrea", text.getParagraphs().getFirst().getElements().get(1)
			.getStyle()), textCleaned.getParagraphs().getFirst().getElements().get(1));
	}

	@Test public void mergeTest() {
		//test case: merge some texts and see if correct strings are produced
		FormattedText text1 = createText1Para();
		FormattedText text2 = createText3Paras();
		assertEquals("Hallo AndreaHallo Andrea", merge(text1, text1).toString());
		assertEquals("Hallo AndreaFirst Line\nSecond Line and\na Third Line", merge(text1, text2)
			.toString());
	}

	@Test public void splitTest1Para() {
		//test case: split a single line text and see if correct strings are produced
		FormattedText text = createText1Para();
		//"Hallo A|ndrea"
		Tuple2<FormattedText, FormattedText> textSplit = split(text, 7);
		assertEquals("Hallo A", textSplit.get1().toString());
		assertEquals("ndrea", textSplit.get2().toString());
		//"Hallo An|drea"
		textSplit = split(text, 8);
		assertEquals("Hallo An", textSplit.get1().toString());
		assertEquals("drea", textSplit.get2().toString());
	}

	@Test public void splitTest3Paras() {
		//test case: split a text with multiple lines and see if correct strings are produced
		FormattedText text = createText3Paras();
		//split after first line
		Tuple2<FormattedText, FormattedText> textSplit = split(text, 10);
		assertEquals("First Line", textSplit.get1().toString());
		assertEquals("\nSecond Line and\na Third Line", textSplit.get2().toString());
		//split at beginning of second line
		textSplit = split(text, 11);
		assertEquals("First Line\n", textSplit.get1().toString());
		assertEquals("Second Line and\na Third Line", textSplit.get2().toString());
		//split in the middle of the first line
		textSplit = split(text, 3);
		assertEquals("Fir", textSplit.get1().toString());
		assertEquals("st Line\nSecond Line and\na Third Line", textSplit.get2().toString());
		//split in the middle of the second line
		textSplit = split(text, 14);
		assertEquals("First Line\nSec", textSplit.get1().toString());
		assertEquals("ond Line and\na Third Line", textSplit.get2().toString());
		//split in the middle of the third line
		textSplit = split(text, 30);
		assertEquals("First Line\nSecond Line and\na T", textSplit.get1().toString());
		assertEquals("hird Line", textSplit.get2().toString());
		//split at the very beginning
		textSplit = split(text, 0);
		assertEquals("", textSplit.get1().toString());
		assertEquals("First Line\nSecond Line and\na Third Line", textSplit.get2().toString());
		//split at the very end
		textSplit = split(text, 39);
		assertEquals("First Line\nSecond Line and\na Third Line", textSplit.get1().toString());
		assertEquals("", textSplit.get2().toString());
	}

	@Test public void splitTestExceptions() {
		//test case: split at all possible positions
		//(problems are only discovered by exceptions)
		FormattedText text1 = createText1Para();
		for (int i : range(text1.toString().length() + 1))
			split(text1, i);
		FormattedText text2 = createText3Paras();
		for (int i : range(text2.toString().length() + 1))
			split(text2, i);
	}

	@Test public void insertElementTest1() {
		//test case: insert element with same style inbetween an element
		FormattedText text = createText1Para();
		FormattedTextString insertText = new FormattedTextString("ndi und A",
			text.getParagraphs().getFirst().getElements().get(1).getStyle()); //same style
		//insert at index 7: "Hallo A{ndi und A}ndrea"
		FormattedText textInsert = insert(text, 7, insertText);
		assertEquals(1, textInsert.getParagraphs().size());
		assertEquals(2, textInsert.getParagraphs().getFirst().getElements().size());
		assertEquals(new FormattedTextString("Hallo ", text.getParagraphs().getFirst().getElements().get(0)
			.getStyle()), textInsert.getParagraphs().getFirst().getElements().get(0));
		assertEquals(new FormattedTextString("Andi und Andrea", text.getParagraphs().getFirst().getElements()
			.get(1).getStyle()), textInsert.getParagraphs().getFirst().getElements().get(1));
	}

	@Test public void insertElementTest2() {
		//test case: insert element with different style at the end of an element
		FormattedText text = createText1Para();
		FormattedTextString insertText = new FormattedTextString("di und An",
			text.getParagraphs().getFirst().getElements().get(0).getStyle()); //different style
		//insert at index 8: "Hallo An{di und An}drea"
		FormattedText textInsert = insert(text, 8, insertText);
		assertEquals(1, textInsert.getParagraphs().size());
		assertEquals(4, textInsert.getParagraphs().getFirst().getElements().size());
		assertEquals(new FormattedTextString("Hallo ", text.getParagraphs().getFirst().getElements().get(0)
			.getStyle()), textInsert.getParagraphs().getFirst().getElements().get(0));
		assertEquals(new FormattedTextString("An", text.getParagraphs().getFirst().getElements().get(1)
			.getStyle()), textInsert.getParagraphs().getFirst().getElements().get(1));
		assertEquals(new FormattedTextString("di und An", text.getParagraphs().getFirst().getElements().get(0)
			.getStyle()), textInsert.getParagraphs().getFirst().getElements().get(2));
		assertEquals(new FormattedTextString("drea", text.getParagraphs().getFirst().getElements().get(1)
			.getStyle()), textInsert.getParagraphs().getFirst().getElements().get(3));
	}

	@Test public void insertTextTest() {
		//test case: insert multiline text into multiline text
		FormattedText text = createText3Paras();
		//insert at index 14: "First Line\nSecFirst Line\nSecond Li..."
		FormattedText textInsert = insert(text, 14, text);
		assertEquals(
			"First Line\nSecFirst Line\nSecond Line and\na Third Lineond Line and\na Third Line",
			textInsert.toString());
		assertEquals(5, textInsert.getParagraphs().size());
	}

	private FormattedText createText1Para() {
		//create a formatted text with one paragraph and three adjacent strings
		//with the same style: "Hallo "{style1}, "An"{style2}, "dr"{style2}"ea"{style2}
		FormattedTextStyle style1 = new FormattedTextStyle(12);
		FormattedTextStyle style2 = new FormattedTextStyle(14, Color.Companion.getBlue());
		return fText(fPara(CList.<FormattedTextElement>ilist(new FormattedTextString("Hallo ", style1),
			new FormattedTextString("An", style2), new FormattedTextString("dr", style2),
			new FormattedTextString("ea", style2)), Alignment.Left));
	}

	private FormattedText createText3Paras() {
		//create a formatted text with three paragraphs (styles
		//enclosed by "{}"):
		//"{First }{Line"
		//"Second Line and"
		//"a }{Third}{ Line}"
		FormattedTextStyle style1 = new FormattedTextStyle(12);
		FormattedTextStyle style2 = new FormattedTextStyle(14, Color.Companion.getBlue());
		return fText(ilist(
			fPara(CList.<FormattedTextElement>ilist(new FormattedTextString("First ", style1),
				new FormattedTextString("Line", style2)), Alignment.Left),
			fPara(CList.<FormattedTextElement>ilist(new FormattedTextString("Second Line and", style2)),
				Alignment.Left),
			fPara(CList.<FormattedTextElement>ilist(new FormattedTextString("a ", style2),
				new FormattedTextString("Third", style1), new FormattedTextString(" Line", style2)),
				Alignment.Left)));
	}

}
