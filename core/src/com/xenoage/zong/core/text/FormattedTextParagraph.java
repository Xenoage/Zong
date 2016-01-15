package com.xenoage.zong.core.text;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.llist;

import java.util.LinkedList;

import lombok.Data;

import com.xenoage.utils.StringUtils;
import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.font.TextMetrics;

/**
 * One paragraph within a {@link FormattedText}.
 * 
 * A paragraph is a String with the following attributes for substrings: font
 * name, size, color, bold, italic, underline, strikethrough,
 * superscript/subscript.
 * 
 * @author Andreas Wenger
 */
@Const @Data public class FormattedTextParagraph {

	/** The elemens of this paragraph */
	private final IList<FormattedTextElement> elements;
	/** The horizontal alignment of this paragraph */
	private final Alignment alignment;
	
	/** The measurements of this paragraph. */
	private final TextMetrics metrics;

	public static final Alignment defaultAlignment = Alignment.Left;


	/**
	 * Creates a new {@link FormattedTextParagraph} with the given elements and alignment.
	 */
	public FormattedTextParagraph(IList<FormattedTextElement> elements, Alignment alignment) {
		this.elements = elements;
		this.alignment = alignment;
		//compute ascent, descent and leading
		float ascent = 0;
		float descent = 0;
		float leading = 0;
		float width = 0;
		for (FormattedTextElement e : elements) {
			TextMetrics m = e.getMetrics();
			ascent = Math.max(ascent, m.getAscent());
			descent = Math.max(descent, m.getDescent());
			leading = Math.max(leading, m.getLeading());
			width += m.getWidth();
		}
		this.metrics = new TextMetrics(ascent, descent, leading, width);
	}

	/**
	 * Creates a new {@link FormattedTextParagraph} with the given element.
	 */
	public static FormattedTextParagraph fPara(FormattedTextElement element) {
		return new FormattedTextParagraph(ilist(element), defaultAlignment);
	}

	/**
	 * Creates a new {@link FormattedTextParagraph} with the given element and alignment.
	 */
	public static FormattedTextParagraph fPara(FormattedTextElement element, Alignment alignment) {
		return new FormattedTextParagraph(ilist(element), alignment);
	}

	/**
	 * Creates a new {@link FormattedTextParagraph} with the given elements and alignment.
	 */
	public static FormattedTextParagraph fPara(IList<FormattedTextElement> elements,
		Alignment alignment) {
		return new FormattedTextParagraph(elements, alignment);
	}

	/**
	 * Gets the plain text.
	 */
	public String getText() {
		StringBuilder ret = new StringBuilder();
		for (FormattedTextElement e : elements)
			ret.append(e.getText());
		return "" + ret;
	}

	public int getLength() {
		int ret = 0;
		for (FormattedTextElement e : elements)
			ret += e.getLength();
		return ret;
	}

	/**
	 * Gets the height of this paragraph in mm.
	 */
	public float getHeightMm() {
		float maxAscent = 0;
		float maxDescentAndLeading = 0;
		for (FormattedTextElement element : elements) {
			TextMetrics m = element.getMetrics();
			maxAscent = Math.max(maxAscent, m.getAscent());
			maxDescentAndLeading = Math.max(maxDescentAndLeading, m.getDescent() + m.getLeading());
		}
		return maxAscent + maxDescentAndLeading;
	}

