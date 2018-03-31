package com.xenoage.utils.jse.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.zip.ZipUtils;

/**
 * Test cases for the {@link ZipUtils} class.
 * 
 * @author Andreas Wenger
 */
public class ZipUtilsTest {

	File dir = new File(JseFileUtils.getTempFolder(), getClass().getName() + ".extractAllTest");


	@Before public void setUp() {
		JseFileUtils.deleteDirectory(dir);
		dir.mkdir();
	}

	@Test public void extractAllTest()
		throws IOException {
		ZipUtils.extractAll(new FileInputStream("data/test/zip/album.zip"), dir);
		assertTrue(new File(dir, "META-INF/container.xml").exists());
		assertTrue(new File(dir, "BeetAnGeSample.xml").exists());
		assertTrue(new File(dir, "BrahWiMeSample.mxl").exists());
		assertTrue(new File(dir, "SchbAvMaSample.xml").exists());
	}

	@After public void cleanUp() {
		JseFileUtils.deleteDirectory(dir);
	}

}
