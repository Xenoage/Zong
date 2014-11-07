package com.xenoage.zong.commands.player.convert;

import static com.xenoage.utils.jse.io.JseFileUtils.listFiles;
import static com.xenoage.zong.desktop.App.app;
import static com.xenoage.zong.player.Player.pApp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.filter.AllFilter;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.settings.FileSettings;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.musicxml.FileType;
import com.xenoage.zong.io.musicxml.in.FileTypeReader;

/**
 * This command lets the user select a directory whose MusicXML files
 * are then converted into MIDI files.
 * 
 * MusicXML files with multiple scores are also supported. In this case
 * each score gets its own MIDI file.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class DirToMidiConvert
	extends TransparentCommand {
	
	private Window ownerWindow;

	@Override public void execute() {
		DirectoryChooser dc = new DirectoryChooser();
		
		//use last document directory
		File initDir = FileSettings.getLastDir();
		if (initDir != null)
			dc.setInitialDirectory(initDir);

		/* TODO - how to extend the DirectoryChooser in JavaFX?
		//see http://stackoverflow.com/questions/25982945/javafx-filechooser-and-directorychooser-accessory-component
		JCheckBox chkSubdir = new JCheckBox(Lang.get(Voc.IncludeSubdirectories), true);
		JCheckBox chkCancel = new JCheckBox(Lang.get(Voc.CancelAtFirstError), false);
		JPanel pnlOptions = new JPanel();
		pnlOptions.setLayout(new BoxLayout(pnlOptions, BoxLayout.Y_AXIS));
		pnlOptions.add(chkSubdir);
		pnlOptions.add(chkCancel);
		fc.setAccessory(pnlOptions); */
		boolean subDirs = true; //TODO
		boolean cancelOnFirstError = false; //TODO

		File dir = dc.showDialog(ownerWindow);

		if (dir != null) {
			//remember directory
			FileSettings.rememberDir(dir);
			//start conversion - TODO: show progress
			List<File> files = listFiles(dir, subDirs);
			int countOK = 0;
			int countFailed = 0;

			for (File file : files) {
				try {
					//only process MusicXML files
					FileType fileType = FileTypeReader.getFileType(new JseInputStream(file));

					if (fileType != null) {
						String filePath = file.getAbsolutePath();
						List<Score> scores = pApp().loadMxlScores(filePath, new AllFilter<String>());

						if ((scores.size() == 0) /* TODO && chkCancel.isSelected() */) {
							countFailed++;
							break;
						}

						boolean useNumber = scores.size() > 1;
						It<Score> scoresIt = new It<Score>(scores);

						for (Score score : scoresIt) {
							Sequence seq = MidiConverter.convertToSequence(
								score, false, false, new JseMidiSequenceWriter()).getSequence();
							String number = (useNumber ? ("-" + (scoresIt.getIndex() + 1)) : "");
							String newPath = filePath;

							if (filePath.toLowerCase().endsWith(".xml") ||
								filePath.toLowerCase().endsWith(".mxl")) {
								newPath = newPath.substring(0, filePath.length() - 4);
							}

							newPath += (number + ".mid");
							MidiSystem.write(seq, 1, new File(newPath));
							countOK++;
						}
					}
				} catch (IOException ex) {
					countFailed++;

					if (cancelOnFirstError) {
						break;
					}
				}
			}

			app().showMessageDialog(
				Lang.get(Voc.DirectoryConversionResult, "" + countOK, "" + countFailed));
		}
	}

}