	/**
	 * Breaks this paragraph up into one or more lines so
	 * that it fits into the given width and returns the result.
	 */
	public IList<FormattedTextParagraph> lineBreak(float width) {
		CList<FormattedTextParagraph> ret = clist();
		if (elements.size() == 0) {
			//nothing to break up
			ret.add(this);
		}
		else {
			//queue with elements still to format
			LinkedList<FormattedTextElement> queue = llist();
			queue.addAll(elements);
			//loop through all elements
			while (!queue.isEmpty()) {
				//create list to collect elements for current line
				LinkedList<FormattedTextElement> line = new LinkedList<FormattedTextElement>();
				float lineWidth = 0;
				//add elements to the line until width is reached
				do {
					FormattedTextElement currentElement = queue.removeFirst();
					line.add(currentElement);
					lineWidth += currentElement.getMetrics().getWidth();
				} while (lineWidth <= width && !queue.isEmpty());
				//line too wide?
				if (lineWidth > width) {
					//yes. we have to do a line break.
					//for string elements, we can divide the text and put one part in this
					//line and the other part into the next one. for symbols, we always begin a new line.
					if (line.getLast() instanceof FormattedTextString) {
						FormattedTextString lastElement = (FormattedTextString) line.getLast();
						float lineWidthBeforeLineBreak = lineWidth;
						//first test if line is wide enough for at least one character (if it is a String)
						//or the whole symbol (if it is a symbol)
						FormattedTextString firstCharElement = new FormattedTextString(lastElement.getText()
							.substring(0, 1), lastElement.getStyle());
						if (firstCharElement.getMetrics().getWidth() > width) {
							//not enough space for even one character. return empty list.
							return ilist();
						}
						//spacing character within this line?
						boolean lineBreakSuccess = false;
						if (containsLineBreakCharacter(line)) {
							//go back until the last spacing character.
							//search for element to cut
							FormattedTextString searchCuttedElement = (FormattedTextString) line.removeLast();
							lineWidth -= searchCuttedElement.getMetrics().getWidth();
							int queuedElementsCount = 0;
							while (!StringUtils.containsLineBreakCharacter(searchCuttedElement.getText())) {
								queue.addFirst(searchCuttedElement);
								queuedElementsCount++;
								searchCuttedElement = (FormattedTextString) line.removeLast();
								lineWidth -= searchCuttedElement.getMetrics().getWidth();
							}
							FormattedTextString cuttedElement = searchCuttedElement;
							//find last spacing character that fits into the given width and split the
							//cuttedElement there
							FormattedTextString forThisLine = null;
							FormattedTextString forThisLineTrimRight = null;
							for (int i = cuttedElement.getText().length() - 1; i >= 0; i--) {
								char c = cuttedElement.getText().charAt(i);
								if (StringUtils.isLineBreakCharacter(c)) {
									forThisLine = new FormattedTextString(
										cuttedElement.getText().substring(0, i + 1), cuttedElement.getStyle());
									//ignore spaces at the end
									String forThisLineTrimRightText = StringUtils.trimRight(forThisLine.getText());
									forThisLineTrimRight = (forThisLineTrimRightText.length() > 0 ? new FormattedTextString(
										forThisLineTrimRightText, forThisLine.getStyle()) : null);
									if (forThisLineTrimRight == null ||
										lineWidth + forThisLineTrimRight.getMetrics().getWidth() <= width) {
										break;
									}
								}
							}
							//if the left side of the cutted line is now short enough to fit into the width, we had
							//success and apply the linebreak. otherwise we must do a linebreak in the middle of a word.
							if (forThisLineTrimRight == null ||
								lineWidth + forThisLineTrimRight.getMetrics().getWidth() <= width) {
								lineBreakSuccess = true;
								//complete this line
								line.add(forThisLine);
								ret.add(new FormattedTextParagraph(ilist(line), alignment));
								//begin next line
								if (forThisLine.getText().length() < cuttedElement.getText().length()) {
									FormattedTextString forNextLine = new FormattedTextString(cuttedElement.getText()
										.substring(forThisLine.getText().length()), cuttedElement.getStyle());
									queue.addFirst(forNextLine);
								}
							}
							else {
								lineBreakSuccess = false;
								lineWidth = lineWidthBeforeLineBreak;
								//restore original line and queue
								for (int i = 0; i < queuedElementsCount; i++) {
									line.addLast(queue.removeFirst());
								}
								line.addLast(cuttedElement);
							}
						}
						if (!lineBreakSuccess) {
							//there is no spacing character in this line or we had no success using it,
							//so we have to do a linebreak in the middle of a word.
							//since we have added element after element, the possible line break must be
							//within the last element.
							FormattedTextString cuttedElement = (FormattedTextString) line.removeLast();
							lineWidth -= cuttedElement.getMetrics().getWidth();
							FormattedTextString forThisLine = null;
							for (int i = cuttedElement.getText().length() - 1; i >= -1; i--) {
								if (i >= 0) {
									forThisLine = new FormattedTextString(
										cuttedElement.getText().substring(0, i + 1), cuttedElement.getStyle());
									//ignore spaces at the end
									String forThisLineTrimRightText = StringUtils.trimRight(forThisLine.getText());
									FormattedTextString forThisLineTrimRight = (forThisLineTrimRightText.length() > 0 ?
										new FormattedTextString(forThisLineTrimRightText, forThisLine.getStyle()) : null);
									if (forThisLineTrimRight == null ||
										lineWidth + forThisLineTrimRight.getMetrics().getWidth() <= width) {
										break;
									}
								}
								else {
									forThisLine = null;
								}
							}
							//complete this line
							if (forThisLine != null) {
								line.add(forThisLine);
							}
							ret.add(new FormattedTextParagraph(ilist(line), alignment));
							//begin next line
							if (forThisLine == null ||
								forThisLine.getText().length() < cuttedElement.getText().length()) {
								FormattedTextString forNextLine = new FormattedTextString(
									(forThisLine != null ? cuttedElement.getText().substring(
										forThisLine.getText().length()) : cuttedElement.getText()),
									cuttedElement.getStyle());
								queue.addFirst(forNextLine);
							}
						}
					}
					else if (line.getLast() instanceof FormattedTextSymbol) {
						if (line.size() > 1) {
							//at least two elements, so one can be placed in this line
							//move symbol into next line
							FormattedTextElement symbol = line.removeLast();
							ret.add(new FormattedTextParagraph(ilist(line), alignment));
							//begin next line
							queue.addFirst(symbol);
						}
						else {
							//not enough space for even one symbol. return empty list.
							return ilist();
						}
					}
				}
				else {
					//no. we can use exactly that line. that means, this was
					//the last line and we are finished.
					ret.add(new FormattedTextParagraph(ilist(line), alignment));
					break;
				}
			}
		}
		return ilist(ret.close());
	}

	/**
	 * Returns true, if a line break character is found in the given list
	 * of formatted text elements, but not at the end (trailing spaces are ignored).
	 */
	private boolean containsLineBreakCharacter(LinkedList<FormattedTextElement> elements) {
		for (FormattedTextElement element : elements) {
			if (element != elements.getLast()) {
				if (StringUtils.containsLineBreakCharacter(element.getText()))
					return true;
			}
			else {
				return StringUtils.containsLineBreakCharacter(StringUtils.trimRight(element.getText()));
			}
		}
		return false;
	}

	@Override public String toString() {
		StringBuilder ret = new StringBuilder();
		for (FormattedTextElement element : elements) {
			ret.append(element.toString());
		}
		return ret.toString();
	}

}
