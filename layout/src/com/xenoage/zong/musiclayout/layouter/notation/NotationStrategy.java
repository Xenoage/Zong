package com.xenoage.zong.musiclayout.layouter.notation;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;

import java.util.List;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This strategy computes information about the layout
 * of {@link MusicElement}s, e.g. the needed horizontal space
 * in interline spaces, for musical elements like chords,
 * rests, clefs and so on.
 *
 * @author Andreas Wenger
 */
@SuppressWarnings("unused")
public final class NotationStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final StemDirectionStrategy stemDirectionStrategy;
	private final NotesAlignmentStrategy notesAlignmentStrategy;
	private final AccidentalsAlignmentStrategy accidentalsAlignmentStrategy;
	private final StemAlignmentStrategy stemAlignmentStrategy;
	private final ArticulationsAlignmentStrategy articulationsAlignmentStrategy;
	
	private TextMeasurer textMeasurer;


	/**
	 * Creates a new instance of a {@link NotationStrategy}.
	 */
	public NotationStrategy(StemDirectionStrategy stemDirectionStrategy,
		NotesAlignmentStrategy notesAlignmentStrategy,
		AccidentalsAlignmentStrategy accidentalsAlignmentStrategy,
		StemAlignmentStrategy stemAlignmentStrategy,
		ArticulationsAlignmentStrategy articulationsAlignmentStrategy) {
		this.stemDirectionStrategy = stemDirectionStrategy;
		this.notesAlignmentStrategy = notesAlignmentStrategy;
		this.accidentalsAlignmentStrategy = accidentalsAlignmentStrategy;
		this.stemAlignmentStrategy = stemAlignmentStrategy;
		this.articulationsAlignmentStrategy = articulationsAlignmentStrategy;
		this.textMeasurer = checkNotNull(platformUtils().getTextMeasurer());
	}

	/**
	 * Computes the {@link Notation}s of all {@link MusicElement}s
	 * in the given {@link Score}, using the given {@link SymbolPool}.
	 */
	public NotationsCache computeNotations(Score score, SymbolPool symbolPool,
		LayoutSettings layoutSettings) {
		NotationsCache ret = new NotationsCache();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = score.getColumn(iMeasure);

			//column elements (for each staff)
			for (ColumnElement element : score.getHeader().getColumnHeader(iMeasure).getColumnElements()) {
				for (int iStaff : range(measureColumn)) {
					Notation notation = computeNotation(element, iStaff, score, symbolPool, layoutSettings);
					if (notation != null)
						ret.add(notation, iStaff);
				}
			}

			for (int iStaff : range(measureColumn)) {
				Measure measure = measureColumn.get(iStaff);
				//measure elements
				for (BeatE<MeasureElement> element : measure.getMeasureElements()) {
					Notation notation = computeNotation(element.element, iStaff, score, symbolPool,
						layoutSettings);
					if (notation != null)
						ret.add(notation);
				}
				for (int iVoice : range(measure.getVoices())) {
					Voice voice = measure.getVoice(iVoice);
					//voice elements
					Fraction beat = _0;
					for (VoiceElement element : voice.getElements()) {
						Notation notation = computeNotation(element, iStaff, score, symbolPool, layoutSettings);
						if (notation != null)
							ret.add(notation);
						beat = beat.add(element.getDuration());
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Computes the {@link Notation} of the given {@link MusicElement} at the
	 * given staff in the given {@link Score}, using the given {@link SymbolPool}.
	 */
	private Notation computeNotation(MPElement element, int staff, Score score,
		SymbolPool symbolPool, LayoutSettings layoutSettings) {
		//note: we can not read the MP of the element from the score in all cases,
		//since column elements have no single MP, but are used over the whole column.
		//so we provide the staff as a parameter

		//not nice, but we want to leave the logic within this class.
		//multiple dispatch would be needed.
		Notation notation;
		if (element instanceof Chord)
			notation = computeChord((Chord) element, null, score, layoutSettings);
		else if (element instanceof Clef)
			notation = computeClef((Clef) element, layoutSettings);
		else if (element instanceof Time)
			notation = computeTime((Time) element, symbolPool);
		else if (element instanceof Rest)
			notation = computeRest((Rest) element, layoutSettings);
		else if (element instanceof TraditionalKey)
			notation = computeTraditionalKey((TraditionalKey) element,
				score.getClef(MP.getMP(element).withStaff(staff), Interval.Before),
				layoutSettings);
		else if (element instanceof Direction) {
			//directions need no notations at the moment
			notation = null;
		}
		else if (element instanceof InstrumentChange) {
			//ignore instrument change
			notation = null;
		}
		else if (element instanceof Barline) {
			//ignore barline
			notation = null;
		}
		else if (element instanceof Break) {
			//ignore break
			notation = null;
		}
		else if (element instanceof Volta) {
			//ignore volta
			notation = null;
		}
		else {
			notation = computeUnsupported(element);
		}

		return notation;
	}

	/**
	 * Computes the {@link Notation} of the given {@link Chord} at the
	 * given {@link IMP} ,
	 * using the given {@link StemDirection} in the given {@link Score}.
	 */
	public ChordNotation computeChord(Chord chord, StemDirection stemDirection,
		Score score, LayoutSettings layoutSettings) {
		//get the music context and the parent voice
		MP mp = MP.getMP(chord);
		MusicContext mc = score.getMusicContext(mp, BeforeOrAt, Before);
		//compute the notation
		return computeChord(chord, mc, score.getInterlineSpace(mp), score.getFormat().getLyricFont(),
			stemDirection, layoutSettings);
	}

	/**
	 * Computes the layout of a {@link Chord}.
	 */
	private ChordNotation computeChord(Chord chord, MusicContext mc, float interlineSpace,
		FontInfo lyricsFont, StemDirection stemDirection, LayoutSettings layoutSettings) {
		//grace or normal chord?
		boolean grace = chord.isGrace();
		ChordWidths chordWidths = (grace ? layoutSettings.graceChordWidths : layoutSettings.chordWidths);
		ChordSpacings spacings = (grace ? layoutSettings.spacings.graceChordSpacings
			: layoutSettings.spacings.normalChordSpacings);

		//stem direction
		if (stemDirection == null) {
			stemDirection = stemDirectionStrategy.computeStemDirection(chord, mc);
		}

		//notes alignment
		NotesAlignment notesAlignment = notesAlignmentStrategy.computeNotesAlignment(chord,
			stemDirection, chordWidths, mc);
		//accidentals alignment
		AccidentalsAlignment accidentalsAlignment = accidentalsAlignmentStrategy
			.computeAccidentalsAlignment(chord, notesAlignment, chordWidths, mc);
		float accidentalsWidth = (accidentalsAlignment != null ? accidentalsAlignment.getWidth() : 0);

		float leftSuspendedWidth = notesAlignment.getLeftSuspendedWidth();
		//symbol's width: width of the noteheads and dots
		float symbolWidth = notesAlignment.getWidth() - leftSuspendedWidth;
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
		StemAlignment stemAlignment = stemAlignmentStrategy.computeStemAlignment(chord.getStem(),
			notesAlignment, stemDirection, mc.getLinesCount(), grace ? layoutSettings.scalingGrace : 1);

		//compute articulations
		ArticulationsAlignment articulationsAlignment = articulationsAlignmentStrategy
			.computeArticulationsAlignment(chord, stemDirection, notesAlignment, mc.getLinesCount());

		return new ChordNotation(chord, new ElementWidth(frontGap, symbolWidth, rearGap, lyricWidth),
			notesAlignment, stemDirection, stemAlignment, accidentalsAlignment, articulationsAlignment);
	}

	/**
	* Computes the layout of a {@link Clef}, which is not in a leading spacing.
	* These clefs are usually drawn smaller.
	*/
	private ClefNotation computeClef(Clef clef, LayoutSettings ls) {
		return new ClefNotation(clef, new ElementWidth(0, ls.spacings.widthClef * ls.scalingClefInner,
			0), clef.getType().getLp(), ls.scalingClefInner);
	}

	/**
	* Computes the layout of a {@link Time}.
	*/
	private TimeNotation computeTime(Time time, SymbolPool symbolPool) {
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
	private RestNotation computeRest(Rest rest, LayoutSettings layoutSettings) {
		float width = layoutSettings.spacings.normalChordSpacings.getWidth(rest.getDuration());
		return new RestNotation(rest, new ElementWidth(width));
	}

	/**
	* Computes the layout of a {@link TraditionalKey}.
	*/
	public TraditionalKeyNotation computeTraditionalKey(TraditionalKey key, Clef contextClef,
		LayoutSettings layoutSettings) {
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

	/**
	* Computes the layout of an unknown {@link MusicElement}.
	*/
	private Notation computeUnsupported(MusicElement element) {
		throw new IllegalArgumentException("Unsupported MusicElement of type " + element.getClass());
	}

}
