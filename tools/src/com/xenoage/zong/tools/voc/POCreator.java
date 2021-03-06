package com.xenoage.zong.tools.voc;

import java.io.IOException;
import java.io.PrintStream;

import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.utils.jse.lang.LanguageReader;
import com.xenoage.utils.lang.Language;
import com.xenoage.utils.lang.VocID;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;

/**
 * Creates a PO file (GNU gettext) with all vocab from {@link Voc}
 * for the language "de".
 * 
 * {@link JsePlatformUtils} is used.
 * 
 * @author Andreas Wenger
 */
public class POCreator {

	public static void main(String... args)
		throws IOException {
		JsePlatformUtils.init(POCreator.class.getName());
		Language lang = LanguageReader.read(LangManager.defaultLangPath, "de");
		POCreator.write(Voc.values(), lang, "de", "de.po");
	}

	public static void write(VocID[] voc, Language lang, String langID, String filename)
		throws IOException {
		PrintStream out = new PrintStream(filename, "UTF-8");
		//header
		out.println("# " + Zong.INSTANCE.getNameAndVersion("Vocabulary"));
		out.println("# " + Zong.INSTANCE.getCopyright());
		out.println("msgid \"\"");
		out.println("msgstr \"\"");
		out.println("\"Project-Id-Version: " + Zong.INSTANCE.getNameAndVersion("Vocabulary") + "\\n\"");
		out.println("\"POT-Creation-Date: \\n\"");
		out.println("\"PO-Revision-Date: \\n\"");
		out.println("\"Last-Translator: Andreas Wenger <andi@xenoage.com>\\n\"");
		out.println("\"Language-Team: Xenoage Software <info@xenoage.com>\\n\"");
		out.println("\"MIME-Version: 1.0\\n\"");
		out.println("\"Content-Type: text/plain; charset=utf-8\\n\"");
		out.println("\"Content-Transfer-Encoding: 8bit\\n\"");
		if (langID != null)
			out.println("\"Language: " + langID + "\\n\"");
		//entries
		for (VocID v : voc) {
			out.println();
			out.println("msgctxt \"" + e(v.toString()) + "\"");
			out.println("msgid \"" + e(v.getDefaultValue()) + "\"");
			out.println("msgstr \"" + e(lang != null ? lang.get(v) : "") + "\"");
		}
		out.close();
	}

	private static String e(String s) {
		return s.replace("\"", "\\\"").replace("\n", "\\n");
	}

}
