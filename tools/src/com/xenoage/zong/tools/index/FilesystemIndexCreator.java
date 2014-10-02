package com.xenoage.zong.tools.index;

import java.io.File;
import java.io.FileOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.io.index.FilesystemIndex;
import com.xenoage.utils.jse.xml.XMLWriter;

/**
 * This little program creates a {@link FilesystemIndex} file
 * in the current directory, recursively listing all files
 * and directories in the given directories.
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
			processChildren(new File("."), root);
		}
		else {
			for (String dir : args) {
				processFileOrDir(new File(dir), root);
			}
		}
		XMLWriter.writeFile(doc, new FileOutputStream(FilesystemIndex.indexFile));
		System.out.println(FilesystemIndexCreator.class.getSimpleName() + " finished.");
		System.out.println("Index written to file " + FilesystemIndex.indexFile);
	}
	
	private static void processChildren(File dir, Element eParent) {
		for (File child : dir.listFiles()) {
			processFileOrDir(child, eParent);
		}
	}
	
	private static void processFileOrDir(File file, Element eParent) {
		if (file.isFile()) {
			Element eFile = XMLWriter.addElement("file", eParent);
			eFile.setAttribute("name", file.getName());
			eFile.setAttribute("size", ""+file.length());
		}
		else if (file.isDirectory()) {
			Element eDir = XMLWriter.addElement("directory", eParent);
			eDir.setAttribute("name", file.getName());
			processChildren(file, eDir);
		}
	}
	
	
}
