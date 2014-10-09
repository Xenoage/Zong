package com.xenoage.zong.tools.index;

import java.io.File;
import java.io.FileOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.io.index.FilesystemIndex;
import com.xenoage.utils.jse.xml.XMLWriter;

/**
 * This little program creates a {@link FilesystemIndex} file
 * for directories or for a JAR file.
 * 
 * Possible arguments:
 * <ul>
 *   <li>(none): list all content in the current directory</li>
 *   <li>-jar myjar1.jar myjar2.jar ...: lists the content of the given jar files</li>
 *   <li>-dir mydir1 mydir2 mydir3 ...: lists the content of the given directories</li>
 * </ul>
 * 
 * The resulting index file is written to the current directory.
 * 
 * @author Andreas Wenger
 */
public class FilesystemIndexCreator {

	/**
	 * @param args  names of the directories to include (like "data" and "files").
	 *              If empty, the current directory is used.
	 */
	public static void main(String... args)
		throws Exception {
		Document doc = XMLWriter.createEmptyDocument();
		Element root = XMLWriter.addElement("index", doc);
		if (args.length == 0) {
			DirCollector.processDirChildren(new File("."), root);
		}
		else if (args.length >= 2 && args[0].equals("-jar")) {
			for (int i = 1; i < args.length; i++) {
				JarCollector.process(new File(args[i]), root);
			}
		}
		else if (args.length >= 2 && args[0].equals("-dir")) {
			for (int i = 1; i < args.length; i++) {
				DirCollector.processDir(new File(args[i]), root);
			}
		}
		else {
			System.out.println("Wrong syntax. See class JavaDoc.");
			return;
		}
		XMLWriter.writeFile(doc, new FileOutputStream(FilesystemIndex.indexFile));
		System.out.println(FilesystemIndexCreator.class.getSimpleName() + " finished.");
		System.out.println("Index written to file " + FilesystemIndex.indexFile);
	}
	
	
	
	
}
