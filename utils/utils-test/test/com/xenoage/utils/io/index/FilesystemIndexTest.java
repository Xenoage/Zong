package com.xenoage.utils.io.index;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.io.Directory;
import com.xenoage.utils.io.File;
import com.xenoage.utils.io.FilesystemItem;

/**
 * Tests for {@link FilesystemIndex}.
 * 
 * @author Andreas Wenger
 */
public class FilesystemIndexTest {
	
	private FilesystemIndex index;
	
	@Before public void setup() {
		List<FilesystemItem> items = alist();
		IndexedDirectory folder, subfolder;
		items.add(new IndexedFile("rootfile1", null));
		items.add(new IndexedFile("rootfile2", null));
		items.add(folder = new IndexedDirectory("folder"));
		folder.addChild(new IndexedFile("file", null));
		folder.addChild(subfolder = new IndexedDirectory("subfolder"));
		subfolder.addChild(new IndexedFile("file1", null));
		subfolder.addChild(new IndexedFile("file2", null));
		folder.addChild(new IndexedDirectory("secondsubfolder"));
		index = new FilesystemIndex(items);
	}
	
	@Test public void existsFileTest() {
		//test with and without leading "/"
		assertEquals(true, index.existsFile("/folder/file"));
		assertEquals(true, index.existsFile("folder/file"));
		//test other files
		assertEquals(true, index.existsFile("rootfile1"));
		assertEquals(true, index.existsFile("rootfile2"));
		assertEquals(true, index.existsFile("folder/subfolder/file1"));
		assertEquals(true, index.existsFile("folder/subfolder/file2"));
		//test some folders and non-existing files
		assertEquals(false, index.existsFile("folder"));
		assertEquals(false, index.existsFile("folder/subfolder/"));
		assertEquals(false, index.existsFile("foo"));
		assertEquals(false, index.existsFile("folder/foo"));
	}

	@Test public void existsDirectoryTest() {
		//test with and without trailing "/"
		assertEquals(true, index.existsDirectory("folder"));
		assertEquals(true, index.existsDirectory("/folder"));
		//test other directories
		assertEquals(true, index.existsDirectory("folder/subfolder"));
		assertEquals(true, index.existsDirectory("folder/secondsubfolder"));
		//test files and non-existing directories
		assertEquals(false, index.existsDirectory("folder/file"));
		assertEquals(false, index.existsDirectory("folder/file/"));
		assertEquals(false, index.existsDirectory("foofolder/"));
		assertEquals(false, index.existsDirectory("folder/foofolder/"));
	}

	@Test public void listFilesTest() {
		//folder "/"
		List<File> files = index.listFiles("/");
		assertEquals(2, files.size());
		assertTrue(containsItem(files, "rootfile1"));
		assertTrue(containsItem(files, "rootfile2"));
		//folder "folder/"
		files = index.listFiles("folder/");
		assertEquals(1, files.size());
		assertTrue(containsItem(files, "file"));
		//folder "folder/subfolder"
		files = index.listFiles("folder/subfolder/");
		assertEquals(2, files.size());
		assertTrue(containsItem(files, "file1"));
		assertTrue(containsItem(files, "file2"));
		//folder "folder/secondsubfolder/": empty
		assertEquals(0, index.listFiles("folder/secondsubfolder/").size());
		//non existing folder: empty
		assertEquals(0, index.listFiles("foo/").size());
	}
	
	@Test public void listDirectoriesTest() {
		//folder "/"
		List<Directory> dirs = index.listDirectories("/");
		assertEquals(1, dirs.size());
		assertTrue(containsItem(dirs, "folder"));
		//folder "folder/"
		dirs = index.listDirectories("folder/");
		assertEquals(2, dirs.size());
		assertTrue(containsItem(dirs, "subfolder"));
		assertTrue(containsItem(dirs, "secondsubfolder"));
		//folder "folder/subfolder": empty
		assertEquals(0, index.listDirectories("folder/subfolder/").size());
		//folder "folder/secondsubfolder/": empty
		assertEquals(0, index.listDirectories("folder/secondsubfolder/").size());
		//non existing folder: empty
		assertEquals(0, index.listDirectories("foo/").size());
	}
	
	public static boolean containsItem(List<? extends FilesystemItem> items, String name) {
		for (FilesystemItem item : items)
			if (item.getName().equals(name))
				return true;
		return false;
	}

}
