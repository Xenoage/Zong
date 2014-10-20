package com.xenoage.zong.core.text;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.PlatformUtils.platformUtils;
import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMetrics;

/**
 * Formatted substring of a text.
 *
 * @author Andreas Wenger
 */
@Const @Data public class FormattedTextString
	implements FormattedTextElement {

	/** The text */
	@NonEmpty private final String text;
	/** The style of the text */
	@NonNull private final FormattedTextStyle style;

	/** The measurements of this substring */
	private transient final TextMetrics metrics;


	public FormattedTextString(String text, FormattedTextStyle style) {
		checkArgsNotNull(text, style);
		//text may not be empty
		if (text.length() == 0)
			throw new IllegalArgumentException("Must contain at least one character.");
		//text may not contain a line break. this must be represented
		//as several FormattedTextParagraphs.
		if (text.indexOf("\n") > -1) {
			//throw new IllegalArgumentException("FormattedTextString may not contain line breaks.");
			//better, ignore it
			text = text.replaceAll("\n", "");
		}
		//text may not contain tabs. tabs are replaced by spaces.
		text = text.replaceAll("\t", " ");

		this.text = text;
		this.style = style;
		
		FontInfo font = style.getFont();
		if (font == null)
			font = FontInfo.defaultValue;
		
		this.metrics = platformUtils().getTextMeasurer().measure(font, text);
	}

	public static FormattedTextString fString(String text, FormattedTextStyle style) {
		return new FormattedTextString(text, style);
	}

	@Override public int getLength() {
		return text.length();
	}

	@Override public String toString() {
		return text;
	}

}
