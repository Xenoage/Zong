package com.xenoage.zong.core.music.lyric;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.text.Text;


/**
 * This class represents a single syllable of lyrics.
 * 
 * @author Andreas Wenger
 */
@EqualsAndHashCode(exclude="parent")
public class Lyric
	implements TextElement {

	/** The text. Usually a single syllable. Or null, if type is {@link SyllableType#Extend}. */
	@Getter private Text text;
	/** The position of the syllable within the lyrics. */
	@Getter @NonNull private SyllableType syllableType;
	/** The verse number. 0 is the first one. */
	@Getter @Setter private int verse;
	
	/** Back reference: The parent element, or null, if not attached to an element. */
	@Getter @Setter private VoiceElement parent;


	public Lyric(Text text, SyllableType syllableType, int verse) {
		checkArgsNotNull(syllableType);
		if (syllableType != SyllableType.Extend)
			checkArgsNotNull(text);
		this.text = text;
		this.syllableType = syllableType;
		this.verse = verse;
	}


	/**
	 * Creates a new extend lyric.
	 */
	public static Lyric lyricExtend(int verse) {
		return new Lyric(null, SyllableType.Extend, verse);
	}


	@Override public void setText(Text text) {
		if (syllableType != SyllableType.Extend)
			checkArgsNotNull(text);
		this.text = text;
	}

	

	@Override public String toString() {
		return "Lyric (\"" + text + "\")";
	}


}
