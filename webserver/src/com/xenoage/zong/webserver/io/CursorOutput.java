package com.xenoage.zong.webserver.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.io.midi.out.MidiConverter.Options.optionsForFileExport;

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
		val seq = MidiConverter.convertToSequence(score, optionsForFileExport,
				new JseMidiSequenceWriter());

		//save time map
		JsonArray jsonMPs = new JsonArray();
		val timeMap = seq.getTimeMap();
		for (int iRep : range(timeMap.getRepetitionsCount())) {
			for (val time : timeMap.getTimesSorted(iRep)) {
				val midiTime = timeMap.getByRepTime(iRep, time);
				JsonObject jsonMP = new JsonObject();
				jsonMP.addProperty("measure", time.measure);
				jsonMP.addProperty("beat", "" + time.beat);
				jsonMP.addProperty("ms", midiTime.ms);
				jsonMPs.add(jsonMP);
			}
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
					
					for (SystemSpacing systemSpacing : sfl.getFrameSpacing().getSystems()) {
						//read system data
						int systemIndex = systemCount + systemSpacing.getSystemIndexInFrame();
						while (systems.size() - 1 < systemIndex)
							systems.add(new System());
						
						System system = systems.get(systemIndex);
						system.page = iPage;
						system.top = (offsetY + systemSpacing.offsetYMm) / pageSize.height;
						system.bottom = (offsetY + systemSpacing.offsetYMm + systemSpacing.getHeightMm()) / pageSize.height;
						
						//read measure beats
						float systemOffsetX = systemSpacing.marginLeftMm;
						for (int iMeasure : systemSpacing.getMeasures()) {
							Measure measure = measures.get(iMeasure);
							measure.system = systemIndex;
							
							measure.left = (offsetX + systemOffsetX + systemSpacing.getMeasureStartMm(iMeasure)) / pageSize.width;
							measure.right = (offsetX + systemOffsetX + systemSpacing.getMeasureEndMm(iMeasure)) / pageSize.width;
							for (BeatOffset bo : systemSpacing.getColumn(iMeasure).getBeatOffsets()) {
								measure.beats.put(bo.getBeat(), (offsetX + systemOffsetX + bo.getOffsetMm()) /
									pageSize.width);
							}
						}
						
					}
					systemCount += sfl.getFrameSpacing().getSystems().size();
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
		for (int iRep : range(timeMap.getRepetitionsCount())) {
			for (val time : timeMap.getTimesSorted(iRep)) {
				val midiTime = timeMap.getByRepTime(iRep, time);
				JsonObject jsonTC = new JsonObject();
				jsonTC.addProperty("time", midiTime.ms);
				Measure measure = measures.get(time.measure);
				System system = systems.get(measure.system);
				jsonTC.addProperty("page", system.page);
				jsonTC.addProperty("top", system.top);
				jsonTC.addProperty("left", measure.beats.get(time.beat));
				jsonTC.addProperty("bottom", system.bottom);
				jsonTCs.add(jsonTC);
			}
		}
		ret.add("timecursors", jsonTCs);

		return ret;
	}

}
