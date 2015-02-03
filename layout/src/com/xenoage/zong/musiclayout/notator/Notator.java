package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.musiclayout.notator.chord.AccidentalsNotator.accidentalsNotator;
import static com.xenoage.zong.musiclayout.notator.chord.ArticulationsNotator.articulationsNotator;
import static com.xenoage.zong.musiclayout.notator.chord.NotesNotator.notesNotator;
import static com.xenoage.zong.musiclayout.notator.chord.StemNotator.stemNotator;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDirector.stemDirector;

import java.util.Map;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Creates layout information about the layout of {@link MusicElement}s,
 * so called {@link Notation}s.
 * 
 * For example, the detailled positioning for musical elements like chords,
 * rests, clefs and so on is computed.
 *
 * @author Andreas Wenger
 */
public final class Notator {
	
	private Score score;
	private SymbolPool symbolPool;
	private LayoutSettings layoutSettings;
	private TextMeasurer textMeasurer;
	
	private NotationsCache cache;
	

	/**
	 * Creates a new {@link Notator} for the given score and settings.
	 */
	public static Notator notator(Score score, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		Notator notator = new Notator();
		notator.score = score;
		notator.symbolPool = symbolPool;
		notator.layoutSettings = layoutSettings;
		notator.textMeasurer = checkNotNull(platformUtils().getTextMeasurer());
		notator.cache = new NotationsCache();
		return notator;
	}
	
	private Notator() {
	}

