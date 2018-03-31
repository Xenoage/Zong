package com.xenoage.utils.io.index;

import static com.xenoage.utils.io.index.FilesystemIndexTest.containsItem;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.utils.io.FilesystemItem;
import com.xenoage.utils.jse.xml.JseXmlReader;

/**
 * Tests for {@link FilesystemIndexReader}.
 * 
 * @author Andreas Wenger
 */
public class FilesystemIndexReaderTest {
	
	@Test public void test()
		throws Exception {
		//read file
		FilesystemIndex index = FilesystemIndexReader.read(
			new JseXmlReader(jsePlatformUtils().openFile("data/test/index/FilesystemIndexReaderTest.xml")));
		//check content
		List<? extends FilesystemItem> items;
		assertEquals(0, index.listFiles("/").size());
		assertEquals(2, (items = index.listDirectories("/")).size());
		assertEquals(true, containsItem(items, "dir"));
		assertEquals(true, containsItem(items, "dirempty"));
		assertEquals(2, (items = index.listFiles("/dir/")).size());
		assertEquals(true, containsItem(items, "file1"));
		assertEquals(1000, (long) index.findFile("dir/file1").getSizeInBytes());
		assertEquals(2000, (long) index.findFile("/dir/file2").getSizeInBytes());
		assertEquals(true, containsItem(items, "file2"));
		assertEquals(1, (items = index.listDirectories("/dir/")).size());
		assertEquals(1, (items = index.listDirectories("/dir/subdir")).size());
		assertEquals(0, (items = index.listDirectories("/dir/deadend")).size());
		assertEquals(0, index.listFiles("dirempty").size());
		assertEquals(0, index.listDirectories("dirempty").size());
	}

}
