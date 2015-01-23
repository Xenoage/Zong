package com.xenoage.zong.desktop.io.musicxml.in;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readAlignment;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.io.musicxml.in.readers.FontInfoReader;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.musicxml.types.MxlCredit;
import com.xenoage.zong.musicxml.types.MxlCreditWords;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent.MxlCreditContentType;
import com.xenoage.zong.musicxml.types.enums.MxlVAlign;

/**
 * This class reads the credit elements of a MusicXML 2.0
 * document and creates {@link TextFrame}s out of them.
 * 
 * @author Andreas Wenger
 */
public final class CreditsReader {

	/**
	 * Reads the top-level credit elements of the given MusicXML 2.0
	 * document, creates {@link TextFrame}s out of them and adds
	 * them to the given {@link Layout}, using the given {@link ScoreFormat}.
	 */
	public static void read(MxlScorePartwise mxlScorePartwise, Layout layout, ScoreFormat scoreFormat) {
		Layout ret = layout;
		//get all credit elements
		for (MxlCredit credit : it(mxlScorePartwise.getScoreHeader().getCredits())) {
			addTextFrame(credit, ret, scoreFormat);
		}
	}

	/**
	 * Adds the given credit element as a {@link TextFrame} to the given {@link Layout}.
	 */
	private static void addTextFrame(MxlCredit credit, Layout layout, ScoreFormat scoreFormat) {
		if (credit.getContent().getCreditContentType() == MxlCreditContentType.CreditWords) {
			MxlCreditWords mxlCreditWords = (MxlCreditWords) credit.getContent();
			//create formatted text
			FormattedText text = createFormattedText(mxlCreditWords, scoreFormat.getLyricFont());
			//compute position (read only the position of the first credit-words element)
			Page firstPage = layout.getPages().get(0);
			float tenths = scoreFormat.getInterlineSpace() / 10;
			MxlFormattedText mxlFirstCreditWords = mxlCreditWords.getItems().get(0);
			MxlPosition mxlPosition = mxlFirstCreditWords.getPrintStyle().getPosition();
			Point2f offsetFromBottomInTenths = mxlPosition.getDefault().or(new Point2f(10f, 10f));
			Point2f position = new Point2f(offsetFromBottomInTenths.x * tenths,
				firstPage.getFormat().getSize().height - offsetFromBottomInTenths.y * tenths);
			//compute size
			//this is the width of the widest paragraph and the height of the sum of all paragraphs
			float maxParagraphWidth = 1; //at least 1 mm
			float sumParagraphsHeight = 1; //at least 1 mm
			for (FormattedTextParagraph paragraph : text.getParagraphs()) {
				maxParagraphWidth = Math.max(maxParagraphWidth, paragraph.getMetrics().getWidth());
				sumParagraphsHeight += paragraph.getHeightMm();
			}
			Size2f size = new Size2f(maxParagraphWidth, sumParagraphsHeight);
			//horizontal alignment:
			//try with halign first, and if not set, use justify
			Alignment alignment = readAlignment(mxlFirstCreditWords.getHAlign());
			if (alignment == null) {
				alignment = readAlignment(mxlFirstCreditWords.getJustify());
			}
			if (alignment == null || alignment == Alignment.Left) {
				position = position.add(size.width / 2, 0);
			}
			else if (alignment == Alignment.Center) {
				//already centered
			}
			else if (alignment == Alignment.Right) {
				position = position.add(-size.width / 2, 0);
			}
			//vertical alignment
			MxlVAlign mxlVAlign = mxlFirstCreditWords.getVAlign();
			if (mxlVAlign == MxlVAlign.Top || mxlVAlign == MxlVAlign.Unknown) {
				position = position.add(0, size.height / 2);
			}
			else if (mxlVAlign == MxlVAlign.Middle) {
				//already centered
			}
			else if (mxlVAlign == MxlVAlign.Bottom || mxlVAlign == MxlVAlign.Baseline) {
				position = position.add(0, -size.height / 2);
			}
			//create and add TextFrame
			TextFrame textFrame = new TextFrame();
			textFrame.setPosition(position);
			textFrame.setSize(size);
			textFrame.setText(text);
			layout.getPages().get(0).addFrame(textFrame);
		}
	}

	/**
	 * Creates a {@link FormattedText} and returns it.
	 */
	private static FormattedText createFormattedText(MxlCreditWords mxlCreditWords,
		FontInfo defaultFont) {
		CList<FormattedTextParagraph> paragraphs = clist();
		CList<FormattedTextElement> elements = clist();
		//iterate through all credit-words elements.
		//currently we are only interested in credit-words elements on page 1.
		Alignment alignment = FormattedTextParagraph.defaultAlignment;
		for (MxlFormattedText mxlFormattedText : mxlCreditWords.getItems()) {
			//read text. if empty, ignore this element
			String textContent = mxlFormattedText.getValue();
			if (textContent.length() == 0)
				continue;
			//apply horizontal alignment, if set, otherwise keep the old value
			Alignment newAlignment = readAlignment(mxlFormattedText.getJustify());
			if (newAlignment != null) {
				alignment = newAlignment;
			}
			//since we use paragraphs but MusicXML doesn't, split
			//the text where there are line breaks.
			String[] textLines = textContent.split("\n");
			boolean endsWithLineBreak = textContent.endsWith("\n");
			//append the first line to the current paragraph, then create
			//new paragraphs for the following lines
			for (int iLine = 0; iLine < textLines.length; iLine++) {
				if (iLine > 0) {
					//not the first line: we have to create a new paragraph
					paragraphs.add(new FormattedTextParagraph(elements.close(), alignment));
					elements = clist();
				}
				//read line
				String textLine = textLines[iLine];
				if (textLine.length() > 0) {
					//font
					FontInfo fontInfo = new FontInfoReader(mxlFormattedText.getPrintStyle().getFont(), defaultFont).read();
					//color
					Color color = mxlFormattedText.getPrintStyle().getColor().getValue();
					//create text element
					FormattedTextElement textElement = new FormattedTextString(textLine,
						new FormattedTextStyle(fontInfo, color, null));
					elements.add(textElement);
				}
			}
			if (endsWithLineBreak) {
				//create a new paragraph
				paragraphs.add(new FormattedTextParagraph(elements.close(), alignment));
				elements = clist();
			}
		}
		//add non-empty paragraph at the end
		if (elements.size() > 0) {
			paragraphs.add(new FormattedTextParagraph(elements.close(), alignment));
		}
		return new FormattedText(paragraphs.close());
	}

}
