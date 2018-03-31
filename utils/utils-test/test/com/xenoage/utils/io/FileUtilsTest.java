package com.xenoage.utils.io;

import static com.xenoage.utils.io.FileUtils.cleanPath;
import static com.xenoage.utils.io.FileUtils.getNameWithoutExt;
import static com.xenoage.utils.io.FileUtils.splitDirectoryAndFilename;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;

/**
 * Test cases for the {@link FileUtils} class.
 *
 * @author Andreas Wenger
 */
public class FileUtilsTest {

	@Test public void splitDirectoryAndFilenameTest() {
		//test "1/2/3.pdf"
		String s = "1/2/3.pdf";
		Tuple2<String, String> res = splitDirectoryAndFilename(s);
		assertEquals("1/2", res.get1());
		assertEquals("3.pdf", res.get2());
		//test "4.xml"
		s = "4.xml";
		res = splitDirectoryAndFilename(s);
		assertEquals("", res.get1());
		assertEquals("4.xml", res.get2());
		//test "1/2/3/"
		s = "1/2/3/";
		res = splitDirectoryAndFilename(s);
		assertEquals("1/2", res.get1());
		assertEquals("3", res.get2());
		//test "1/2/3/"
		s = "1/2/3";
		res = splitDirectoryAndFilename(s);
		assertEquals("1/2", res.get1());
		assertEquals("3", res.get2());
		//test "1/2/"
		s = "1/2/";
		res = splitDirectoryAndFilename(s);
		assertEquals("1", res.get1());
		assertEquals("2", res.get2());
		//test "1/"
		s = "1/";
		res = splitDirectoryAndFilename(s);
		assertEquals("", res.get1());
		assertEquals("1", res.get2());
	}

	@Test public void getNameWithoutExtTest() {
		//filename beginning with "."
		assertEquals(".htaccess", getNameWithoutExt(".htaccess"));
		assertEquals(".htaccess", getNameWithoutExt(".htaccess.tmp"));
		assertEquals(".htaccess", getNameWithoutExt(".htaccess.tmp.bla"));
		assertEquals(".htaccess", getNameWithoutExt("/.htaccess.tmp.bla"));
		assertEquals(".htaccess", getNameWithoutExt("/ok/.htaccess.tmp"));
		assertEquals(".htaccess", getNameWithoutExt("some/dirs/.htaccess"));
		assertEquals(".htaccess", getNameWithoutExt("C:\\some\\dirs\\.htaccess.tmp"));
		//other file names
		assertEquals("doc", getNameWithoutExt("doc"));
		assertEquals("doc", getNameWithoutExt("doc.tmp"));
		assertEquals("doc", getNameWithoutExt("doc.tmp.bla"));
		assertEquals("doc", getNameWithoutExt("/doc.tmp.bla"));
		assertEquals("doc", getNameWithoutExt("/ok/doc.tmp"));
		assertEquals("doc", getNameWithoutExt("some/dirs/doc"));
		assertEquals("doc", getNameWithoutExt("C:\\some\\dirs\\doc.tmp"));
	}
	
	@Test public void cleanPathTest() {
		assertEquals("1/2/3/", cleanPath("1\\\\2/\\3\\"));
	}
	
}
