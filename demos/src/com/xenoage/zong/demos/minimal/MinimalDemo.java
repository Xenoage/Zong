package com.xenoage.zong.demos.minimal;

import java.awt.Desktop;
import java.io.File;

import com.xenoage.zong.desktop.io.ScoreDocIO;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This tiny demo shows how to use Zong! to convert a MusicXML file
 * to a PDF file.
 * 
 * @author Andreas Wenger
 */
public class MinimalDemo {
	
	public static final String appName = "MinimalDemo";

	public static void main(String... args)
		throws Exception {
		//initialize platform-dependent utilities, including I/O.
		//the app name is required for the working directory
		JseZongPlatformUtils.init(appName);
		//load MusicXML file
		File inFile = new File("scores/BeetAnGeSample.xml");
		ScoreDoc doc = ScoreDocIO.read(inFile, new MusicXmlScoreDocFileInput());
		//convert to PDF
		File outFile = new File("demo.pdf");
		ScoreDocIO.write(doc, outFile, new PdfScoreDocFileOutput());
		//finished. open the PDF file.
		Desktop.getDesktop().open(outFile);
	}
	
}
