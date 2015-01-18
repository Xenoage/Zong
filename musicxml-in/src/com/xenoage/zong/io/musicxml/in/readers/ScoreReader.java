package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.atStaff;
import static com.xenoage.zong.io.musicxml.in.util.CommandPerformer.execute;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAddUpTo;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.musicxml.in.util.MusicReaderException;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * This class reads the actual musical contents of
 * the given partwise MusicXML 2.0 document into a {@link Score}.
 * 
 * If possible, this reader works with the voice-element
 * to separate voices. TODO: if not existent or
 * used unreliably within a measure, implement this algorithm: 
 * http://archive.mail-list.com/musicxml/msg01673.html
 * 
 * TODO: Connect chords over staves, if they have the same
 * voice element but different staff element.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class ScoreReader {

	private final MxlScorePartwise doc;
	
	
	public void readToScore(Score score, boolean ignoreErrors) {
		Context context = new Context(score, new ReaderSettings(ignoreErrors));
		
		//create the measures of the parts
		It<MxlPart> mxlParts = it(doc.getParts());
		for (MxlPart mxlPart : mxlParts) {
			//create measures
			execute(new MeasureAddUpTo(score, mxlPart.getMeasures().size()));
			//initialize each measure with a C clef
			Part part = score.getStavesList().getParts().get(mxlParts.getIndex());
			StavesRange stavesRange = score.getStavesList().getPartStaffIndices(part);
			for (int staff : stavesRange.getRange()) {
				execute(new MeasureElementWrite(new Clef(ClefType.clefTreble),
					score.getMeasure(MP.atMeasure(staff, 0)), _0));
			}
		}
		
		//write a 4/4 measure and C key signature in the first measure
		execute(new ColumnElementWrite(new Time(TimeType.time_4_4), score.getColumnHeader(0), _0, null));
		execute(new ColumnElementWrite(new TraditionalKey(0), score.getColumnHeader(0), _0, null));
			
		//read the parts
		mxlParts = it(doc.getParts());
		for (MxlPart mxlPart : mxlParts) {
			//clear part-dependent context values
			context.beginNewPart(mxlParts.getIndex());
			//read the measures
			It<MxlMeasure> mxlMeasures = it(mxlPart.getMeasures());
			for (MxlMeasure mxlMeasure : mxlMeasures) {
				try {
					MeasureReader.readToContext(mxlMeasure, mxlMeasures.getIndex(), context);
				} catch (MusicReaderException ex) {
					throw new RuntimeException("Error at " + ex.getContext().toString(), ex);
				} catch (Exception ex) {
					throw new RuntimeException("Error (roughly) around " + context.toString(), ex);
				}
			}
		}

		//go through the whole score, and fill empty measures (that means, measures where
		//voice 0 has no single VoiceElement) with rests
		Fraction measureDuration = fr(1, 4);
		for (int iStaff = 0; iStaff < score.getStavesCount(); iStaff++) {
			Staff staff = score.getStaff(atStaff(iStaff));
			for (int iMeasure : range(staff.getMeasures())) {
				Measure measure = staff.getMeasure(iMeasure);
				Time newTime = score.getHeader().getColumnHeader(iMeasure).getTime();
				if (newTime != null) {
					//time signature has changed
					measureDuration = newTime.getType().getMeasureBeats();
				}
				if (measureDuration == null) { //senza misura
					measureDuration = fr(4, 4); //use whole rest
				}
				Voice voice0 = measure.getVoice(0);
				if (voice0.isEmpty()) {
					//TODO: "whole rests" or split. currently, also 3/4 rests are possible
					MP mp = atElement(iStaff, iMeasure, 0, 0);
					new VoiceElementWrite(score.getVoice(mp), mp, new Rest(measureDuration), null).execute();
				}
			}
		}
	}

}
