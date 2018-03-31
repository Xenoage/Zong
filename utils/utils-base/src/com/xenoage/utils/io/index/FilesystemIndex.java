package com.xenoage.utils.io.index;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.io.Directory;
import com.xenoage.utils.io.File;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.FilesystemItem;

/**
 * Index of files and directories.
 * 
 * Some file systems do not provide methods for listing files and directories,
 * for example web servers with HTTP access or files within the JAR files on
 * the classpath. On these filesystems, a file {@value #indexFile} can be
 * provided which contains an index of all available files.
 * 
 * Use the {@link FilesystemIndexReader} to load such an index file.
 * 
 * @author Andreas Wenger
 */
public class FilesystemIndex {

	public static final String indexFile = ".index";
	
	private static final List<File> emptyFileList = new CList<File>().close();
	private static final List<Directory> emptyDirList = new CList<Directory>().close();

	private final Directory root;


	/**
	 * Creates an index from the given index file data.
	 * @param indexFileContent  the complete content of the index file
	 */
	public FilesystemIndex(List<FilesystemItem> items) {
		this.root = new IndexedDirectory("", items);
	}
	
	/**
	 * Returns the file at the given path, or null if not found.
	 */
	public File findFile(String filePath) {
		FilesystemItem item = findItem(filePath);
		if (item instanceof File)
			return (File) item;
		else
			return null;
	}
	
	/**
	 * Returns the directory at the given path, or null if not found.
	 */
	public Directory findDirectory(String dirPath) {
		FilesystemItem item = findItem(dirPath);
		if (item instanceof Directory)
			return (Directory) item;
		else
			return null;
	}
	
	/**
	 * Returns the item at the given path, or null if not found.
	 */
	public FilesystemItem findItem(String path) {
		path = cleanPath(path);
		String[] pathItems = path.split("/");
		if (pathItems[0].length() == 0)
			return root;
		else
			return findItem(pathItems, 0, root);
	}
	
	private String cleanPath(String path) {
		//clean file path (e.g. "\" -> "/")
		path = path.trim();
		path = FileUtils.cleanPath(path);
		//remove leading "/"
		if (path.startsWith("/"))
			path = path.substring(1);
		return path;
	}
	
	private FilesystemItem findItem(String[] pathItems, int currentIndex, Directory currentDir) {
		//find current item
		String currentName = pathItems[currentIndex];
		FilesystemItem nextItem = null;
		for (FilesystemItem currentItem : currentDir.getChildren()) {
			if (currentItem.getName().equals(currentName)) {
				nextItem = currentItem;
			}
		}
		//finished or next level
		if (currentIndex == pathItems.length - 1)
			return nextItem;
		else if (false == nextItem instanceof Directory)
			return null;
		else
			return findItem(pathItems, currentIndex + 1, (Directory)nextItem);
	}
	
	
	public boolean existsFile(String filepath) {
		return (findItem(filepath) instanceof File);
	}
	
	
	public boolean existsDirectory(String dirpath) {
		return (findItem(dirpath) instanceof Directory);
	}


	/**
	 * Returns the files in the given directory.
	 * If nothing is found, an empty list is returned.
	 */
	public List<File> listFiles(String dirPath) {
		//find items
		Directory dir = findDirectory(dirPath);
		if (dir == null)
			return emptyFileList;
		//select only files
		List<File> ret = alist();
		for (FilesystemItem item : dir.getChildren())
			if (item instanceof File)
				ret.add((File) item);
		return ret;
	}
	
	/**
	 * Returns the names of the files in the given directory
	 * matching the given file filter.
	 * If nothing is found, an empty list is returned.
	 * Only the file names are returned, not their complete paths.
	 */
	public List<File> listFiles(String dirPath, FileFilter filter) {
		//retrieve unfiltered list
		List<File> candidates = listFiles(dirPath);
		//select only files which match
		List<File> ret = alist();
		for (File candidate : candidates)
			if (filter.accept(candidate))
				ret.add(candidate);
		return ret;
	}

	/**
	 * Returns the directories in the given directory.
	 * If nothing is found, an empty list is returned.
	 */
	public List<Directory> listDirectories(String dirPath) {
		//find items
		Directory dir = findDirectory(dirPath);
		if (dir == null)
			return emptyDirList;
		//select only directories
		List<Directory> ret = alist();
		for (FilesystemItem item : dir.getChildren())
			if (item instanceof Directory)
				ret.add((Directory) item);
		return ret;
	}

}
