package com.xenoage.zong.converter;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.fatal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.ConverterSupportedFormats;

/**
 * The <strong>Zong! Converter</strong> command-line application
 * can convert MusicXML files into different graphic and audio formats
 * like PDF, PNG, WAV, MID, OGG and MP3.
 * 
 * Syntax:
 * <pre>... --convert infile outfile format</pre>
 * 
 * Example:
 * <pre>... --convert /home/andi/files/Beeth.xml "/home/andi/my midi files/test.mid" mid</pre>
 * 
 * The input format is always MusicXML.
 * To list the possible output formats, call <pre>... --formats</pre>
 * 
 * @author Andreas Wenger
 */
public class Converter {

	public static final String projectFirstName = "Converter";
	public static final String filename = Zong.filename + "/converter/";

	private static ConverterSupportedFormats supportedFormats = new ConverterSupportedFormats();


	public static void main(String[] args)
		throws IOException {
		JseZongPlatformUtils.init(filename);
		
		Log.init(new DesktopLogProcessing(Zong.getNameAndVersion(projectFirstName)));
		//SymbolPoolUtils.init(new AWTSVGPathReader());
		//SymbolPoolUtils.setDefaultSymbolPool(new SymbolPool());
		
		try {
			SynthManager.init(true);
		} catch (MidiUnavailableException ex) {
			handle(fatal(ex));
		}

		//do the job
		if (args.length == 0)
			showHelp();
		else if (args[0].equals("--formats"))
			showFormats();
		else
			convert(args);
		
		//TIDY - maybe hangs in SynthManager
		System.exit(0);
	}

	private static void showHelp() {
		System.out.println("Usage:");
		System.out.println("... --convert infile outfile format");
		System.out.println("For example:");
		System.out
			.println("... --convert /home/andi/files/Beeth.xml \"/home/andi/my midi files/test.mid\" mid");
		System.out
			.println("Input format is always MusicXML. To list the supported output formats, call:");
		System.out.println("... --formats");
	}

	private static void showFormats() {
		for (FileFormat<ScoreDoc> format : supportedFormats.getWriteFormats()) {
			System.out.println(format.getId() + " (" + format.getName() + ")");
		}
	}

	public static void convert(String[] args)
		throws IOException {
		//exception for wrong format
		if (args.length < 4 || !args[0].equals("--convert")) {
			System.out.println("Wrong usage of parameters.");
			showHelp();
			return;
		}
		//second argument: input file
		File inputFile = new File(args[1]);
		if (!inputFile.exists()) {
			System.out.println("Input file could not be found at");
			System.out.println(inputFile.getAbsolutePath());
			return;
		}
		//third argument: output file
		File outputFile = new File(args[2]);
		//fourth argument: output format
		String formatId = args[3].toLowerCase();
		FileFormat<ScoreDoc> format = null;
		try {
			format = supportedFormats.getByID(formatId);
		} catch (IllegalArgumentException ex) {
		}
		if (format == null || format.getOutput() == null) {
			System.out.println("Can not save files in format " + formatId);
			System.out.println("Supported formats are:");
			showFormats();
			return;
		}
		//do the conversion
		FileInput<ScoreDoc> input = supportedFormats.getReadDefaultFormat().getInput();
		ScoreDoc doc = input.read(new JseInputStream(new FileInputStream(inputFile)),
			inputFile.getAbsolutePath());
		doc.getLayout().updateScoreLayouts(doc.getScore()); //TIDY
		format.getOutput().write(doc, new JseOutputStream(new FileOutputStream(outputFile)),
			outputFile.getAbsolutePath());
	}

}
