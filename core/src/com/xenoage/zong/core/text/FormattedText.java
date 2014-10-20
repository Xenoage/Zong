package com.xenoage.zong.core.text;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.zong.core.text.FormattedTextParagraph.fPara;
import static com.xenoage.zong.core.text.FormattedTextString.fString;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Class for formatted text.
 * 
 * A formatted text contains a list of paragraphs
 * with multi-styled text.
 *
 * @author Andreas Wenger
 */
@Const @EqualsAndHashCode public class FormattedText
	implements Text {
	
	/** An empty text. */
	public static final FormattedText empty = fText(); 

	/** The list of paragraphs */
	@Getter private final IList<FormattedTextParagraph> paragraphs;

	//cache
	private transient float width = Float.NaN;
	private transient float height = Float.NaN;


	/**
	 * Creates a {@link FormattedText} with the given paragraphs.
	 */
	public FormattedText(IList<FormattedTextParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	/**
	 * Creates an empty {@link FormattedText}.
	 */
	public static FormattedText fText() {
		return new FormattedText(CList.<FormattedTextParagraph>ilist());
	}

	/**
	 * Creates a {@link FormattedText} with the given paragraphs.
	 */
	public static FormattedText fText(IList<FormattedTextParagraph> paragraphs) {
		return new FormattedText(paragraphs);
	}

	/**
	 * Creates a {@link FormattedText} with the given paragraph.
	 */
	public static FormattedText fText(FormattedTextParagraph paragraph) {
		return new FormattedText(ilist(paragraph));
	}

	/**
	 * Creates a {@link FormattedText} with a single paragraph, using
	 * the given style and alignment.
	 */
	public static FormattedText fText(String text, FormattedTextStyle style, Alignment alignment) {
		return fText(fPara(fString(text, style), alignment));
	}

	/**
	 * Breaks this formatted text up so that it fits into the given width
	 * and returns the result.
	 * The result is no deep copy of the whole text, instead references to the
	 * unmodified parts are used.
	 */
	public FormattedText lineBreak(float width) {
		CList<FormattedTextParagraph> ret = clist();
		//break up paragraphs
		for (FormattedTextParagraph paragraph : paragraphs) {
			for (FormattedTextParagraph paragraphLine : paragraph.lineBreak(width)) {
				ret.add(paragraphLine);
			}
		}
		return new FormattedText(ret.close());
	}

	@Override public String toString() {
		StringBuilder ret = new StringBuilder();
		for (FormattedTextParagraph paragraph : paragraphs) {
			ret.append(paragraph.toString());
			ret.append('\n');
		}
		ret.delete(ret.length() - 1, ret.length()); //remove last '\n'
		return ret.toString();
	}

	/**
	 * Gets the width of this text in mm (without automatic line breaks).
	 */
	public float getWidth() {
		if (Float.isNaN(width)) {
			float maxWidth = 0;
			for (FormattedTextParagraph paragraph : paragraphs) {
				maxWidth = Math.max(maxWidth, paragraph.getMetrics().getWidth());
			}
			width = maxWidth;
		}
		return width;
	}

	/**
	 * Gets the height of this text in mm (without automatic line breaks).
	 */
	public float getHeight() {
		if (Float.isNaN(height)) {
			float sumHeight = 0;
			for (FormattedTextParagraph paragraph : paragraphs) {
				sumHeight += paragraph.getHeightMm();
			}
			height = sumHeight;
		}
		return height;
	}

	/**
	 * Gets the bounding rectangle of the text.
	 */
	public Rectangle2f getBoundingRect() {
		float w = getWidth();
		float h = getHeight();
		float x = 0;
		FormattedTextParagraph p = getFirstParagraph();
		switch (p.getAlignment()) {
			case Center:
				x -= w / 2;
				break;
			case Right:
				x -= w;
				break;
			default:
				break;
		}
		float y = -1 * p.getMetrics().getAscent();
		return new Rectangle2f(x, y, w, h);
	}

	/**
	 * Gets the first paragraph, or throws an {@link IndexOutOfBoundsException}
	 * if there is none.
	 */
	public FormattedTextParagraph getFirstParagraph() {
		return paragraphs.getFirst();
	}

	@Override public int getLength() {
		int ret = 0;
		for (FormattedTextParagraph paragraph : paragraphs) {
			ret += paragraph.getLength();
		}
		ret += (paragraphs.size() - 1); //\n as paragraph separator
		return ret;
	}

}
