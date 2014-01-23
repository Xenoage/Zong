package com.xenoage.utils.android;

import static com.xenoage.utils.android.io.AndroidIO.androidIO;

import java.io.IOException;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.android.font.AndroidTextMeasurer;
import com.xenoage.utils.android.xml.AndroidXmlReader;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.xml.XmlReader;

/**
 * Android specific {@link PlatformUtils} implementation.
 * 
 * It is based on the {@link JsePlatformUtils} as far as Android
 * supports the Java SE platform.
 * 
 * @author Andreas Wenger
 */
public class AndroidPlatformUtils
	extends JsePlatformUtils {

	private AndroidTextMeasurer textMeasurer = new AndroidTextMeasurer();

	@Override public TextMeasurer getTextMeasurer() {
		return textMeasurer;
	}
	
	@Override public FilesystemInput getFilesystemInput() {
		return androidIO();
	}

	@Override public InputStream openFile(String filePath)
		throws IOException {
		return androidIO().openFile(filePath);
	}
	
	@Override public XmlReader createXmlReader(InputStream inputStream) {
		return new AndroidXmlReader(new JseInputStream(inputStream));
	}

}
