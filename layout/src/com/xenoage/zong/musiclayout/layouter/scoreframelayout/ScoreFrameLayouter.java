package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.addNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.DirectionStamper.directionStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStamper.lyricStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.MusicElementStamper.musicElementStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.SlurStamper.slurStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.StaffStamper.staffStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.TupletStamper.tupletStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.VoltaStamper.voltaStamper;
import static com.xenoage.zong.musiclayout.stamper.BarlinesStamper.barlinesStamper;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.beamStamper;
import static com.xenoage.zong.musiclayout.stamper.ChordStamper.chordStamper;
import static com.xenoage.zong.musiclayout.stamper.PartNameStamper.partNameStamper;

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
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.continued.ContinuedVolta;
import com.xenoage.zong.musiclayout.continued.ContinuedWedge;
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
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;
import com.xenoage.zong.musiclayout.stamper.PartNameStamper;
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
		Notations notations, List<ContinuedElement> unclosedElements, Context context,
		Map<Beam, BeamSpacing> beamsSpacing) {
		
		Score score = context.score;
		SymbolPool symbols = context.symbols;
		LayoutSettings settings = context.settings;
		
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
		ArrayList<ContinuedVolta> openVoltasCache = alist();
		ArrayList<ContinuedWedge> openWedgesCache = alist();
		OpenLyricsCache openLyricsCache = new OpenLyricsCache();
		LastLyrics lastLyrics = new LastLyrics();
		OpenTupletsCache openTupletsCache = new OpenTupletsCache();

		//add continued elements
		for (ContinuedElement ce : unclosedElements) {
			if (ce instanceof ContinuedSlur) {
				openCurvedLinesCache.add(SlurCache.createContinued((ContinuedSlur) ce));
			}
			else if (ce instanceof ContinuedVolta) {
				openVoltasCache.add((ContinuedVolta) ce);
			}
			else if (ce instanceof ContinuedWedge) {
				openWedgesCache.add((ContinuedWedge) ce);
			}
		}

		//create staff stampings
		StaffStampings staffStampings = staffStamper.createStaffStampings(score, frame);
		staffStampings.addAllTo(staffStampsPool);

		//go through the systems
		for (int iSystem : range(frame.getSystems())) {
			SystemSpacing system = frame.getSystems().get(iSystem);
			List<StaffStamping> systemStaves = staffStampings.getAllOfSystem(iSystem);

			//add the part names (first system) or part abbreviations (other systems)
			int iStaff = 0;
			for (Part part : stavesList.getParts()) {
				PartNameStamper.Style style = (frameIndex == 0 && iSystem == 0 ?
					PartNameStamper.Style.Full : PartNameStamper.Style.Abbreviated);
				addNotNull(otherStampsPool, partNameStamper.stamp(part, iStaff, systemStaves, style));
				iStaff += part.getStavesCount();
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
			for (iStaff = 0; iStaff < stavesCount; iStaff++) {
				StaffStamping staff = systemStaves.get(iStaff);
				float xOffset = staff.positionMm.x;
				float interlineSpace = staff.is;

				for (int iMeasure = 0; iMeasure < system.columns.size(); iMeasure++) {
					int globalMeasureIndex = system.getStartMeasureIndex() + iMeasure;
					ColumnSpacing measureColumnSpacing = system.columns.get(iMeasure);
					MeasureSpacing measureStaffSpacing = measureColumnSpacing.getMeasures().get(iStaff);

					//add leading spacing elements, if available
					LeadingSpacing leadingSpacing = measureStaffSpacing.getLeadingSpacing();
					if (leadingSpacing != null) {
						for (ElementSpacing spacingElement : leadingSpacing.elements) {
							MusicElement element = spacingElement.getElement();
							if (element != null) {
								float x = xOffset + spacingElement.offsetIs * interlineSpace;
								Notation notation = notations.get(element, iStaff);
								if (notation == null)
									throw new RuntimeException("No notation for element " + element + " at " +
										MP.getMP(element));
								otherStampsPool.add(createMeasureElementStamping(notation, x, staff,
									symbols, settings));
							}
						}
					}

					//add directions - TODO
					BeatEList<Direction> directionsWithBeats = score.getMeasure(
						MP.atMeasure(iStaff, iMeasure + system.getStartMeasureIndex())).getDirections();
					if (directionsWithBeats != null) {
						//over first staff, also add tempo directions for the whole column
						if (iStaff == 0) {
							directionsWithBeats.addAll(header.getColumnHeader(globalMeasureIndex).getTempos());
						}
						for (BeatE<Direction> elementWithBeat : directionsWithBeats) {
							Direction element = elementWithBeat.element;
							Stamping stamping = null;
							if (element instanceof Tempo) {
								stamping = directionStamper.createTempo((Tempo) element,
									atBeat(iStaff, iMeasure + system.getStartMeasureIndex(), -1, elementWithBeat.beat),
									staff, symbols);
							}
							else if (element instanceof Dynamics) {
								stamping = directionStamper.createDynamics((Dynamics) element,
									atBeat(iStaff, iMeasure + system.getStartMeasureIndex(), -1, elementWithBeat.beat),
									staff, symbols);
							}
							else if (element instanceof Pedal) {
								stamping = directionStamper.createPedal((Pedal) element,
									atBeat(iStaff, iMeasure + system.getStartMeasureIndex(), -1, elementWithBeat.beat),
									staff, symbols);
							}
							else if (element instanceof Words) {
								stamping = directionStamper.createWords((Words) element,
									atBeat(iStaff, iMeasure + system.getStartMeasureIndex(), -1, elementWithBeat.beat),
									staff);
							}
							if (stamping != null)
								otherStampsPool.add(stamping);
						}
					}

					//now begin with the voices
					float voicesOffset = xOffset + measureColumnSpacing.getLeadingWidthMm();

					//add measure elements within this measure
					for (ElementSpacing spacingElement : measureStaffSpacing.getMeasureElementsSpacings()) {
						MusicElement element = spacingElement.getElement();
						if (element != null) {
							Notation notation = notations.get(element, iStaff);
							float x = voicesOffset + spacingElement.offsetIs * interlineSpace;
							if (element instanceof MeasureElement || element instanceof ColumnElement) {
								//clef, key, time, ...
								otherStampsPool.add(createMeasureElementStamping(notation, x, staff,
									symbols, settings));
							}
							else {
								throw new IllegalArgumentException("Notation not supported: " + notation);
							}
						}
					}

					//add voice elements within this measure
					for (VoiceSpacing voiceSpacing : measureStaffSpacing.getVoiceSpacings()) {
						List<ElementSpacing> voice = voiceSpacing.elements;

						//TODO
						//don't stamp leading rests in voice 2 - TODO: config?
						//boolean stampRests = (iVoice == 0);

						//create the voice elements
						for (ElementSpacing spacingElement : voice) {
							MusicElement element = spacingElement.getElement();
							if (element != null /* TODO && (stampRests || !(element instanceof Rest)) */) {
								Notation notation = notations.get(element, iStaff);
								float x = voicesOffset + spacingElement.offsetIs * interlineSpace;
								if (element instanceof Chord) {
									//chord
									otherStampsPool.addAll(createChordStampings((ChordNotation) notation, x, staff,
										iStaff, iSystem, defaultLyricStyle, openBeamsCache, openCurvedLinesCache,
										openLyricsCache, lastLyrics, openTupletsCache, score, symbols,
										settings, notations));
								}
								else if (element instanceof Rest) {
									//rest
									otherStampsPool.add(musicElementStamper.createRestStamping(
										(RestNotation) notation, x, staff, symbols));
								}
								else {
									throw new IllegalArgumentException("Notation not supported: " + notation);
								}
							}
						}

					}

					xOffset += measureColumnSpacing.getWidthMm();

				}

			}

			//create all voltas in this system
			otherStampsPool.addAll(createVoltas(iSystem, system, header, staffStampings, openVoltasCache,
				defaultLyricStyle));

			//create all wedges in this system
			otherStampsPool.addAll(createWedges(iSystem, system, score, staffStampings, openWedgesCache));

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
		continuedElements.addAll(openVoltasCache);
		continuedElements.addAll(openWedgesCache);

		return new ScoreFrameLayout(frame, staffStampsPool, otherStampsPool, continuedElements);
	}

	/**
	 * Returns the stamping for the given notation of a {@link MeasureElement},
	 * using an appropriate strategy.
	 */
	private Stamping createMeasureElementStamping(Notation notation, float positionX,
		StaffStamping staff, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		if (notation instanceof ClefNotation) {
			return musicElementStamper.createClefStamping((ClefNotation) notation, positionX,
				staff, symbolPool);
		}
		else if (notation instanceof TraditionalKeyNotation) {
			return musicElementStamper.createKeyStamping((TraditionalKeyNotation) notation,
				positionX, staff, symbolPool, layoutSettings);
		}
		else if (notation instanceof TimeNotation) {
			return musicElementStamper.createTimeStamping((TimeNotation) notation, positionX,
				staff, symbolPool);
		}
		else {
			throw new IllegalArgumentException("Notation not supported: " + notation);
		}
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

	/**
	 * Creates all volta stampings in the given system.
	 * All closed voltas are removed from the cache. The unclosed voltas (which have to
	 * be continued on the next system or frame) remain in the cache (or are added, if they are new).
	 */
	private List<VoltaStamping> createVoltas(int systemIndex, SystemSpacing system,
		ScoreHeader header, StaffStampings staffStampings, List<ContinuedVolta> openVoltasCache,
		FormattedTextStyle textStyle) {
		ArrayList<VoltaStamping> ret = alist();
		//find new voltas beginning in this system
		for (int iMeasure = 0; iMeasure < system.columns.size(); iMeasure++) {
			int scoreMeasure = system.getStartMeasureIndex() + iMeasure;
			ColumnHeader columnHeader = header.getColumnHeader(scoreMeasure);
			if (columnHeader.getVolta() != null) {
				openVoltasCache.add(new ContinuedVolta(columnHeader.getVolta(), scoreMeasure, 0)); //staff 0: TODO
			}
		}
		//draw voltas in the cache, and remove them if closed in this system
		int endMeasureIndex = system.getEndMeasureIndex();
		for (Iterator<ContinuedVolta> itV = openVoltasCache.iterator(); itV.hasNext();) {
			ContinuedVolta volta = itV.next();
			ret
				.add(voltaStamper.createVoltaStamping(volta.getMusicElement(),
					volta.startMeasureIndex, staffStampings.get(systemIndex, volta.getStaffIndex()),
					textStyle));
			if (volta.startMeasureIndex + volta.getMusicElement().getLength() - 1 <= endMeasureIndex) {
				//volta is closed
				itV.remove();
			}
		}
		return ret;
	}

	/**
	 * Creates all wedge stampings in the given system.
	 * All closed wedges are removed from the cache. The unclosed wedges (which have to
	 * be continued on the next system or frame) remain in the cache (or are added, if they are new).
	 */
	private List<WedgeStamping> createWedges(int systemIndex, SystemSpacing system,
		Score score, StaffStampings staffStampings, List<ContinuedWedge> openWedgesCache) {
		ArrayList<WedgeStamping> ret = alist();
		//find new wedges beginning in this staff
		for (int iStaff = 0; iStaff < score.getStavesCount(); iStaff++) {
			Staff staff = score.getStaff(iStaff);
			for (int iMeasure : system.getMeasureIndices()) {
				Measure measure = staff.getMeasures().get(iMeasure);
				for (Voice voice : measure.getVoices()) {
					for (MusicElement element : voice.getElements()) {
						if (element instanceof Wedge) {
							openWedgesCache.add(new ContinuedWedge((Wedge) element, iStaff));
						}
					}
				}
			}
		}
		//draw wedges in the cache, and remove them if closed in this system
		for (Iterator<ContinuedWedge> itW = openWedgesCache.iterator(); itW.hasNext();) {
			ContinuedWedge wedge = itW.next();
			ret.add(directionStamper.createWedgeStamping(wedge.getMusicElement(),
				staffStampings.get(systemIndex, wedge.getStaffIndex())));
			if (MP.getMP(wedge.getMusicElement().getWedgeEnd()).measure <= system.getEndMeasureIndex()) {
				//wedge is closed
				itW.remove();
			}
		}
		return ret;
	}

}
