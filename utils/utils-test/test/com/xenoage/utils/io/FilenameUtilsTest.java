package com.xenoage.utils.io;

import static com.xenoage.utils.io.FilenameUtils.numberFiles;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Test cases for {@link FilenameUtils}.
 * 
 * @author Andreas Wenger
 */
public class FilenameUtilsTest {

	@Test public void numberFilesTest() {
		numberFilesTest("", "name", ".xml");
		numberFilesTest("", "name", "");
		numberFilesTest("", ".htaccess", "");
		numberFilesTest("", "name", ".xml");
		numberFilesTest("", "name", "");
		numberFilesTest("", ".htaccess", "");
		numberFilesTest("/home/test/", "name", ".xml");
		numberFilesTest("/home/test/", "name", "");
		numberFilesTest("/home/test/", ".htaccess", "");
		numberFilesTest("/home/test/", "name", ".xml");
		numberFilesTest("/home/test/", "name", "");
		numberFilesTest("/home/test/", ".htaccess", "");
		numberFilesTest("C:\\te st\\", "name", ".xml");
		numberFilesTest("C:\\te st\\", "name", "");
		numberFilesTest("C:\\te st\\", ".htaccess", "");
		numberFilesTest("C:\\te st\\", "name", ".xml");
		numberFilesTest("C:\\te st\\", "name", "");
		numberFilesTest("C:\\te st\\", ".htaccess", "");
	}

	private void numberFilesTest(String dir, String name, String ext) {
		String total = dir + name + ext;
		//test simple case
		List<String> s = numberFiles(total, 1);
		assertEquals(1, s.size());
		assertEquals(total, s.get(0));
		//test with 9 files
		s = numberFiles(total, 9);
		assertEquals(9, s.size());
		assertEquals(dir + name + "-1" + ext, s.get(0));
		assertEquals(dir + name + "-9" + ext, s.get(8));
		//test with 99 files
		s = numberFiles(total, 99);
		assertEquals(99, s.size());
		assertEquals(dir + name + "-01" + ext, s.get(0));
		assertEquals(dir + name + "-99" + ext, s.get(98));
		//test with 100 files
		s = numberFiles(total, 100);
		assertEquals(100, s.size());
		assertEquals(dir + name + "-001" + ext, s.get(0));
		assertEquals(dir + name + "-100" + ext, s.get(99));
	}

}
