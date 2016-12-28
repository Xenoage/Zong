package com.xenoage.zong.test.utils;

import com.xenoage.utils.jse.io.JseFileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Helper program to sort/cleanup downloaded MusicXML test files.
 *
 * @author Andreas Wenger
 */
public class TestFileSorter {

	public static void main(String... args)
		throws IOException {
		//cleanFolkotecagalegaComFiles();
	}

	/**
	 * Cleanup MusicXML/PDF files from www.folkotecagalega.com.
	 */
	private static void cleanFolkotecagalegaComFiles()
		throws IOException {
		//files are in x/.../y/@@display-file/pdf/ or in
		//x/.../y/@@download/xml/ : move all PDF and XML files two
		//directories down, then delete the @@display-file and @@download folders
		File baseDir = new File("../Zong-Test/MusicXML/folkotecagalega.com/");
		for (File file : JseFileUtils.listFiles(baseDir, true)) {
			if (file.getName().endsWith(".pdf") || file.getName().endsWith(".xml")) {
				Path oldPath = file.toPath();
				Path newPath = Paths.get(oldPath.getParent().getParent().getParent().toString(), file.getName());
				if (oldPath.toFile().exists()) //may happen when there are encoding problems in the filename
					Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		for (File dir : JseFileUtils.listDirectories(baseDir, true)) {
			if (dir.getName().startsWith("@@")) {
				JseFileUtils.deleteDirectory(dir);
			}
		}
	}

}
