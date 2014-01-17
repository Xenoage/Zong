package com.xenoage.utils.font.android;

import com.xenoage.utils.graphics.font.FontInfo;
import com.xenoage.utils.graphics.font.TextMeasurerFactory;


/**
 * This class provides a factory for creating {@link AndroidTextMeasurer}s.
 * 
 * @author Andreas Wenger
 */
public final class AndroidTextMeasurerFactory
	implements TextMeasurerFactory
{
	
	@Override public AndroidTextMeasurer textMeasurer(FontInfo font, String text)
	{
		return new AndroidTextMeasurer(font, text);
	}

}
