package com.xenoage.zong.desktop.test;

import static com.xenoage.utils.jse.io.FileUtils.listFilesDeep;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.FileUtils;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXMLScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This program can process a large number of MusicXML files
 * within a directory (recursively).
 * 
 * The files will be loaded and checked for consistency.
 * If a file fails, its name is written to standard output.
 * 
 * This class was written for Vladimir Viro to filter
 * unsupported MusicXML files out of about 100,000 files
 * scanned and exported with SmartScore.
 * 
 * @author Andreas Wenger
 */
public class FileTester {

	private String dir;


	public static void main(String... args) {
		DesktopIO.initTest();
		Log.initNoLog();
		if (args.length == 0) {
			System.out.println("Usage:   One parameter: Directory with MusicXML files");
			System.out.println("Output:  Names which could not be loaded");
		}
		else {
			FileTester test = new FileTester();
			test.dir = new File(args[0]).getAbsolutePath();
			test.start();
		}
	}

	private void start() {
		Collection<File> files = listFilesDeep(new File(dir), FileUtils.getXMLFilter());
		for (File file : files) {
			testFile(file);
		}
	}

	private void testFile(File file) {
		try {
			ScoreDoc x = new MusicXMLScoreDocFileInput().read(new FileInputStream(file),
				file.getAbsolutePath());
			ScoreTest.assertConsistency(x.getScore());
		} catch (Exception ex) {
			System.out.println(file.toString());
		}
	}

}
