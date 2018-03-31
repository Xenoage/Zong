package com.xenoage.utils.font;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CList.ilist;

import java.util.List;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.IList;

/**
 * Information about a font.
 * 
 * This class allows to save multiple font names/families and allows
 * the attributes to be unset. It is independent of a specific API like
 * AWT or Android, and can thus be used on each device.
 * 
 * @author Andreas Wenger
 */
@Const public final class FontInfo {

	private final IList<String> families;
	private final Float size;
	private final FontStyle style;

	public static final IList<String> defaultFamilies = ilist("Times New Roman", "Linux Libertine",
		"Times");
	public static final float defaultSize = 12;
	public static final FontStyle defaultFontStyle = FontStyle.normal;
	public static final FontInfo defaultValue = new FontInfo(defaultFamilies, defaultSize, defaultFontStyle);


	/**
	 * Creates a new {@link FontInfo} with the given attributes.
	 */
	public FontInfo(@MaybeNull @MaybeEmpty IList<String> families, @MaybeNull Float size,
		@MaybeNull FontStyle style) {
		this.families = (families != null && families.size() > 0 ? families : null);
		this.size = size;
		this.style = style;
	}

	/**
	 * Creates a new {@link FontInfo} with the given attributes.
	 */
	public FontInfo(@MaybeNull String family, @MaybeNull Float size, @MaybeNull FontStyle style) {
		this(family != null ? ilist(family) : null, size, style);
	}

	/** 
	 * The list of families, or the default families if unset.
	 * The first entry is the preferred font, the alternative fonts can be found at the following entries.
	 */
	@NonEmpty public List<String> getFamilies() {
		return notNull(families, defaultFamilies);
	}

	/**
	 * The size of the font in pt, or the default size if unset.
	 */
	public float getSize() {
		return notNull(size, defaultSize);
	}

	/**
	 * The style of the font, or the default style if unset.
	 */
	@NonNull public FontStyle getStyle() {
		return notNull(style, defaultFontStyle);
	}

	/**
	 * The list of families, or null if unset.
	 */
	@MaybeNull public List<String> getFamiliesOrNull() {
		return families;
	}

	/**
	 * The size of the font in pt, or null if unset.
	 */
	@MaybeNull public Float getSizeOrNull() {
		return size;
	}

	/**
	 * The style of the font, or null if unset.
	 */
	@MaybeNull public FontStyle getStyleOrNull() {
		return style;
	}

	@Override public int hashCode() { //auto-generated
		final int prime = 31;
		int result = 1;
		result = prime * result + ((families == null) ? 0 : families.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) { //auto-generated
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontInfo other = (FontInfo) obj;
		if (families == null) {
			if (other.families != null)
				return false;
		}
		else if (!families.equals(other.families))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		}
		else if (!size.equals(other.size))
			return false;
		if (style == null) {
			if (other.style != null)
				return false;
		}
		else if (!style.equals(other.style))
			return false;
		return true;
	}

}
