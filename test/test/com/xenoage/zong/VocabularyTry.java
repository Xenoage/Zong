package com.xenoage.zong;

import static com.xenoage.utils.PlatformUtils.init;
import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.jse.io.JseFileUtils.listFilesDeep;
import static com.xenoage.utils.lang.VocByString.voc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.utils.jse.lang.LanguageReader;
import com.xenoage.utils.lang.Language;
import com.xenoage.utils.lang.VocID;

/**
 * This class contains tests for the language packages.
 * 
 * The English package ("en") is defined as the reference package,
 * which should be complete (this is also tested here).
 * 
 * The tests checks
 * <ul>
 * 	<li>the usage of the defined vocabulary items in {@link Voc} in the program:</li>
 * 	<ul>
 * 		<li>each word in the {@link Voc} class must be referenced at least one time
 * 			by looking at all java files</li>
 * 	</ul>
 *	<li>the reference package:</li>
 *	<ul>
 *		<li>the {@link Voc} words are implicitly complete, because they are given in the source code</li>
 *		<li>for each {@link Command} there must be a name</li>
 *	</ul>
 *	<li>all translations:</li>
 *	<ul>
 *		<li>for each word from the reference package: is it defined in this package?</li>
 *		<li>for each word from this package: is it defined in the reference package?</li>
 *	</ul>
 * </ul>
 * 
 * The result (including missing or unneeded words) is written to the
 * <code>{@value #logFileName}</code> file.
 * 
 * @author Andreas Wenger
 */
public class VocabularyTry {

	private String referenceID = "en";
	private String[] translationIDs = new String[] { "de" };

	private Language referencePackage = null;
	private Language[] translationPackages = new Language[translationIDs.length];

	private static final String logFileName = "VocabularyTryResult.txt";
	private PrintStream logFile = null;


	public static void main(String... args)
		throws Exception {
		init(new JsePlatformUtils());
		DesktopIO.init(Zong.filename + "/" + VocabularyTry.class.getName());
		VocabularyTry test = new VocabularyTry();
		test.setUp();
		test.testAllVocWordsUsed();
		test.testReferenceCommandNames();
		test.testTranslationCompleteness();
		test.testTranslationMinimality();
		test.close();
	}

	public void setUp()
		throws IOException {
		//open file for logging
		logFile = new PrintStream(logFileName, "UTF-8");
		//load reference package
		referencePackage = LanguageReader.read(LangManager.defaultLangPath, referenceID);
		for (VocID voc : Voc.values()) {
			if (referencePackage.getWithNull(voc) == null)
				referencePackage.add(voc.toString(), voc.getDefaultValue());
		}
		//load translation packages
		for (int i = 0; i < translationPackages.length; i++) {
			translationPackages[i] = LanguageReader.read(LangManager.defaultLangPath, translationIDs[i]);
		}
	}

	/**
	 * Tests, if all words of the given project are used (see class documentation).
	 * No real Java parsing is done. Instead, just <code>"Voc."+id</code> is searched.
	 */
	public void testAllVocWordsUsed() {
		//collect all Voc values
		LinkedList<VocID> remaining = llist((VocID[]) Voc.values());
		//search in all .java files in src folder
		Collection<File> files = listFilesDeep(new File(".."), JseFileUtils.getJavaFilter());
		for (File file : files) {
			String fileContent = JseFileUtils.readFile(file);
			for (Iterator<VocID> vocIt = remaining.iterator(); vocIt.hasNext();) {
				VocID voc = vocIt.next();
				if (fileContent.contains("Voc." + voc.getID()))
					vocIt.remove();
			}
		}
		//print all remaining elements
		if (remaining.size() > 0) {
			logFile.println("\nUnneeded Voc items:");
			for (VocID voc : remaining) {
				logFile.println(voc);
			}
		}
		//assertEquals("There are unneeded Voc items", 0, remaining.size());
	}

	/**
	 * Tests, if each class extending the {@link Command} class, which is not
	 * abstract, has a name in the reference language pack.
	 */
	public void testReferenceCommandNames() {
		String cmdPackage = "com.xenoage.zong.commands.";
		ArrayList<Class<?>> commands = SubclassCollector.getClassesImplementing(Command.class);
		//if there are less than 10 commands, something went wrong
		if (commands.size() < 10)
			logFile.println("Warning: Less then 10 commands - seems to be an error!");
		//walk through all commands
		for (Class<?> cls : commands) {
			//test only commands in cmdPackage, ignore test commands in "test." package
			//and ignore abstract classes
			String clsName = cls.getName();
			String packageName = clsName.substring(cmdPackage.length());
			if (clsName.startsWith(cmdPackage) && !packageName.startsWith("test.") &&
				!Modifier.isAbstract(cls.getModifiers())) {
				//look for name
				if (referencePackage.getWithNull(voc(clsName)) == null) {
					logFile.println("Vocabulary for Command \"" + clsName + "\" not found in language \"" +
						referenceID + "\"");
				}
			}
		}
	}

	/**
	 * Tests, if each word from the reference package is defined in the other packages
	 * of the given project (see class documentation).
	 */
	public void testTranslationCompleteness()
		throws Exception {
		for (Language pack : translationPackages) {
			if (pack.getID().equals(referencePackage.getID()))
				continue;
			//check other language
			LinkedList<String> unknown = new LinkedList<String>();
			for (String key : referencePackage.getAllKeys()) {
				if (pack.getWithNull(key) == null)
					unknown.add(key);
			}
			//print all unknown elements
			if (unknown.size() > 0) {
				logFile.println("\nMissing elements in language \"" + pack.getID() + "\":");
				for (String voc : unknown) {
					logFile.println(voc);
				}
			}
			//assertEquals("The language \"" + pack.getID() + "\" is incomplete", 0, unknown.size());
		}
	}

	/**
	 * Tests, if each word of the other packages can also be found in the
	 * reference package (see class documentation).
	 */
	public void testTranslationMinimality()
		throws Exception {
		for (Language pack : translationPackages) {
			if (pack.getID().equals(referencePackage.getID()))
				continue;
			//check other language
			LinkedList<String> unused = new LinkedList<String>();
			for (String key : pack.getAllKeys()) {
				//look for a reference language item with the same name
				if (!referencePackage.getAllKeys().contains(key)) {
					unused.add(key);
				}
			}
			//print all unused elements
			if (unused.size() > 0) {
				logFile.println("\nUnneeded elements in language \"" + pack.getID() + "\":");
				for (String voc : unused) {
					logFile.println(voc);
				}
			}
			//assertEquals("The language \"" + pack.getID() + "\" is not minimal.", 0, unused.size());
		}
	}

	public void close() {
		logFile.close();
	}

}
