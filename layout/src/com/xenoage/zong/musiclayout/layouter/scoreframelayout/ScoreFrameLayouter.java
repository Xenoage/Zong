package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.addNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStamper.lyricStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.SlurStamper.slurStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.StaffStamper.staffStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.TupletStamper.tupletStamper;
import static com.xenoage.zong.musiclayout.stamper.BarlinesStamper.barlinesStamper;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.beamStamper;
import static com.xenoage.zong.musiclayout.stamper.ChordStamper.chordStamper;
import static com.xenoage.zong.musiclayout.stamper.DirectionStamper.directionStamper;
import static com.xenoage.zong.musiclayout.stamper.MeasureStamper.measureStamper;
import static com.xenoage.zong.musiclayout.stamper.PartNameStamper.partNameStamper;
import static com.xenoage.zong.musiclayout.stamper.VoiceStamper.voiceStamper;
import static com.xenoage.zong.musiclayout.stamper.VoltaStamper.voltaStamper;
import static com.xenoage.zong.musiclayout.stamper.WedgeStamper.wedgeStamper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.continued.ContinuedVolta;
import com.xenoage.zong.musiclayout.continued.ContinuedWedge;
import com.xenoage.zong.musiclayout.continued.OpenVolta;
import com.xenoage.zong.musiclayout.continued.OpenWedges;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.cache.OpenBeamsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenLyricsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenSlursCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;
import com.xenoage.zong.musiclayout.layouter.cache.util.SlurCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.LastLyrics;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stamper.PartNameStamper;
import com.xenoage.zong.musiclayout.stamper.StamperContext;
import com.xenoage.zong.musiclayout.stamper.WedgeStamper;
import com.xenoage.zong.musiclayout.stampings.BracketStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.SlurStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.VoltaStamping;
import com.xenoage.zong.musiclayout.stampings.WedgeStamping;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This strategy creates the {@link ScoreFrameLayout}
 * for a given {@link FrameSpacing}.
 * 
 * TODO: TIDY or redesign. This is probably the ugliest
 * class in the whole project.
 * 
 * @author Andreas Wenger
 */
public class ScoreFrameLayouter {
	
	public static final ScoreFrameLayouter scoreFrameLayouter = new ScoreFrameLayouter();


