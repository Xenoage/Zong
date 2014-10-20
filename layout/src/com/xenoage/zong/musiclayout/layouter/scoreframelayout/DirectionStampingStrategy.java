package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.zong.core.music.format.Position.asPosition;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.musiclayout.text.DefaultTexts.getTempoTextNotNull;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.stampings.PedalStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.musiclayout.stampings.WedgeStamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * This strategy computes the text stampings of {@link Direction}s.
 * 
 * @author Andreas Wenger
 */
public class DirectionStampingStrategy
	implements ScoreLayouterStrategy {

	private static final float FONT_SIZE_IN_IS = 3.5f * 2.67f; //TODO


	/**
	 * Creates the {@link StaffTextStamping}s for the {@link Direction}s of
	 * the given {@link Chord} and its {@link ChordStampings}.
	 */
	public IList<StaffTextStamping> createForChord(Chord chord, ChordStampings chordStampings,
		SymbolPool symbolPool) {
		CList<StaffTextStamping> ret = CList.clist();
		for (Direction direction : chord.getDirections()) {
			if (direction instanceof Dynamics) {
				ret.add(createDynamics((Dynamics) direction, MP.getMP(chord), chord, chordStampings, symbolPool));
			}
		}
		return ret.close();
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamics}s
	 * below the given {@link Chord} and its {@link ChordStampings}.
	 */
	public StaffTextStamping createDynamics(Dynamics dynamics, MP mp, Chord chord,
		ChordStampings chordStampings, SymbolPool symbolPool) {
		StaffStamping staff = chordStampings.staffStamping;

		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staff.linesCount - 1) * 2 + 2 * 2;
		if (chordStampings.noteheads.size() > 0) {
			defaultLPBelow = Math.min(defaultLPBelow,
				chordStampings.noteheads.getFirst().position.lp - 2 * 2);
			defaultLPAbove = Math.max(defaultLPAbove,
				chordStampings.noteheads.getLast().position.lp + 1 * 2);
		}
		SP sp = computePosition(dynamics, mp, staff, defaultLPBelow, defaultLPAbove, defaultLPBelow);

		//create text
		CList<FormattedTextElement> elements = clist();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getType())) {
			Symbol symbol = symbolPool.getSymbol(s);
			elements.add(new FormattedTextSymbol(symbol, staff.is * FONT_SIZE_IN_IS,
				FormattedTextStyle.defaultColor));
		}
		elements.close();
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(staff, chord, text, sp);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamics}s
	 * at the given {@link MP} within the given {@link StaffStamping}.
	 */
	public StaffTextStamping createDynamics(Dynamics dynamics, MP mp, StaffStamping staffStamping,
		SymbolPool symbolPool) {
		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 2 * 2;
		SP sp = computePosition(dynamics, mp, staffStamping, defaultLPBelow, defaultLPAbove,
			defaultLPBelow);

		//create text
		CList<FormattedTextElement> elements = clist();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getType())) {
			Symbol symbol = symbolPool.getSymbol(s);
			elements.add(new FormattedTextSymbol(symbol, staffStamping.is * FONT_SIZE_IN_IS,
				FormattedTextStyle.defaultColor));
		}
		elements.close();
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(staffStamping, null, text, sp);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Tempo}
	 * at the given {@link MP} within the given {@link StaffStamping}.
	 */
	public StaffTextStamping createTempo(Tempo tempo, MP mp, StaffStamping staffStamping,
		SymbolPool symbolPool) {
		//positioning
		//below: 3 IS below the base line
		//above (default): 2 IS above the top line
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 2 * 2;
		SP p = computePosition(tempo, mp, staffStamping, defaultLPAbove, defaultLPAbove,
			defaultLPBelow);

		//create text
		FormattedText text = getTempoTextNotNull(tempo, symbolPool);

		//create stamping
		return new StaffTextStamping(staffStamping, tempo, text, p);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Words}
	 * at the given {@link MP} within the given {@link StaffStamping}.
	 */
	public StaffTextStamping createWords(Words words, MP bmp, StaffStamping staffStamping) {
		if (words.getText().getLength() == 0)
			return null;

		//positioning
		//below: 5 IS below the base line
		//above (default): 4 IS above the top line
		float defaultLPBelow = -5f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 4 * 2;
		SP p = computePosition(words, bmp, staffStamping, defaultLPAbove, defaultLPAbove,
			defaultLPBelow);

		//create text
		FormattedTextStyle style =FormattedTextStyle.defaultStyle; //TODO: FormattedTextStyle(words.getFontInfo());
		FormattedText text = styleText(words.getText(), style);

		//create stamping
		return new StaffTextStamping(staffStamping, null, text, p);
	}

	/**
	 * Creates a {@link StaffSymbolStamping} for the given {@link Pedal}
	 * at the given {@link MP} within the given {@link StaffStamping}.
	 */
	public StaffSymbolStamping createPedal(Pedal pedal, MP mp, StaffStamping staffStamping,
		SymbolPool symbolPool) {
		//positioning
		//below (default): 4 IS below the base line
		//above: 3 IS above the top line
		float defaultLPBelow = -4f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 3 * 2;
		SP sp = computePosition(pedal, mp, staffStamping, defaultLPBelow, defaultLPAbove,
			defaultLPBelow);

		//create stamping
		return new PedalStamping(pedal, staffStamping, sp, 1, symbolPool);
	}

	/**
	 * Creates a {@link WedgeStamping} for the given {@link Wedge} on the given staff.
	 * The start and end measure of the wedge may be outside the staff, then the
	 * wedge is clipped to the staff.
	 */
	public WedgeStamping createWedgeStamping(Wedge wedge, StaffStamping staffStamping) {
		//musical positions of wedge
		MP p1 = MP.getMP(wedge);
		MP p2 = MP.getMP(wedge.getWedgeEnd());
		//clip start to staff
		float x1Mm;
		if (p1.measure < staffStamping.getStartMeasureIndex()) {
			//begins before staff
			x1Mm = staffStamping.getMeasureLeadingMm(staffStamping.getStartMeasureIndex());
		}
		else {
			//begins within staff
			x1Mm = staffStamping.getXMmAt(p1);
		}
		//clip end to staff
		float x2Mm;
		if (p2.measure > staffStamping.getEndMeasureIndex()) {
			//ends after staff
			x2Mm = staffStamping.getMeasureEndMm(staffStamping.getEndMeasureIndex());
		}
		else {
			//ends within staff
			x2Mm = staffStamping.getXMmAt(p2);
		}
		//spread
		float d1Is = 0;
		float d2Is = 0;
		float defaultSpreadIS = 1.5f;
		if (wedge.getType() == WedgeType.Crescendo) {
			d2Is = (wedge.getSpread() != null ? wedge.getSpread() : defaultSpreadIS);
		}
		else if (wedge.getType() == WedgeType.Diminuendo) {
			d1Is = (wedge.getSpread() != null ? wedge.getSpread() : defaultSpreadIS);
		}

		//custom horizontal position
		Position customPos = asPosition(wedge.getPositioning());
		float length = x2Mm - x1Mm;
		if (customPos != null && customPos.x != null)
			x1Mm = customPos.x;
		x1Mm += Position.getRelativeX(customPos);
		x2Mm = x1Mm + length;

		//vertical position
		float lp;
		if (customPos != null && customPos.y != null)
			lp = customPos.y;
		else
			lp = -6;
		lp += Position.getRelativeY(customPos);

		return new WedgeStamping(wedge, lp, x1Mm, x2Mm, d1Is, d2Is, staffStamping);
	}

	/**
	 * Computes the position for the given {@link Direction}
	 * at the given {@link MP} within the given {@link StaffStamping}.
	 */
	private SP computePosition(Direction direction, MP mp, StaffStamping staffStamping,
		float defaultLP, float defaultLPAbove, float defaultLPBelow) {
		Positioning customPos = direction.getPositioning();
		float x, lp;

		//default positioning
		x = notNull(staffStamping.getXMmAt(mp), 0f) + staffStamping.position.x;
		lp = defaultLP;

		//custom positioning
		if (customPos instanceof Placement) {
			Placement placement = (Placement) customPos;
			if (placement == Placement.Above)
				lp = defaultLPAbove;
			else
				lp = defaultLPBelow;
		}
		else if (customPos instanceof Position) {
			//coordinates
			Position pos = (Position) customPos;
			if (pos.x != null)
				x = pos.x;
			x += Position.getRelativeX(pos);

			//vertical position
			if (pos.y != null)
				lp = pos.y;
			lp += Position.getRelativeY(pos);
		}

		return sp(x, lp);
	}

}
