package com.xenoage.zong.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.xenoage.utils.io.IO;
import com.xenoage.utils.log.AppLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.ConverterScoreDocFormats;
import com.xenoage.zong.io.ScoreDocFileInput;
import com.xenoage.zong.io.ScoreFileFormat;
import com.xenoage.zong.io.SupportedFormats;
import com.xenoage.zong.io.symbols.AWTSVGPathReader;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.SymbolPoolUtils;


/**
 * Task "--convert" for the command line.
 * 
 * Syntax:
 * <pre>--convert infile outfile format</pre>
 * 
 * Example:
 * <pre>--convert /home/andi/files/Beeth.xml "/home/andi/pino 10 files/test.pino10" pino10</pre>
 * 
 * Input format is always MusicXML.
 * 
 * @author Andreas Wenger
 */
public class Converter
{
	
	public static final String PROJECT_FIRST_NAME = "Converter";
	public static final String FILENAME = Zong.FILENAME + "/converter/";
	

	public static void main(String[] args)
		throws IOException
	{
		IO.initApplication(FILENAME);
		Log.init(new AppLogProcessing(Zong.getNameAndVersion(PROJECT_FIRST_NAME)));
		SymbolPoolUtils.init(new AWTSVGPathReader());
		SymbolPoolUtils.setDefaultSymbolPool(new SymbolPool());
		convert(args, ConverterScoreDocFormats.getInstance());
	}
	
	
	public static void convert(String[] args, SupportedFormats supportedFormats)
		throws IOException
	{
		//exception for wrong format
		IOException ex =
			new IOException("Convert syntax: --convert infile outfile format");
		if (args.length < 4) throw ex;
		//first argument: --convert
		if (!args[0].equals("--convert")) throw ex;
		//second argument: input file
		File inputFile = new File(args[1]);
		if (!inputFile.exists())
			throw new IOException("Input file could not be found");
		//third argument: output file
		File outputFile = new File(args[2]);
		//fourth argument: output format
		ScoreFileFormat format = supportedFormats.getByID(args[3]);
		if (format == null) 
			throw new IOException("Can not save files in format " + args[3]);
		//do the conversion
		ScoreDocFileInput input = (ScoreDocFileInput) supportedFormats.getByID("MusicXML").getInput();
		ScoreDoc doc = null;
		doc = input.read(new FileInputStream(inputFile), inputFile.getAbsolutePath());
		if (doc != null && format.getOutput() != null) {
			format.getOutput().write(doc, new FileOutputStream(outputFile), outputFile.getAbsolutePath());
		}
		else
			throw new IOException("Invalid score file");
	}

}
