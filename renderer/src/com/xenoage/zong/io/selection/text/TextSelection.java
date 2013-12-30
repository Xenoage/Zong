package com.xenoage.zong.io.selection.text;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.text.Text;

/**
 * Interface for all kinds of selection within a {@link Text},
 * like a caret or a range of selected text.
 * 
 * @author Andreas Wenger
 */
public interface TextSelection {

	/**
	 * Gets the index of the character, where the selection process started
	 * (thus sorted by time).
	 */
	public int getStartPosition();

	/**
	 * Gets the index of the last character, that was added to the selected range
	 * (thus sorted by time).
	 */
	public int getEndPosition();

	/**
	 * Gets the index of the leftmost selected character (inclusive)
	 * (thus sorted by position).
	 */
	public int getLeftPosition();

	/**
	 * Gets the index of the rightmost selected character (exclusive)
	 * (thus sorted by position).
	 */
	public int getRightPosition();

	/**
	 * Writes the given text into the given text using this selection.
	 * The resulting text and new selection is returned.
	 */
	public Tuple2<Text, TextSelection> write(Text source, Text input);

}
