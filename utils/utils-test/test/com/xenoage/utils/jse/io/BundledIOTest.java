package com.xenoage.utils.jse.io;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.io.FilesystemItem;
import com.xenoage.utils.io.index.FilesystemIndex;
import com.xenoage.utils.io.index.IndexedDirectory;
import com.xenoage.utils.io.index.IndexedFile;

/**
 * Tests for {@link BundledIO}.
 * 
 * @author Andreas Wenger
 */
public class BundledIOTest {
	
	@Test public void openFileTest()
		throws IOException {
		//index which contains the file "META-INF/MANIFEST.MF" (exists in
		//some JAR on the classpath)
		IndexedDirectory dir = new IndexedDirectory("META-INF");
		dir.addChild(new IndexedFile("MANIFEST.MF", null));
		FilesystemIndex index = new FilesystemIndex(CollectionUtils.<FilesystemItem>alist(dir));
		//create IO for bundled resources and read the file
		BundledIO bundledIO = new BundledIO(index);
		JseInputStream stream = bundledIO.openFile("META-INF/MANIFEST.MF");
		//must start with "Manifest-Version: "
		String content = JseStreamUtils.readToString(stream);
		assertTrue(content.startsWith("Manifest-Version: "));
	}

}
