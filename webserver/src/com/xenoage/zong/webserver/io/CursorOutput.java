package com.xenoage.zong.webserver.io;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.sound.midi.Sequence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.MidiSequence;
import com.xenoage.zong.io.midi.out.MidiTime;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.MeasureMarks;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This class creates a JSON object which contains a mapping from time
 * (in sampled audio files) to cursor positions.
 * 
 * This is the format in detail:
 * 
 * <pre>{"mps":[   // map time to measure/beat
 *   {"measure":0, "beat":"0/1", "ms":0},  // ms: milliseconds in MP3/OGG file
 *   {"measure":0, "beat":"1/8", "ms":112},
 *   ... ],
 * "systems":[   // for each system its number, page, and y coordinates (ratio to page height)
 *   {"page":0, "number":0, "top":0.1, "bottom":0.4},
 *   {"page":0, "number":1, "top":0.5, "bottom":0.8"},
 *   ... ],
 * "measures":[  //the list of measures
 *   //for each measure: system index, left and right boundaries
 *   {"number":0, "system":0, "left":0.1, "right":0.22, "beats":[
 *     //beat and horizontal coordinate (ratio to page width; and relative to system start)
 *     {"at":"0/1", "x":0.21},  
 *     {"at":"1/8", "x":0.214},
 *     ... ]},
 *   ... ],
 * "timecursors":[  //simple mapping from time to cursor positions
 *   //for each beat (given in milliseconds), the page index, x-coordinate and y-coordinate
 *   //and height (ratio to page size, relative to the top-left corner of the page) of the cursor
 *   { time : 0.5, page : 0, top : 0.05, left : 0.08, bottom : 0.10 },
 *   ... ]}</pre>
 * 
 * @author Andreas Wenger
 */
public class CursorOutput {

	private class System {

		int page = 0;
		float top = Integer.MAX_VALUE;
		float bottom = Integer.MIN_VALUE;
	}

	private class Measure {

		int system = 0;
		float left = 0;
		float right = 0;
		HashMap<Fraction, Float> beats = new HashMap<Fraction, Float>();
	}


	public JsonObject write(ScoreDoc doc) {
		JsonObject ret = new JsonObject();

		//create midi sequence and mp mappings
		Score score = doc.getScore();
		MidiSequence<Sequence> seq = MidiConverter.convertToSequence(score, true, false,
			fr(1, 1), new JseMidiSequenceWriter());

		//save mp mappings
		JsonArray jsonMPs = new JsonArray();
		for (MidiTime time : seq.getTimePool()) {
			JsonObject jsonMP = new JsonObject();
			jsonMP.addProperty("measure", time.mp.measure);
			jsonMP.addProperty("beat", "" + time.mp.beat);
			jsonMP.addProperty("ms", time.ms);
			jsonMPs.add(jsonMP);
		}
		ret.add("mps", jsonMPs);

		//collect data
		int measuresCount = score.getMeasuresCount();
		ArrayList<System> systems = new ArrayList<System>();
		ArrayList<Measure> measures = new ArrayList<Measure>();
		for (int i = 0; i < measuresCount; i++) {
			measures.add(new Measure());
		}
		int systemCount = 0;
		Layout layout = doc.getLayout();
		for (int iPage : range(layout.getPages())) {
			Page page = layout.getPages().get(iPage);
			Size2f pageSize = page.getFormat().getSize();
			for (Frame frame : page.getFrames()) {
				if (frame instanceof ScoreFrame) {
					Point2f absPos = frame.getAbsolutePosition();
					float offsetX = absPos.x - frame.getSize().width / 2;
					float offsetY = absPos.y - frame.getSize().height / 2;
					ScoreFrameLayout sfl = ((ScoreFrame) frame).getScoreFrameLayout();
					for (StaffStamping ss : sfl.getStaffStampings()) {
						//read system data
						int systemIndex = systemCount + ss.getSystemIndex();
						while (systems.size() - 1 < systemIndex)
							systems.add(new System());
						System system = systems.get(systemIndex);
						system.page = iPage;
						system.top = Math.min((ss.position.y + offsetY) / pageSize.height, system.top);
						system.bottom = Math.max((ss.position.y + (ss.linesCount - 1) * ss.is + offsetY) /
							pageSize.height, system.bottom);
						//read measure marks
						//TODO: wrong calculation (though right result?!) - mm.getBeatOffsets() should
						//be relative to measure beginning
						
						/* GOON
						for (MeasureMarks mm : ss.staffMarks.getMeasureMarks()) {
							Measure measure = measures.get(ss.getStartMeasureIndex() + mm.getMeasure());
							measure.system = systemIndex;
							float staffOffset = ss.position.x;
							measure.left = (offsetX + staffOffset + mm.getStartMm()) / pageSize.width;
							measure.right = (offsetX + staffOffset + mm.getEndMm()) / pageSize.width;
							for (BeatOffset bo : mm.getBeatOffsets()) {
								measure.beats.put(bo.getBeat(), (offsetX + staffOffset + bo.getOffsetMm()) /
									pageSize.width);
							}
						} */
						
					}
					systemCount += sfl.getFrameArrangement().getSystems().size();
				}
			}
		}

		//save systems
		JsonArray jsonSystems = new JsonArray();
		for (int i = 0; i < systems.size(); i++) {
			System system = systems.get(i);
			JsonObject jsonSystem = new JsonObject();
			jsonSystem.addProperty("number", i);
			jsonSystem.addProperty("page", system.page);
			jsonSystem.addProperty("top", system.top);
			jsonSystem.addProperty("bottom", system.bottom);
			jsonSystems.add(jsonSystem);
		}
		ret.add("systems", jsonSystems);

		//save measures
		JsonArray jsonMeasures = new JsonArray();
		for (int i = 0; i < measuresCount; i++) {
			Measure measure = measures.get(i);
			JsonObject jsonMeasure = new JsonObject();
			jsonMeasure.addProperty("number", i);
			jsonMeasure.addProperty("system", measure.system);
			jsonMeasure.addProperty("left", measure.left);
			jsonMeasure.addProperty("right", measure.right);
			//beats
			JsonArray jsonBeats = new JsonArray();
			ArrayList<Fraction> sortedBeats = new ArrayList<Fraction>(measure.beats.keySet());
			Collections.sort(sortedBeats);
			for (Fraction beat : sortedBeats) {
				JsonObject jsonBeat = new JsonObject();
				jsonBeat.addProperty("at", "" + beat);
				jsonBeat.addProperty("x", measure.beats.get(beat));
				jsonBeats.add(jsonBeat);
			}
			jsonMeasure.add("beats", jsonBeats);
			jsonMeasures.add(jsonMeasure);
		}
		ret.add("measures", jsonMeasures);

		//save time cursors
		JsonArray jsonTCs = new JsonArray();
		for (MidiTime time : seq.getTimePool()) {
			JsonObject jsonTC = new JsonObject();
			jsonTC.addProperty("time", time.ms);
			Measure measure = measures.get(time.mp.measure);
			System system = systems.get(measure.system);
			jsonTC.addProperty("page", system.page);
			jsonTC.addProperty("top", system.top);
			jsonTC.addProperty("left", measure.beats.get(time.mp.beat));
			jsonTC.addProperty("bottom", system.bottom);
			jsonTCs.add(jsonTC);
		}
		ret.add("timecursors", jsonTCs);

		return ret;
	}

}
