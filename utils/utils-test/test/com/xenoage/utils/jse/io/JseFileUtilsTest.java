package com.xenoage.utils.jse.io;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.kernel.Tuple2;

/**
 * Test cases for the {@link JseFileUtils} class.
 *
 * @author Andreas Wenger
 */
public class JseFileUtilsTest {

	@Test public void getNameWithoutExt() {
		//gets the filename without extension of this file
		assertEquals("My File",
			JseFileUtils.getNameWithoutExt(new File("some/path/with - space/My File.txt")));
	}

	@Test public void splitDirectoryAndFilename() {
		//test "1/2/3.pdf"
		String s = "1" + File.separator + "2" + File.separator + "3.pdf";
		Tuple2<String, String> res = JseFileUtils.splitDirectoryAndFilename(s);
		assertEquals("1" + File.separator + "2", res.get1());
		assertEquals("3.pdf", res.get2());
		//test "4.xml"
		s = "4.xml";
		res = JseFileUtils.splitDirectoryAndFilename(s);
		assertEquals("", res.get1());
		assertEquals("4.xml", res.get2());
	}

}