	/**
	 * Creates a {@link ScoreFrameLayout} from the given {@link FrameSpacing}.
	 * 
	 * @param continuedElements  unclosed elements from the last frame, like slurs
	 *                           spanning over more than one frame
	 */
	public ScoreFrameLayout computeScoreFrameLayout(FrameSpacing frame, int frameIndex,
		Notations notations, List<ContinuedElement> unclosedElements, Context layouterContext,
		Map<Beam, BeamSpacing> beamsSpacing) {
		
		layouterContext.saveMp();
		
		Score score = layouterContext.score;
		SymbolPool symbols = layouterContext.symbols;
		LayoutSettings settings = layouterContext.settings;
		
		StamperContext context = new StamperContext();
		context.layouter = layouterContext;
		context.notations = notations;
		
		ScoreHeader header = score.getHeader();
		int stavesCount = score.getStavesCount();
		StavesList stavesList = score.getStavesList();
		ArrayList<StaffStamping> staffStampsPool = alist();
		ArrayList<Stamping> otherStampsPool = alist();

		//default lyric style
		FormattedTextStyle defaultLyricStyle = new FormattedTextStyle(score.getFormat().getLyricFont());

		//caches
		OpenBeamsCache openBeamsCache = new OpenBeamsCache();
		OpenSlursCache openCurvedLinesCache = new OpenSlursCache();
		OpenWedges openWedges = new OpenWedges();
		OpenLyricsCache openLyricsCache = new OpenLyricsCache();
		LastLyrics lastLyrics = new LastLyrics();
		OpenTupletsCache openTupletsCache = new OpenTupletsCache();
		OpenVolta openVolta = new OpenVolta();

		//add continued elements
		for (ContinuedElement ce : unclosedElements) {
			if (ce instanceof ContinuedSlur) {
				openCurvedLinesCache.add(SlurCache.createContinued((ContinuedSlur) ce));
			}
			else if (ce instanceof ContinuedVolta) {
				openVolta.volta = (ContinuedVolta) ce;
			}
			else if (ce instanceof ContinuedWedge) {
				openWedges.wedges.add((ContinuedWedge) ce);
			}
		}

		//create staff stampings
		StaffStampings staffStampings = staffStamper.createStaffStampings(score, frame);
		staffStampings.addAllTo(staffStampsPool);

		//go through the systems
		for (int iSystem : range(frame.getSystems())) {
			context.systemIndex = iSystem;
			SystemSpacing system = frame.getSystems().get(iSystem);
			
			List<StaffStamping> systemStaves = staffStampings.getAllOfSystem(iSystem);
			StaffStamping systemFirstStaff = getFirst(systemStaves);
			
			//add the part names (first system) or part abbreviations (other systems)
			int iStaffInPart = 0;
			for (Part part : stavesList.getParts()) {
				PartNameStamper.Style style = (frameIndex == 0 && iSystem == 0 ?
					PartNameStamper.Style.Full : PartNameStamper.Style.Abbreviated);
				addNotNull(otherStampsPool, partNameStamper.stamp(part, iStaffInPart, systemStaves, style));
				iStaffInPart += part.getStavesCount();
			}

			//create the brackets at the beginning of the system
			for (BracketGroup bracketGroup : stavesList.getBracketGroups()) {
				StavesRange r = bracketGroup.getStaves();
				otherStampsPool.add(new BracketStamping(systemStaves.get(r.getStart()), systemStaves.get(r
					.getStop()), system.getMarginLeftMm() - 1.4f, bracketGroup.getStyle()));
			}

			//create the barlines and measure numbers
			otherStampsPool.addAll(barlinesStamper.stamp(system, systemStaves, score));

			//fill the staves
			for (int iStaff : range(stavesCount)) {
				layouterContext.mp = layouterContext.mp.withStaff(iStaff);
				context.staff = systemStaves.get(iStaff);
				float xMm = context.staff.positionMm.x;

				for (int iMeasure : range(system.columns)) {
					int globalMeasureIndex = system.getStartMeasureIndex() + iMeasure;
					layouterContext.mp = layouterContext.mp.withMeasure(globalMeasureIndex);
					
					ColumnSpacing measureColumnSpacing = system.columns.get(iMeasure);
					MeasureSpacing measure = measureColumnSpacing.getMeasures().get(iStaff);

					//add leading spacing elements, if available
					otherStampsPool.addAll(measureStamper.stampLeading(measure, xMm, context));

					//add directions
					otherStampsPool.addAll(directionStamper.stamp(context));

					//add measure elements within this measure
					float voicesXMm = xMm + measureColumnSpacing.getLeadingWidthMm();
					otherStampsPool.addAll(measureStamper.stampMeasure(measure, voicesXMm, context));

					//add voice elements within this measure
					otherStampsPool.addAll(voiceStamper.stampVoices(measure, voicesXMm, context));
					
					xMm += measureColumnSpacing.getWidthMm();
				}

			}

			//create all voltas in this system, including open voltas from the last system
			otherStampsPool.addAll(voltaStamper.stampSystem(systemFirstStaff, openVolta,
				header, defaultLyricStyle));

			//create all wedges in this system
			otherStampsPool.addAll(wedgeStamper.stampSystem(system, score, staffStampings, openWedges));

		}

		//create the collected beams
		otherStampsPool.addAll(createBeams(beamsSpacing, openBeamsCache));

		//create the collected ties and slurs
		otherStampsPool.addAll(createTiesAndSlurs(openCurvedLinesCache, staffStampings, frame
			.getSystems().size()));

		//create the open lyric underscore lines
		for (Tuple3<StaffTextStamping, NoteheadStamping, Integer> openUnderscore : openLyricsCache
			.getUnderscores()) {
			//TODO: fetch style efficiently
			FormattedTextStyle style = defaultLyricStyle;
			FormattedTextElement firstElement = openUnderscore.get1().getText().getFirstParagraph().getElements().getFirst();
			if (firstElement instanceof FormattedTextString) {
				style = ((FormattedTextString) firstElement).getStyle();
			}
			otherStampsPool.addAll(lyricStamper.createUnderscoreStampings(openUnderscore.get1(),
				openUnderscore.get2(), style, staffStampings.getAllOfStaff(openUnderscore.get3())));
		}

		//create tuplet brackets/numbers
		for (Tuplet tuplet : openTupletsCache) {
			otherStampsPool.add(tupletStamper.createTupletStamping(tuplet, openTupletsCache,
				symbols));
		}

		//collect elements that have to be continued on the next frame
		ArrayList<ContinuedElement> continuedElements = alist();
		for (SlurCache clc : openCurvedLinesCache) {
			continuedElements.add(clc.getContinuedCurvedLine());
		}
		if (openVolta.volta != null)
			continuedElements.add(openVolta.volta);
		continuedElements.addAll(openWedges.wedges);

		layouterContext.restoreMp();
		return new ScoreFrameLayout(frame, staffStampsPool, otherStampsPool, continuedElements);
	}

