package com.xenoage.zong.tools.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.zip.ZipUtils;

/**
 * Lists the files in the given JAR recursively.
 * 
 * @author Andreas Wenger
 */
public class JarCollector {
	
	public static void process(File jarFile, Element eParent)
		throws IOException {
		//simple solution: unpack jar file to temp dir, list the files, delete temp dir
		File tempDir = JseFileUtils.createNewTempFolder();
		ZipUtils.extractAll(new FileInputStream(jarFile), tempDir);
		DirCollector.processDirChildren(tempDir, eParent);
		JseFileUtils.deleteDirectory(tempDir);
	}

}
