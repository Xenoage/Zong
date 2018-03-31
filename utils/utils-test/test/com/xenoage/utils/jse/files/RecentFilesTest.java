package com.xenoage.utils.jse.files;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

/**
 * Test cases for the {@link RecentFiles} class.
 * 
 * @author Andreas Wenger
 */
public class RecentFilesTest {

	@Test public void test() {
		assertEquals("Test was written for 5 entries", 5, RecentFiles.maxEntries);
		//delete file
		io().deleteFile(RecentFiles.filePath, false);
		//save 7 files (2 are non-existing)
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/1.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/2.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/err1.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/3.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/4.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/err2.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/5.txt"));
		//get files. must be 5.txt, 4.txt, 3.txt, 2.txt and 1.txt
		ArrayList<File> files = RecentFiles.getRecentFiles();
		assertEquals(5, files.size());
		assertEqualsFiles(new File("data/test/RecentFilesTest/5.txt"), files.get(0));
		assertEqualsFiles(new File("data/test/RecentFilesTest/4.txt"), files.get(1));
		assertEqualsFiles(new File("data/test/RecentFilesTest/3.txt"), files.get(2));
		assertEqualsFiles(new File("data/test/RecentFilesTest/2.txt"), files.get(3));
		assertEqualsFiles(new File("data/test/RecentFilesTest/1.txt"), files.get(4));
		//add 6.txt, 7.txt and 8.txt
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/6.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/7.txt"));
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/8.txt"));
		//get files. must be 8.txt, 7.txt, 6.txt, 5.txt and 4.txt
		files = RecentFiles.getRecentFiles();
		assertEquals(5, files.size());
		assertEqualsFiles(new File("data/test/RecentFilesTest/8.txt"), files.get(0));
		assertEqualsFiles(new File("data/test/RecentFilesTest/7.txt"), files.get(1));
		assertEqualsFiles(new File("data/test/RecentFilesTest/6.txt"), files.get(2));
		assertEqualsFiles(new File("data/test/RecentFilesTest/5.txt"), files.get(3));
		assertEqualsFiles(new File("data/test/RecentFilesTest/4.txt"), files.get(4));
		//reopen 6.txt. must move to the front.
		RecentFiles.addRecentFile(new File("data/test/RecentFilesTest/6.txt"));
		files = RecentFiles.getRecentFiles();
		assertEquals(5, files.size());
		assertEqualsFiles(new File("data/test/RecentFilesTest/6.txt"), files.get(0));
		assertEqualsFiles(new File("data/test/RecentFilesTest/8.txt"), files.get(1));
		assertEqualsFiles(new File("data/test/RecentFilesTest/7.txt"), files.get(2));
		assertEqualsFiles(new File("data/test/RecentFilesTest/5.txt"), files.get(3));
		assertEqualsFiles(new File("data/test/RecentFilesTest/4.txt"), files.get(4));
	}

	private void assertEqualsFiles(File f1, File f2) {
		assertEquals(f1.getAbsolutePath(), f2.getAbsolutePath());
	}

}
