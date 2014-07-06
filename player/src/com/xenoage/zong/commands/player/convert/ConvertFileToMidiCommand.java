package com.xenoage.zong.commands.player.convert;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.swing.JFileChooserUtil.createFileFilter;
import static com.xenoage.zong.SwingApp.app;
import static com.xenoage.zong.io.midi.out.MidiConverter.convertToSequence;
import static com.xenoage.zong.player.PlayerApplication.pApp;

import java.io.File;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.JFileChooser;

import com.xenoage.utils.base.filter.AllFilter;
import com.xenoage.utils.base.iterators.It;
import com.xenoage.utils.swing.JFileChooserUtil;
import com.xenoage.zong.Voc;
import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.FileFormats;


/**
 * This command lets the user select a MusicXML file which is then
 * converted into a MIDI file.
 * 
 * MusicXML files with multiple scores are also supported. In this case
 * each score gets its own MIDI file.
 * 
 * @author Andreas Wenger
 */
public class ConvertFileToMidiCommand
	extends Command
{

	@Override public void execute(CommandPerformer performer)
	{
		JFileChooser fc = new JFileChooser();
		JFileChooserUtil.setDirFromSettings(fc);
		fc.addChoosableFileFilter(createFileFilter(FileFormats.MusicXML.info));

		int ret = fc.showOpenDialog(app().getMainFrame());

		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String lastPath = file.getAbsolutePath();
			JFileChooserUtil.rememberDir(fc);

			List<Score> scores = pApp().loadMxlScores(lastPath, new AllFilter<String>());
			boolean useNumber = scores.size() > 1;
			It<Score> scoresIt = new It<Score>(scores);

			for (Score score : scoresIt) {
				Sequence seq = convertToSequence(score, false, false).sequence;
				String newPath = lastPath;
				String number = (useNumber ? ("-" + (scoresIt.getIndex() + 1)) : "");

				if (newPath.toLowerCase().endsWith(".xml") || //TIDY: share code: FilenameUtils.numberFiles
					newPath.toLowerCase().endsWith(".mxl")) {
					newPath = newPath.substring(0, newPath.length() - 4);
				}

				newPath += (number + ".mid");

				try {
					MidiSystem.write(seq, 1, new File(newPath));
				} catch (Exception ex) {
					handle(warning(Voc.ErrorSavingFile));
				}
			}
		}
	}

}
