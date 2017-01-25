package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.*;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.*;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.musiclayout.text.DefaultTexts.getTempoTextNotNull;
import static java.util.Collections.emptyList;

/**
 * Computes the {@link Stamping}s of {@link Direction}s.
 * 
 * @author Andreas Wenger
 */
public class DirectionStamper {
	
	public static final DirectionStamper directionStamper = new DirectionStamper();
	

	private static final float FONT_SIZE_IN_IS = 3.5f * 2.67f; //TODO
	
	
	/**
	 * Creates all direction stampings for the given measure.
	 */
	public List<Stamping> stamp(StamperContext context) {
		
		List<Stamping> ret = alist();
		val directionsWithBeats = context.layouter.score.getMeasure(
			context.getMp()).getDirections().clone();

		//over first staff, also add tempo directions for the whole column
		if (context.getMp().staff == 0) {
			directionsWithBeats.addAll(context.layouter.score.getColumnHeader(
				context.getMp().measure).getTempos());
		}
		for (BeatE<Direction> elementWithBeat : directionsWithBeats) {
			Direction element = elementWithBeat.element;
			Stamping stamping = null;
			if (MusicElementType.Tempo.is(element))
				stamping = directionStamper.createTempo((Tempo) element, context);
			else if (MusicElementType.Dynamic.is(element))
				stamping = directionStamper.createDynamics((Dynamic) element, context);
			else if (MusicElementType.Pedal.is(element))
				stamping = directionStamper.createPedal((Pedal) element, context);
			else if (MusicElementType.Words.is(element))
				stamping = directionStamper.createWords((Words) element, context);
			if (stamping != null)
				ret.add(stamping);
		}
		
		return ret;
	}


	/**
	 * Creates the {@link StaffTextStamping}s for the {@link Direction}s of
	 * the given {@link ChordStampings}.
	 */
	public List<StaffTextStamping> stampForChord(ChordStampings chordStampings,
		SymbolPool symbolPool) {
		Chord chord = chordStampings.chord;
		int directionsCount = chord.getDirections().size();
		if (directionsCount == 0)
			return emptyList();
		List<StaffTextStamping> ret = alist();
		for (Direction direction : chord.getDirections()) {
			if (direction instanceof Dynamic) {
				ret.add(createDynamics((Dynamic) direction, chord, chordStampings, symbolPool));
			}
			//TODO: support more directions
		}
		return ret;
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamic}s
	 * below the given {@link Chord} and its {@link ChordStampings}.
	 */
	public StaffTextStamping createDynamics(Dynamic dynamics, Chord chord,
																					ChordStampings chordStampings, SymbolPool symbolPool) {
		StaffStamping staff = chordStampings.staff;

		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staff.linesCount - 1) * 2 + 2 * 2;
		if (chordStampings.noteheads.length > 0) {
			defaultLPBelow = Math.min(defaultLPBelow,
				chordStampings.getFirstNotehead().position.lp - 2 * 2);
			defaultLPAbove = Math.max(defaultLPAbove,
				chordStampings.getLastNotehead().position.lp + 1 * 2);
		}
		SP sp = computePosition(dynamics, staff, defaultLPBelow, defaultLPAbove, defaultLPBelow);

		//create text
		CList<FormattedTextElement> elements = clist();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getValue())) {
			Symbol symbol = symbolPool.getSymbol(s);
			elements.add(new FormattedTextSymbol(symbol, staff.is * FONT_SIZE_IN_IS,
				FormattedTextStyle.defaultColor));
		}
		elements.close();
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(text, sp, staff, dynamics);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamic}s.
	 */
	public StaffTextStamping createDynamics(Dynamic dynamics, StamperContext context) {
		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (context.staff.linesCount - 1) * 2 + 2 * 2;
		SP sp = computePosition(dynamics, context.staff, defaultLPBelow, defaultLPAbove,
			defaultLPBelow);

		//create text
		CList<FormattedTextElement> elements = clist();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getValue())) {
			Symbol symbol = context.getSymbol(s);
			elements.add(new FormattedTextSymbol(symbol, context.staff.is * FONT_SIZE_IN_IS,
				FormattedTextStyle.defaultColor));
		}
		elements.close();
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(text, sp, context.staff, dynamics);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Tempo}.
	 */
	public StaffTextStamping createTempo(Tempo tempo, StamperContext context) {
		//positioning
		//below: 3 IS below the base line
		//above (default): 2 IS above the top line
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (context.staff.linesCount - 1) * 2 + 2 * 2;
		SP p = computePosition(tempo, context.staff, defaultLPAbove, defaultLPAbove,
			defaultLPBelow);

		//create text
		FormattedText text = getTempoTextNotNull(tempo, context.layouter.symbols);

		//create stamping
		return new StaffTextStamping(text, p, context.staff, tempo);
	}

	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Words}.
	 */
	public StaffTextStamping createWords(Words words, StamperContext context) {
		if (words.getText().getLength() == 0)
			return null;

		//positioning
		//below: 5 IS below the base line
		//above (default): 4 IS above the top line
		float defaultLPBelow = -5f * 2;
		float defaultLPAbove = (context.staff.linesCount - 1) * 2 + 4 * 2;
		SP p = computePosition(words, context.staff, defaultLPAbove, defaultLPAbove,
			defaultLPBelow);

		//create text
		FormattedTextStyle style = FormattedTextStyle.defaultStyle; //TODO: FormattedTextStyle(words.getFontInfo());
		FormattedText text = styleText(words.getText(), style);

		//create stamping
		return new StaffTextStamping(text, p, context.staff, words);
	}

	/**
	 * Creates a {@link StaffSymbolStamping} for the given {@link Pedal}.
	 */
	public StaffSymbolStamping createPedal(Pedal pedal, StamperContext context) {
		//positioning
		//below (default): 4 IS below the base line
		//above: 3 IS above the top line
		float defaultLPBelow = -4f * 2;
		float defaultLPAbove = (context.staff.linesCount - 1) * 2 + 3 * 2;
		SP sp = computePosition(pedal, context.staff, defaultLPBelow, defaultLPAbove,
			defaultLPBelow);
		//create stamping
		Symbol symbol = context.getSymbol(CommonSymbol.getPedal(pedal.getType()));
		return new StaffSymbolStamping(null, context.staff, symbol, null, sp, 1, false);
	}

	/**
	 * Computes the position for the given {@link Direction}
	 * within the given {@link StaffStamping}.
	 */
	private SP computePosition(Direction direction, StaffStamping staffStamping,
		float defaultLP, float defaultLPAbove, float defaultLPBelow) {
		
		Positioning customPos = direction.getPositioning();
		MP mp = direction.getMP();
		if (mp == null)
			return SP.sp(0, 0); //TODO. Happens. But just with offical MusicXML sample Telemann.xml
				
		float x, lp;

		//default positioning
		x = notNull(staffStamping.system.getXMmAt(mp.getTime()), 0f) + staffStamping.positionMm.x;
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
