package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInputTest;
import lombok.val;
import org.junit.Test;

import javax.sound.midi.MidiSystem;
import java.io.File;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.zong.io.midi.out.MidiConverter.Options.optionsForPlayback;
import static org.junit.Assert.*;

/**
 * Tests for the {@link MidiConverter}.
 * 
 * @author Andreas Wenger
 */
public class MidiConverterTest {
	
	@Test public void testSampleFiles() {
		boolean writeFiles = true;
		for (String file : MusicXmlScoreFileInputTest.getSampleFiles()) {
			try {
				val score = new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(file), file);
				val sequence = MidiConverter.convertToSequence(
					score, optionsForPlayback, new JseMidiSequenceWriter());
				if (writeFiles) {
					//try: write to file
					new File("temp").mkdirs();
					System.out.println("Converting: " + file);
					MidiSystem.write(sequence.getSequence(), 1, new File("temp/" + file.replaceAll("/", "_") + ".mid"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Failed to load file: " + file);
			}
		}
	}

}