	/**
	 * Returns the stampings for the given {@link Chord}.
	 * The given {@link OpenBeamsCache}, {@link OpenSlursCache},
	 * {@link OpenLyricsCache}, {@link LastLyrics} and {@link OpenTupletsCache} may be modified.
	 */
	private List<Stamping> createChordStampings(ChordNotation chord, float positionX,
		StaffStamping staff, int staffIndex, int systemIndex, FormattedTextStyle defaultLyricStyle,
		OpenBeamsCache openBeamsCache, OpenSlursCache openCurvedLinesCache,
		OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache,
		Score score, SymbolPool symbolPool, LayoutSettings layoutSettings, Notations notations) {
		
		ArrayList<Stamping> ret = alist();
		Chord chordElement = chord.getElement();

		//noteheads, leger lines, dots, accidentals, stem, flags, articulations
		ChordStampings chordSt = chordStamper.create(chord, positionX,
			staff, symbolPool, layoutSettings);
		chordSt.addAllTo(ret);

		//beam  
		if (chordSt.stem != null) {
			//if the chord belongs to a beam, add the stem to
			//the corresponding list of beamed stems, so that the
			//beam can be created later. the middle stems were not stamped
			//yet, also remember them.
			Beam beam = chord.element.getBeam();
			if (beam != null) {
				BeamNotation beamNot = (BeamNotation) notations.get(beam);
				if (beamNot == null) {
					//GOON
					System.out.println("unknown notation for beam at " + beam.getMP());
				}
				else {
					BeamedStemStampings bss = openBeamsCache.get(beamNot);
					int chordIndex = beam.getWaypointIndex(chordElement);
					bss.stems[chordIndex] = chordSt.stem;
					openBeamsCache.set(beamNot, bss);
				}
			}
		}

		//ties and slurs
		for (Slur slur : it(chordElement.getSlurs())) {
			SlurWaypoint wp = slur.getWaypoint(chordElement);
			WaypointPosition pos = slur.getWaypointPosition(chordElement);
			int noteIndex = notNull(wp.getNoteIndex(), 0); //TODO: choose top/bottom
			NoteheadStamping notehead = chordSt.noteheads[noteIndex];
			//define the placement: above or below (TODO: better strategy)
			VSide side = VSide.Top;
			if (pos == WaypointPosition.Start) {
				if (slur.getSide() != null) {
					//use custom side
					side = slur.getSide();
				}
				else {
					//use default side:
					//for all notes over line position 3, use above, else below
					side = (notehead.position.lp > 3 ? VSide.Top : VSide.Bottom);
				}
			}
			//compute position
			if (pos == WaypointPosition.Start) {
				//create start information
				float distance = slurStamper.computeAdditionalDistance(chord, side);
				SlurCache tiedChords = SlurCache.createNew(slur, side, staffIndex, notehead, distance,
					systemIndex);
				openCurvedLinesCache.add(tiedChords);
			}
			else {
				//create stop information
				SlurCache tiedChords = openCurvedLinesCache.get(slur);
				if (tiedChords == null) {
					//start notehead of this tie is unknown (must be from some preceding frame), which was forgotten
					//ignore it. TODO: warning
				}
				else {
					float distance = slurStamper.computeAdditionalDistance(chord,
						tiedChords.getSide());
					tiedChords.setStop(notehead, distance, systemIndex);
				}
			}
		}

		//lyric
		List<Lyric> lyrics = chordElement.getLyrics();
		if (lyrics.size() > 0) {
			float baseLine = -10;

			for (Lyric lyric : lyrics) {
				if (lyric != null) {
					SyllableType lyricType = lyric.getSyllableType();
					StaffTextStamping lastLyric = lastLyrics.get(staffIndex, lyric.getVerse());

					if (lyricType == SyllableType.Extend) {
						//extend
						if (lastLyric != null) //TODO: frame breaks...
						{
							//remember it
							openLyricsCache.setUnderscore((Lyric) lastLyric.getElement(), lastLyric,
								chordSt.noteheads[0]/* TODO*/, staffIndex);
						}
					}
					else {
						//normal lyric

						//create text stamping
						StaffTextStamping sts = lyricStamper.createSyllableStamping(lyric,
							defaultLyricStyle, staff, chordSt.noteheads[0]/* TODO*/.position.xMm,
							baseLine);
						ret.add(sts);

						//when middle or end syllable, add a hypen between the preceding syllable and this syllable
						if (lastLyric != null) //TODO: frame breaks...
						{
							if (lyricType == SyllableType.Middle || lyricType == SyllableType.End) {
								StaffTextStamping hyphenStamping = lyricStamper.createHyphenStamping(
									lastLyric, sts, defaultLyricStyle);
								ret.add(hyphenStamping);
							}
						}

						//remember this lyric as the currently last one in the current staff and verse
						lastLyrics.set(staffIndex, lyric.getVerse(), sts);

					}
				}

				baseLine += -5;
			}
		}

		//directions
		ret.addAll(directionStamper.createForChord(chordElement, chordSt, symbolPool));

		//tuplet
		Tuplet tuplet = chordElement.getTuplet();
		if (tuplet != null) {
			openTupletsCache.addChord(chordElement, tuplet, chordSt);
		}

		return ret;
	}

