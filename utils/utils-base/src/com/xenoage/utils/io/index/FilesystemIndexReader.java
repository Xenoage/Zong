package com.xenoage.utils.io.index;

import java.io.IOException;

import com.xenoage.utils.xml.XmlReader;

/**
 * This class reads a {@link FilesystemIndex} from a XML file.
 * 
 * The format of the UTF-8 encoded XML index file is as follows:
 * <pre> {@code
 * <index>
 * 	<file name="myfile.txt" size="1000"/> <!-- size in bytes -->
 * 	<directory name="mydir">
 * 		<directory name="myemptychilddir"/>
 * 		<file name="mychildfile.pdf" size="14123"/>
 * 	</directory>
 * </index>
 * } </pre>
 * 
 * @author Andreas Wenger
 */
public class FilesystemIndexReader {
	
	private XmlReader xmlReader;
	
	
	public static FilesystemIndex read(XmlReader xmlReader)
		throws IOException {
		try {
			return new FilesystemIndexReader().parseFilesystemIndex(xmlReader);
		} catch (Exception ex) {
			throw new IOException("Could not read index file", ex);
		}
	}
	
	private FilesystemIndex parseFilesystemIndex(XmlReader xmlReader)
		throws Exception {
		try {
			this.xmlReader = xmlReader;
			checkFileFormat();
			IndexedDirectory root = new IndexedDirectory("");
			readDirectoryChildren(root);
			xmlReader.close();
			return new FilesystemIndex(root.getChildren());
		}
		catch (Exception ex) {
			xmlReader.close();
			throw ex;
		}
	}
	
	private void checkFileFormat() {
		if (xmlReader.openNextChildElement()) {
			if (false == xmlReader.getElementName().equals("index"))
				throw new IllegalStateException("No file index");
		}
		else {
			throw new IllegalStateException("No content");
		}
	}
	
	private void readFile(IndexedDirectory parentDir) {
		Long size = null;
		String s = xmlReader.getAttribute("size");
		if (s != null)
			size = Long.parseLong(s);
		String name = checkFilename(xmlReader.getAttribute("name"));
		parentDir.addChild(new IndexedFile(name, size));			
	}
	
	private void readDirectory(IndexedDirectory parentDir) {
		String name = checkFilename(xmlReader.getAttribute("name"));
		IndexedDirectory dir = new IndexedDirectory(name);
		parentDir.addChild(dir);
		readDirectoryChildren(dir);
	}
	
	private void readDirectoryChildren(IndexedDirectory parentDir) {
		while (xmlReader.openNextChildElement()) {
			String n = xmlReader.getElementName();
			if (n.equals("file"))
				readFile(parentDir);
			else if (n.equals("directory"))
				readDirectory(parentDir);
			else
				throw new IllegalStateException("Unknown element: " + n);
			xmlReader.closeElement();
		}
	}
	
	private String checkFilename(String filename) {
		if (filename == null)
			throw new IllegalStateException("Missing file name");
		//slash and backslash are not allowed
		if (filename.contains("/") || filename.contains("\\"))
			throw new IllegalStateException("Illegal filename: " + filename);
		return filename;
	}

}
