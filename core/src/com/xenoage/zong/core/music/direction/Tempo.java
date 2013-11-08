package com.xenoage.zong.core.music.direction;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.text.Text;


/**
 * Class for a tempo direction, like "Andante", "♩ = 120"
 * or "Ziemlich langsam und mit Ausdruck".
 * 
 * The text is optional, but the meaning must be given.
 * 
 * Tempo directions must always be written to the {@link ColumnHeader}.
 *
 * @author Andreas Wenger
 */
public final class Tempo
	extends Direction
	implements TextElement, ColumnElement {

	/** The musical meaning: which beat per minute. */
	@Getter @Setter @NonNull private Fraction baseBeat;
	/** The musical meaning: how many of that beats per minute. */
	@Getter @Setter private int beatsPerMinute;

	/** Custom caption, or null for format "baseBeat = beatsPerMinute". */
	@Getter @Setter private Text text = null;


	public Tempo(Fraction baseBeat, int beatsPerMinute) {
		checkArgsNotNull(baseBeat);
		this.baseBeat = baseBeat;
		this.beatsPerMinute = beatsPerMinute;
	}

}