	/**
	 * Creates the beams collected in the given {@link OpenBeamsCache}.
	 * TIDY
	 */
	private List<Stamping> createBeams(Map<Beam, BeamSpacing> beams, OpenBeamsCache openBeamsCache) {
		ArrayList<Stamping> ret = alist(openBeamsCache.size());
		for (BeamedStemStampings beam : openBeamsCache) {
			CollectionUtils.addAll(ret, beamStamper.createBeamStampings(
				beams.get(beam.beamNotation.element), beam.firstStem().parentStaff, beam.lastStem().parentStaff));
		}
		return ret;
	}

	/**
	 * Creates the ties and slurs collected in the given {@link OpenSlursCache}.
	 * All closed slurs are removed from the cache. The unclosed slurs (which have to
	 * be continued on the next frame) remain in the cache.
	 */
	private List<SlurStamping> createTiesAndSlurs(OpenSlursCache openCurvedLinesCache,
		StaffStampings staffStampings, int systemsCount) {
		LinkedList<SlurStamping> ret = new LinkedList<SlurStamping>();
		for (Iterator<SlurCache> itCL = openCurvedLinesCache.iterator(); itCL.hasNext();) {
			boolean simpleLine = false; //current curved line simple (within one staff)?
			SlurCache cl = itCL.next();
			int middleSlurStartIndex = 0;
			//if the start is known, begin (and if possible end) the slur in its staff
			if (cl.isStartKnown()) {
				Tuple2<SlurStamping, Boolean> startCLS = slurStamper.createSlurStampingStart(cl);
				ret.add(startCLS.get1());
				if (startCLS.get2() == false) {
					//curved line is finished, remove cache
					simpleLine = true;
					itCL.remove();
				}
				else {
					//curved lines is continued in next system
					middleSlurStartIndex = cl.getStartSystem() + 1;
				}
			}
			//if not a simple line, compute and stamp middle slurs and end slur
			if (!simpleLine) {
				//if the stop notehead is known, compute the system up zo where middle staff slurs (if any)
				//have to be stamped and stamp them
				int middleSlurStopIndex = systemsCount - 1;
				if (cl.isStopKnown()) {
					middleSlurStopIndex = cl.getStopSystem() - 1;
				}
				for (int iSystem = middleSlurStartIndex; iSystem <= middleSlurStopIndex; iSystem++) {
					SlurStamping cls = slurStamper.createSlurStampingMiddle(
						cl.getContinuedCurvedLine(), staffStampings.get(iSystem, cl.getStaffIndex()));
					if (cls != null)
						ret.add(cls);
				}
				//create stop slur (if any)
				if (!simpleLine && cl.isStopKnown()) {
					ret.add(slurStamper.createSlurStampingStop(cl));
					//curved line is finished, remove cache
					itCL.remove();
				}
			}
		}
		return ret;
	}



	

}
