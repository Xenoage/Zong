package com.xenoage.zong.tools.index;

import java.io.File;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLWriter;

/**
 * Lists the files in the given directory recursively.
 * 
 * @author Andreas Wenger
 */
public class DirCollector {
	
	public static void processDirChildren(File dir, Element eParent) {
		for (File child : dir.listFiles()) {
			processDir(child, eParent);
		}
	}
	
	public static void processDir(File file, Element eParent) {
		if (file.isFile()) {
			Element eFile = XMLWriter.addElement("file", eParent);
			eFile.setAttribute("name", file.getName());
			eFile.setAttribute("size", ""+file.length());
		}
		else if (file.isDirectory()) {
			Element eDir = XMLWriter.addElement("directory", eParent);
			eDir.setAttribute("name", file.getName());
			processDirChildren(file, eDir);
		}
	}

}
