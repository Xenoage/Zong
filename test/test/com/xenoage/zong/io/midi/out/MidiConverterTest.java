package com.xenoage.zong.io.midi.out;

import org.junit.Test;

/**
 * Tests for the {@link MidiConverter}.
 * 
 * @author Andreas Wenger
 */
public class MidiConverterTest {
	
	@Test public void testSampleFiles() {
		/* GOON
		boolean writeFiles = false;
		for (String file : MusicXMLScoreFileInputTest.getSampleFiles()) {
			try {
				Score score = new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(file), file);
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
		} */
	}

}
