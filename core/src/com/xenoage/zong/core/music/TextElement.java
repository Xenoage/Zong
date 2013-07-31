package com.xenoage.zong.core.music;

import com.xenoage.zong.core.text.Text;


/**
 * Interface for all musical elements that have a text.
 *
 * @author Andreas Wenger
 */
public interface TextElement
	extends MusicElement {

	/**
	 * Gets the text of this element.
	 */
	public Text getText();


	/**
	 * Sets the text of this element.
	 */
	public void setText(Text text);

}
