package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.text.Text;


/**
 * Direction words, that are not interpreted by this program.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Words
	extends Direction
	implements TextElement {

	/** The text. */
	@NonNull private Text text;

}
