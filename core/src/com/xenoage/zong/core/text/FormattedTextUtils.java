package com.xenoage.zong.core.text;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextParagraph.fPara;
import static com.xenoage.zong.core.text.FormattedTextString.fString;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.kernel.Tuple2;

/**
 * This class creates {@link FormattedText} instances.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextUtils {

	/**
	 * Creates a {@link FormattedText} from the given {@link Text} instance.
	 * If it is already a {@link FormattedText}, it is returned. Otherwise,
	 * the given default style and alignment is applied.
	 */
	public static FormattedText styleText(Text text, FormattedTextStyle defaultStyle,
		Alignment defaultAlignment) {
		if (text instanceof FormattedText)
			return (FormattedText) text;
		else
			return fText(text.toString(), defaultStyle, defaultAlignment);
	}

	/**
	 * Creates a {@link FormattedText} from the given {@link Text} instance.
	 * If it is already a {@link FormattedText}, it is returned. Otherwise,
	 * the given default style and left alignment is applied.
	 */
	public static FormattedText styleText(Text text, FormattedTextStyle defaultStyle) {
		if (text instanceof FormattedText)
			return (FormattedText) text;
		else
			return fText(text.toString(), defaultStyle, Alignment.Left);
	}

	/**
	 * Merges the given two texts.
	 * The last paragraph of the first text and the first paragraph of the second
	 * text are merged into a single paragraph with the alignment of the
	 * last paragraph of the first text.
	 */
	public static FormattedText merge(FormattedText text1, FormattedText text2) {
		CList<FormattedTextParagraph> ps = clist();
		//first text
		for (int i : range(0, text1.getParagraphs().size() - 2))
			ps.add(text1.getParagraphs().get(i));
		//connection paragraph
		FormattedTextParagraph lastPText1 = text1.getParagraphs().getLast();
		FormattedTextParagraph firstPText2 = text2.getParagraphs().getFirst();
		CList<FormattedTextElement> middleP = clist();
		middleP.addAll(lastPText1.getElements());
		middleP.addAll(firstPText2.getElements());
		ps.add(fPara(middleP.close(), lastPText1.getAlignment()));
		//second text
		for (int i : range(1, text2.getParagraphs().size() - 1))
			ps.add(text2.getParagraphs().get(i));
		return clean(fText(ps.close()));
	}

	/**
	 * Splits the given {@link FormattedText} at the given position.
	 */
	public static Tuple2<FormattedText, FormattedText> split(FormattedText source, int position) {
		int curPos = 0;
		CList<FormattedTextParagraph> ps1 = clist();
		CList<FormattedTextParagraph> ps2 = clist();

		//collect data up to split position
		int iP;
		firstText: for (iP = 0; iP < source.getParagraphs().size(); iP++) {
			FormattedTextParagraph oldP = source.getParagraphs().get(iP);
			for (int iE : range(oldP.getElements())) {
				FormattedTextElement oldE = oldP.getElements().get(iE);
				if (position >= curPos && position <= curPos + oldE.getLength()) {
					//element found. if we are at the beginning or end of the found element, we can reuse it.
					//otherwise, we have to split the found element.
					if (position == curPos) {
						//easy case: at the beginning of an element.
						//just use all elements before this one for the first text
						//and this one and the following ones for the second text
						CList<FormattedTextElement> es1 = clist();
						CList<FormattedTextElement> es2 = clist();
						for (int i : range(oldP.getElements())) {
							if (i < iE)
								es1.add(oldP.getElements().get(i));
							else
								es2.add(oldP.getElements().get(i));
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()));
						ps2.add(fPara(es2.close(), oldP.getAlignment()));
					}
					else if (position == curPos + oldE.getLength()) {
						//easy case: at the end of an element.
						//just use all elements up to this one for the first text
						//and the following ones for the second text
						CList<FormattedTextElement> es1 = clist();
						CList<FormattedTextElement> es2 = clist();
						for (int i : range(oldP.getElements())) {
							if (i <= iE)
								es1.add(oldP.getElements().get(i));
							else
								es2.add(oldP.getElements().get(i));
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()));
						ps2.add(fPara(es2.close(), oldP.getAlignment()));
					}
					else {
						//split element:
						//use all elements before this one and the first part of this one for the first text;
						//and the second part of this one and the following elements for the second text
						int splitPos = position - curPos;
						String splitText = oldE.getText();
						FormattedTextStyle style = oldE.getStyle();
						FormattedTextElement oldEPart1 = fString(splitText.substring(0, splitPos), style);
						FormattedTextElement oldEPart2 = splitText.length() > splitPos ? fString(
							splitText.substring(splitPos, splitText.length()), style) : null;
						//collect elements
						CList<FormattedTextElement> es1 = clist();
						CList<FormattedTextElement> es2 = clist();
						for (int i : range(oldP.getElements())) {
							if (i < iE)
								es1.add(oldP.getElements().get(i));
							else if (i == iE) {
								es1.add(oldEPart1);
								if (oldEPart2 != null)
									es2.add(oldEPart2);
							}
							else
								es2.add(oldP.getElements().get(i));
						}
						ps1.add(fPara(es1.close(), oldP.getAlignment()));
						ps2.add(fPara(es2.close(), oldP.getAlignment()));
					}
					break firstText;
				}
				else {
					//position not yet reached. go on.
					curPos += oldE.getLength();
				}
			}
			ps1.add(oldP);
			curPos += 1; //line break character
		}

		//collect data after split position
		iP++;
		for (; iP < source.getParagraphs().size(); iP++) {
			ps2.add(source.getParagraphs().get(iP));
		}

		return t(fText(ps1.close()), fText(ps2.close()));
	}

	/**
	 * Inserts the given {@link FormattedText} at the given position.
	 */
	public static FormattedText insert(FormattedText source, int position, FormattedText input) {
		Tuple2<FormattedText, FormattedText> sourceSplit = split(source, position);
		return merge(merge(sourceSplit.get1(), input), sourceSplit.get2());
	}

	/**
	 * Inserts the given {@link FormattedTextElement} at the given position.
	 */
	public static FormattedText insert(FormattedText source, int position, FormattedTextElement input) {
		FormattedText tInput = fText(fPara(input));
		return insert(source, position, tInput);
	}

	/**
	 * Inserts the given string at the given position.
	 * The style at the given position is used for the inserted string.
	 */
	public static FormattedText insert(FormattedText source, int position, String input) {
		FormattedTextStyle style = getStyle(source, position);
		return insert(source, position, fString(input, style));
	}

	/**
	 * Gets the style of the given text at the given position.
	 */
	private static FormattedTextStyle getStyle(FormattedText text, int position) {
		//get the style at the insert position
		int curPos = 0;
		It<FormattedTextParagraph> oldPs = it(text.getParagraphs());
		for (FormattedTextParagraph oldP : oldPs) {
			It<FormattedTextElement> oldEs = it(oldP.getElements());
			for (FormattedTextElement oldE : oldEs) {
				if (position > curPos && position <= curPos + oldE.getLength()) {
					return oldE.getStyle();
				}
				else {
					//position not yet reached. go on.
					curPos += oldE.getLength();
				}
			}
			curPos += 1; //line break character
		}
		throw new IndexOutOfBoundsException("Invalid position");
	}

	/**
	 * Cleans the given text by merging adjacent {@link FormattedTextString}s
	 * with the same style.
	 * PERFORMANCE: only copy array if there is really a change
	 */
	public static FormattedText clean(FormattedText text) {
		CList<FormattedTextParagraph> newPs = clist(text.getParagraphs());
		for (int iP : range(newPs)) {
			FormattedTextParagraph oldP = text.getParagraphs().get(iP);
			CList<FormattedTextElement> newEs = clist(oldP.getElements());
			int iE = 0;
			boolean changed = false;
			while (iE < newEs.size() - 1) {
				//look at pairs of adjacent elements
				FormattedTextElement e1 = newEs.get(iE);
				FormattedTextElement e2 = newEs.get(iE + 1);
				if (e1 instanceof FormattedTextString && e2 instanceof FormattedTextString &&
					e1.getStyle().equals(e2.getStyle())) {
					//merge two adjacent strings with the same style
					changed = true;
					newEs.remove(iE + 1);
					newEs.remove(iE);
					newEs.add(iE, new FormattedTextString(e1.getText() + e2.getText(), e1.getStyle()));
				}
				else {
					iE++;
				}
			}
			if (changed)
				newPs.set(iP, new FormattedTextParagraph(newEs, oldP.getAlignment()));
		}
		return new FormattedText(newPs.close());
	}

}
