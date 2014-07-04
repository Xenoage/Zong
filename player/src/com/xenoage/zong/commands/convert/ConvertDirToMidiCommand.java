package com.xenoage.zong.commands.convert;

import static com.xenoage.zong.SwingApp.app;
import static com.xenoage.zong.io.midi.out.MidiConverter.convertToSequence;
import static com.xenoage.zong.player.PlayerApplication.pApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.xenoage.utils.base.filter.AllFilter;
import com.xenoage.utils.base.iterators.It;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.swing.JFileChooserUtil;
import com.xenoage.zong.Voc;
import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.core.Score;
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
public class ConvertDirToMidiCommand
	extends Command
{

	@Override public void execute(CommandPerformer performer)
	{
		JFileChooser fc = new JFileChooser();
		JFileChooserUtil.setDirFromSettings(fc);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JCheckBox chkSubdir = new JCheckBox(Lang.get(Voc.IncludeSubdirectories), true);
		JCheckBox chkCancel = new JCheckBox(Lang.get(Voc.CancelAtFirstError), false);
		JPanel pnlOptions = new JPanel();
		pnlOptions.setLayout(new BoxLayout(pnlOptions, BoxLayout.Y_AXIS));
		pnlOptions.add(chkSubdir);
		pnlOptions.add(chkCancel);
		fc.setAccessory(pnlOptions);

		int ret = fc.showOpenDialog(app().getMainFrame());

		if (ret == JFileChooser.APPROVE_OPTION) {
			File dir = fc.getSelectedFile();
			JFileChooserUtil.rememberDir(fc);

			List<File> files = FileUtils.listFiles(dir, chkSubdir.isSelected());
			int countOK = 0;
			int countFailed = 0;

			for (File file : files) {
				try {
					//only process MusicXML files
					FileType fileType = FileTypeReader.getFileType(new FileInputStream(file));

					if (fileType != null) {
						String filePath = file.getAbsolutePath();
						List<Score> scores = pApp().loadMxlScores(filePath, new AllFilter<String>());

						if ((scores.size() == 0) && chkCancel.isSelected()) {
							countFailed++;
							break;
						}

						boolean useNumber = scores.size() > 1;
						It<Score> scoresIt = new It<Score>(scores);

						for (Score score : scoresIt) {
							Sequence seq = convertToSequence(score, false, false).sequence;
							String number = (useNumber ? ("-" + (scoresIt.getIndex() + 1)) : "");
							String newPath = filePath;

							if (filePath.toLowerCase().endsWith(".xml") || filePath.toLowerCase().endsWith(".mxl")) {
								newPath = newPath.substring(0, filePath.length() - 4);
							}

							newPath += (number + ".mid");
							MidiSystem.write(seq, 1, new File(newPath));
							countOK++;
						}
					}
				} catch (IOException ex) {
					countFailed++;

					if (chkCancel.isSelected()) {
						break;
					}
				}
			}

			app().showMessageDialog(
				Lang.get(Voc.DirectoryConversionResult, "" + countOK, "" + countFailed));
		}
	}

}
