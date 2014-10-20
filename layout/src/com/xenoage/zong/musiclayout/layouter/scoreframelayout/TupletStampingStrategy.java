package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextParagraph.fPara;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.TupletStamping;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * This strategy computes the {@link TupletStamping}s
 * for tuplets.
 * 
 * The rules are inspired by Chlapik, page 66 and 67,
 * but not all rules are implemented yet.
 * 
 * Currently, only single-measure tuplets are supported.
 * 
 * @author Andreas Wenger
 */
public class TupletStampingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the {@link TupletStamping} for the given {@link ChordStampings}
	 * and returns it. 
	 */
	public TupletStamping createTupletStamping(Tuplet tuplet, OpenTupletsCache cache,
		SymbolPool symbolPool) {
		StaffStamping ss = cache.getChord(tuplet.getChords().get(0), tuplet).staffStamping;

		//horizontal position of the bracket
		float xDistance = ss.is / 4;
		float x1Mm = cache.getChord(tuplet.getFirstChord(), tuplet).positionX - xDistance;
		ChordStampings cs2 = cache.getChord(tuplet.getLastChord(), tuplet);
		float cs2Width = ss.is * 1.2f; //TODO: notehead width!
		float x2Mm = cs2.positionX + cs2Width + xDistance;

		//vertical position of the bracket (above or below) depends on the directions
		//of the majority of the stems
		int stemDir = 0;
		for (Chord chord : tuplet.getChords()) {
			ChordStampings cs = cache.getChord(chord, tuplet);
			if (cs.stem != null) {
				stemDir += cs.stem.direction.getSign();
			}
		}
		int placement = (stemDir < 0 ? 1 : -1); //1: above, -1: below

		//compute position of start and end point
		//by default, the bracket is 1.5 IS away from the end of the stems
		//when there is no stem, the innermost notehead is used
		//TODO: if stems of inner chords are longer, correct!
		float distanceLp = 1.5f * 2;
		float y1Lp = computeBracketLP(cache.getChord(tuplet.getFirstChord(), tuplet), placement,
			distanceLp);
		float y2Lp = computeBracketLP(cache.getChord(tuplet.getLastChord(), tuplet), placement,
			distanceLp);

		//bracket always outside of staff lines
		//at least 2 IS over top barline / under bottom barline
		if (placement == 1) //above staff
		{
			y1Lp = Math.max(y1Lp, (ss.linesCount - 1) * 2 + 4);
			y2Lp = Math.max(y1Lp, (ss.linesCount - 1) * 2 + 4);
		}
		else //below staff
		{
			y1Lp = Math.min(y1Lp, 0 - 4);
			y2Lp = Math.min(y1Lp, 0 - 4);
		}

		//text
		float fontSize = 10 * ss.is / 1.6f;
		FormattedText text = createText(tuplet.getActualNotes(), fontSize, symbolPool);

		//return result
		return new TupletStamping(x1Mm, x2Mm, y1Lp, y2Lp, true, text, ss);
	}

	private float computeBracketLP(ChordStampings cs, int placement, float distanceLp) {
		if (cs.stem != null) {
			return cs.stem.endLp + placement * distanceLp;
		}
		else {
			if (placement == 1) //distance to top notehead
			{
				return cs.noteheads.getLast().position.lp + placement * distanceLp;
			}
			else //distance to bottom notehead
			{
				return cs.noteheads.getFirst().position.lp + placement * distanceLp;
			}
		}
	}

	private FormattedText createText(int digit, float fontSize, SymbolPool symbolPool) {
		if (digit < 0 || digit > 9)
			return null; //TODO
		CommonSymbol s = CommonSymbol.getDigit(digit);
		FormattedTextParagraph p = fPara(new FormattedTextSymbol(symbolPool.getSymbol(s), fontSize,
			FormattedTextStyle.defaultColor));
		return fText(p);
	}

}
