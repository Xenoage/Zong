package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.zong.core.music.StaffLines.staffLines;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.musiclayout.notator.chord.AccidentalsNotator.accidentalsNotator;
import static com.xenoage.zong.musiclayout.notator.chord.ArticulationsNotator.articulationsNotator;
import static com.xenoage.zong.musiclayout.notator.chord.NotesNotator.notesNotator;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDirector.stemDirector;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemNotator.stemNotator;

import java.util.Map;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.TextMeasurer;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Computes a {@link ChordNotation} from a {@link Chord}.
 * 
 * @author Andreas Wenger
 */
public class ChordNotator
	implements ElementNotator {
	
	public static final ChordNotator chordNotator = new ChordNotator();
	
	
	@Override public ChordNotation compute(MPElement element, Context context, Notations notations) {
		return compute((Chord) element, context, notations);
	}
	
	public ChordNotation compute(Chord chord, Context context, @MaybeNull Notations notations) {
		Score score = context.score;
		float interlineSpace = score.getInterlineSpace(context.mp);
		FontInfo lyricsFont = score.getFormat().getLyricFont();
		MusicContext mc = score.getMusicContext(context.mp, BeforeOrAt, Before);
		
		//grace or normal chord?
		boolean grace = chord.isGrace();
		ChordWidths chordWidths = (grace ? context.settings.graceChordWidths : context.settings.chordWidths);
		ChordSpacings spacings = (grace ? context.settings.spacings.graceChordSpacings
			: context.settings.spacings.normalChordSpacings);

		//use or compute stem direction
		StemDirection stemDirection = chord.getStem().getDirection();
		if (stemDirection == StemDirection.Default) {
			//if stem direction was not computed yet, compute it now
			if (notations != null)
				stemDirection = notations.getChord(chord).stemDirection;
			if (stemDirection == StemDirection.Default) {
				Map<Chord, StemDirection> computedStems = stemDirector.compute(chord);
				stemDirection = computedStems.get(chord);
				//also remember the other computed stems
				if (notations != null)
					for (Chord computedChord : computedStems.keySet())
						notations.getChord(computedChord).stemDirection = computedStems.get(computedChord);
			}
		}

		//notes displacement
		NotesNotation notes = notesNotator.compute(chord, stemDirection, chordWidths, mc);
		float leftSuspendedWidth = (notes.leftSuspended ? notes.noteheadWidthIs : 0);
		
		//accidentals
		AccidentalsNotation accs = accidentalsNotator.compute(chord, notes, chordWidths, mc);

		//symbol's width: width of the noteheads and dots
		float symbolWidth = notes.widthIs - leftSuspendedWidth;
		float frontGap = accs.widthIs + leftSuspendedWidth;

		//rear gap: empty duration-dependent space behind the chord minus the symbol's width
		float rearGap = spacings.getWidth(chord.getDisplayedDuration()) - symbolWidth;

		//lyric width
		float lyricWidth = 0;
		TextMeasurer textMeasurer = platformUtils().getTextMeasurer();
		for (Lyric lyric : chord.getLyrics()) {
			if (lyric != null && lyric.getText() != null) {
				//width of lyric in interline spaces
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
		float scaling = grace ? context.settings.scalingGrace : 1;
		StemNotation stem = stemNotator.compute(chord.getStem(), notes.getLps(),
			stemDirection, staffLines(mc.getLinesCount()), scaling);

		//compute articulations
		ArticulationsNotation arts = articulationsNotator.compute(chord, stemDirection, notes, mc.getLinesCount());

		return new ChordNotation(chord, new ElementWidth(frontGap, symbolWidth, rearGap, lyricWidth),
			notes, stemDirection, stem, accs, arts);
	}

}
