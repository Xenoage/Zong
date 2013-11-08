package com.xenoage.zong.core.music.direction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.text.Text;


/**
 * Direction words, that are not interpreted by this program.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class Words
	extends Direction
	implements TextElement {

	/** The text. */
	@Getter @Setter @NonNull private Text text;

}