	/**
	 * Computes the {@link Notation}s of all elements.
	 */
	public NotationsCache computeAll() {
		cache = new NotationsCache();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = score.getColumn(iMeasure);

			//column elements (for each staff)
			for (ColumnElement element : score.getHeader().getColumnHeader(iMeasure).getColumnElements()) {
				for (int iStaff : range(measureColumn)) {
					Notation notation = compute(element, iStaff);
					cache.add(notation, iStaff);
				}
			}

			for (int iStaff : range(measureColumn)) {
				Measure measure = measureColumn.get(iStaff);
				//measure elements
				for (BeatE<MeasureElement> element : measure.getMeasureElements()) {
					Notation notation = compute(element.element, iStaff);
					cache.add(notation);
				}
				for (int iVoice : range(measure.getVoices())) {
					Voice voice = measure.getVoice(iVoice);
					//voice elements
					for (VoiceElement element : voice.getElements()) {
						Notation notation = compute(element, iStaff);
						cache.add(notation);
					}
				}
			}
		}
		return cache;
	}

	/**
	 * Computes the {@link Notation} of the given element in the given staff.
	 */
	private Notation compute(MPElement element, int staff) {
		//note: we can not read the MP of the element from the score in all cases,
		//since column elements have no single MP, but are used over the whole column.
		//so we provide the staff as a parameter

		//not nice, but we want to leave the logic within this class.
		//multiple dispatch would be needed.
		Notation notation = null;
		switch (element.getMusicElementType()) {
			case Chord:
				notation = computeChord((Chord) element, ((Chord) element).getStem().getDirection());
				break;
			case Clef:
				notation = computeClef((Clef) element);
				break;
			case Time:
				notation = computeTime((Time) element);
				break;
			case Rest:
				notation = computeRest((Rest) element);
				break;
			case TraditionalKey:
				notation = computeTraditionalKey((TraditionalKey) element,
					score.getClef(MP.getMP(element).withStaff(staff), Interval.Before));
				break;
			default:
				//other elements need no notations at the moment
		}
		return notation;
	}

	/**
	 * Computes the {@link ChordNotation} of the given {@link Chord},
	 * using the given {@link StemDirection}.
	 */
	public ChordNotation computeChord(Chord chord, StemDirection stemDirection) {
		//get the music context and the parent voice
		MP mp = MP.getMP(chord);
		MusicContext mc = score.getMusicContext(mp, BeforeOrAt, Before);
		//compute the notation
		return computeChord(chord, mc, score.getInterlineSpace(mp), score.getFormat().getLyricFont(),
			stemDirection);
	}

	/**
	 * Computes the layout of a {@link Chord}.
	 */
	private ChordNotation computeChord(Chord chord, MusicContext mc, float interlineSpace,
		FontInfo lyricsFont, StemDirection stemDirection) {
		
		//grace or normal chord?
		boolean grace = chord.isGrace();
		ChordWidths chordWidths = (grace ? layoutSettings.graceChordWidths : layoutSettings.chordWidths);
		ChordSpacings spacings = (grace ? layoutSettings.spacings.graceChordSpacings
			: layoutSettings.spacings.normalChordSpacings);

		//compute stem direction
		if (stemDirection == StemDirection.Default) {
			//if stem direction was not computed yet, compute it now
			stemDirection = cache.getChord(chord).stemDirection;
			if (stemDirection == StemDirection.Default) {
				Map<Chord, StemDirection> computedStems = stemDirector.compute(chord);
				stemDirection = computedStems.get(chord);
				//also remember the other computed stems
				for (Chord computedChord : computedStems.keySet()) {
					cache.getChord(computedChord).stemDirection = computedStems.get(computedChord);
				}
			}
		}

		//chord displacement
		NotesNotation chordDisplacement = notesNotator.compute(
			chord, stemDirection, chordWidths, mc);
		//accidentals alignment
		AccidentalsNotation accidentalsAlignment = accidentalsNotator.compute(
			chord, chordDisplacement, chordWidths, mc);
		float accidentalsWidth = accidentalsAlignment.widthIs;

		float leftSuspendedWidth = (chordDisplacement.leftSuspended ? chordDisplacement.noteheadWidthIs : 0);
		//symbol's width: width of the noteheads and dots
		float symbolWidth = chordDisplacement.widthIs - leftSuspendedWidth;
		float frontGap = accidentalsWidth + leftSuspendedWidth;

		//rear gap: empty duration-dependent space behind the chord
		//minus the symbol's width
		float rearGap = spacings.getWidth(chord.getDisplayedDuration()) - symbolWidth;

		//lyric width
		float lyricWidth = 0;
		for (Lyric lyric : chord.getLyrics()) {
			if (lyric != null && lyric.getText() != null) {
				//width of lyric in interline spaces
				//OBSOLETE float l = textMeasurer(lyricsFont, lyric.getText()).getWidth() / interlineSpace;
				FormattedText lyricText = styleText(lyric.getText(), new FormattedTextStyle(lyricsFont));
				float l = lyricText.getWidth() / interlineSpace;

				//for start and end syllable, request "-" more space, for middle syllables "--"
				//TODO: unsymmetric - start needs space on the right, end on the left, ...
				SyllableType lyricType = lyric.getSyllableType();
				if (lyricType == SyllableType.Begin || lyricType == SyllableType.End) {
					l += textMeasurer.measure(lyricsFont, "-").getWidth() / interlineSpace;
				}
				else if (lyricType == SyllableType.Middle) {
					l += textMeasurer.measure(lyricsFont, "--").getWidth() / interlineSpace;
				}
				//save width of the widest lyric
				lyricWidth = Math.max(lyricWidth, l);
			}
		}

		//compute length of the stem (if any)
		StemNotation stemAlignment = stemNotator.compute(
			chord.getStem(), chordDisplacement, stemDirection, mc.getLinesCount(),
			grace ? layoutSettings.scalingGrace : 1);

		//compute articulations
		ArticulationsNotation articulationsAlignment = articulationsNotator.compute(
			chord, stemDirection, chordDisplacement, mc.getLinesCount());

		return new ChordNotation(chord, new ElementWidth(frontGap, symbolWidth, rearGap, lyricWidth),
			chordDisplacement, stemDirection, stemAlignment, accidentalsAlignment, articulationsAlignment);
	}

	/**
	* Computes the layout of a {@link Clef}, which is not in a leading spacing.
	* These clefs are usually drawn smaller.
	*/
	private ClefNotation computeClef(Clef clef) {
		LayoutSettings ls = layoutSettings;
		return new ClefNotation(clef, new ElementWidth(0, ls.spacings.widthClef * ls.scalingClefInner,
			0), clef.getType().getLp(), ls.scalingClefInner);
	}

	/**
	* Computes the layout of a {@link Time}.
	*/
	private TimeNotation computeTime(Time time) {
		//front and rear gap: 1 space
		float gap = 1f;
		//gap between digits: 0.1 space
		float digitGap = 0.1f;
		//the numbers of a normal time signature are centered.
		//the width of the time signature is the width of the wider number
		float numeratorWidth = 2;
		float denominatorWidth = 2;
		if (symbolPool != null) {
			numeratorWidth = symbolPool.computeNumberWidth(time.getType().getNumerator(), digitGap);
			denominatorWidth = symbolPool.computeNumberWidth(time.getType().getDenominator(), digitGap);
		}
		float width = Math.max(numeratorWidth, denominatorWidth);
		//create element layout
		float numeratorOffset = (width - numeratorWidth) / 2;
		float denominatorOffset = (width - denominatorWidth) / 2;
		return new TimeNotation(time, new ElementWidth(gap, width, gap), numeratorOffset,
			denominatorOffset, digitGap);
	}

	/**
	* Computes the layout of a {@link Rest}.
	*/
	private RestNotation computeRest(Rest rest) {
		float width = layoutSettings.spacings.normalChordSpacings.getWidth(rest.getDuration());
		return new RestNotation(rest, new ElementWidth(width));
	}

	/**
	* Computes the layout of a {@link TraditionalKey}.
	*/
	public TraditionalKeyNotation computeTraditionalKey(TraditionalKey key, Clef contextClef) {
		float width = 0;
		int fifth = key.getFifths();
		if (fifth > 0) {
			width = fifth * layoutSettings.spacings.widthSharp;
		}
		else {
			width = -fifth * layoutSettings.spacings.widthFlat;
		}
		return new TraditionalKeyNotation(key, new ElementWidth(0, width, 1),
			contextClef.getType().getLp(pi(0, 0, 4)), contextClef.getType().getKeySignatureLowestLp(fifth));
	}

}
