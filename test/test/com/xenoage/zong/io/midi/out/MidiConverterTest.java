package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static org.junit.Assert.fail;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.spi.MidiFileWriter;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.util.InconsistentScoreException;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;

/**
 * Tests for the {@link MidiConverter}.
 * 
 * @author Andreas Wenger
 */
public class MidiConverterTest {
	
	@Test public void testSampleFiles() {
		boolean writeFiles = false;
		for (String file : MusicXMLScoreFileInputTest.getSampleFiles()) {
			try {
				Score score = new MusicXmlScoreFileInput().read(platformUtils().openFile(file), file);
				MidiSequence<Sequence> sequence = MidiConverter.convertToSequence(
					score, true, true, new JseMidiSequenceWriter());
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
