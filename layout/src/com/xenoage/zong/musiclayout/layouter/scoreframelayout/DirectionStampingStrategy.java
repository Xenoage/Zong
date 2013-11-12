package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.core.music.format.Position.asPosition;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.text.DefaultTexts.getTempoTextNotNull;
import static com.xenoage.zong.text.FormattedText.fText;
import static com.xenoage.zong.text.FormattedTextUtils.styleText;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Attachable;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Crescendo;
import com.xenoage.zong.core.music.direction.Diminuendo;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.position.BMP;
import com.xenoage.zong.core.text.Alignment;
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
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.text.FormattedTextElement;
import com.xenoage.zong.text.FormattedTextParagraph;
import com.xenoage.zong.text.FormattedTextStyle;
import com.xenoage.zong.text.FormattedTextSymbol;


/**
 * This strategy computes the text stampings of {@link Direction}s.
 * 
 * @author Andreas Wenger
 */
public class DirectionStampingStrategy
	implements ScoreLayouterStrategy
{
	
	
	private static final float FONT_SIZE_IN_IS = 3.5f * 2.67f; //TODO
	
	
	/**
	 * Creates the {@link StaffTextStamping}s for the {@link Direction}s of
	 * the given {@link Chord} and its {@link ChordStampings}.
	 */
	public PVector<StaffTextStamping> createForChord(Chord chord, ChordStampings chordStampings,
		Score score, SymbolPool symbolPool)
	{
		PVector<StaffTextStamping> ret = pvec();
		for (Attachable attachable : score.globals.getAttachments().get(chord))
		{
			if (attachable instanceof Dynamics)
			{
				ret = ret.plus(createDynamics((Dynamics) attachable, score.getBMP(chord),
					chord, chordStampings, symbolPool));
			}
		}
		return ret;
	}
	
	
	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamics}s
	 * below the given {@link Chord} and its {@link ChordStampings}.
	 */
	public StaffTextStamping createDynamics(Dynamics dynamics, BMP bmp,
		Chord chord, ChordStampings chordStampings, SymbolPool symbolPool)
	{
		StaffStamping staff = chordStampings.staffStamping;
		
		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staff.linesCount - 1) * 2 + 2 * 2;
		if (chordStampings.noteheads.size() > 0)
		{
			defaultLPBelow = Math.min(defaultLPBelow,
				chordStampings.noteheads.getFirst().position.lp - 2 * 2);
			defaultLPAbove = Math.max(defaultLPAbove,
				chordStampings.noteheads.getLast().position.lp + 1 * 2);
		}
		SP sp = computePosition(dynamics, bmp, staff,
			defaultLPBelow, defaultLPAbove, defaultLPBelow);
		
		//create text
		PVector<FormattedTextElement> elements = pvec();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getType()))
		{
			Symbol symbol = symbolPool.getSymbol(s);
			elements = elements.plus(new FormattedTextSymbol(symbol,
				staff.is * FONT_SIZE_IN_IS, FormattedTextStyle.defaultColor));
		}
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(staff, chord, text, sp);
	}
	
	
	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Dynamics}s
	 * at the given {@link BMP} within the given {@link StaffStamping}.
	 */
	public StaffTextStamping createDynamics(Dynamics dynamics,
		BMP bmp, StaffStamping staffStamping, SymbolPool symbolPool)
	{
		//positioning
		//below (default): 3 IS below the base line, or 2 IS below the lowest note
		//above: 2 IS above the top line, or 1 IS above the highest note
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 2 * 2;
		SP sp = computePosition(dynamics, bmp, staffStamping,
			defaultLPBelow, defaultLPAbove, defaultLPBelow);
		
		//create text
		PVector<FormattedTextElement> elements = pvec();
		for (CommonSymbol s : CommonSymbol.getDynamics(dynamics.getType()))
		{
			Symbol symbol = symbolPool.getSymbol(s);
			elements = elements.plus(new FormattedTextSymbol(symbol,
				staffStamping.is * FONT_SIZE_IN_IS, FormattedTextStyle.defaultColor));
		}
		FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Center);
		FormattedText text = fText(paragraph);
		//create stamping
		return new StaffTextStamping(staffStamping, null, text, sp);
	}
	
	
	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Tempo}
	 * at the given {@link BMP} within the given {@link StaffStamping}.
	 */			
	public StaffTextStamping createTempo(Tempo tempo, BMP bmp,
		StaffStamping staffStamping, SymbolPool symbolPool)
	{
		//positioning
		//below: 3 IS below the base line
		//above (default): 2 IS above the top line
		float defaultLPBelow = -3f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 2 * 2;
		SP p = computePosition(tempo, bmp, staffStamping,
			defaultLPAbove, defaultLPAbove, defaultLPBelow);
		
		//create text
		FormattedText text = getTempoTextNotNull(tempo, symbolPool);
		
		//create stamping
		return new StaffTextStamping(staffStamping, tempo, text, p);
	}
	
	
	/**
	 * Creates a {@link StaffTextStamping} for the given {@link Words}
	 * at the given {@link BMP} within the given {@link StaffStamping}.
	 */
	public StaffTextStamping createWords(Words words, BMP bmp,
		StaffStamping staffStamping)
	{
		if (words.getText().getLength() == 0)
			return null;
		
		//positioning
		//below: 5 IS below the base line
		//above (default): 4 IS above the top line
		float defaultLPBelow = -5f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 4 * 2;
		SP p = computePosition(words, bmp, staffStamping,
			defaultLPAbove, defaultLPAbove, defaultLPBelow);
		
		//create text
		FormattedTextStyle style = new FormattedTextStyle(words.getFontInfo());
		FormattedText text = styleText(words.getText(), style);
		
		//create stamping
		return new StaffTextStamping(staffStamping, null, text, p);
	}
	
	
	/**
	 * Creates a {@link StaffSymbolStamping} for the given {@link Pedal}
	 * at the given {@link BMP} within the given {@link StaffStamping}.
	 */
	public StaffSymbolStamping createPedal(Pedal pedal, BMP bmp,
		StaffStamping staffStamping, SymbolPool symbolPool)
	{
		//positioning
		//below (default): 4 IS below the base line
		//above: 3 IS above the top line
		float defaultLPBelow = -4f * 2;
		float defaultLPAbove = (staffStamping.linesCount - 1) * 2 + 3 * 2;
		SP sp = computePosition(pedal, bmp, staffStamping,
			defaultLPBelow, defaultLPAbove, defaultLPBelow);
		
		//create stamping
		return new PedalStamping(pedal, staffStamping, sp, 1, symbolPool);
	}
	
	
	/**
	 * Creates a {@link WedgeStamping} for the given {@link Wedge} on the given staff.
	 * The start and end measure of the wedge may be outside the staff, then the
	 * wedge is clipped to the staff.
	 */
	public WedgeStamping createWedgeStamping(Wedge wedge, StaffStamping staffStamping,
		Score score)
	{
		//musical positions of wedge
		MusicElement anchorStart = score.globals.getAttachments().getAnchor(wedge);
		BMP p1 = score.getBMP(anchorStart);
		MusicElement anchorStop = score.globals.getAttachments().getAnchor(wedge.getWedgeEnd());
		BMP p2 = score.getBMP(anchorStop);
		//clip start to staff
		float x1Mm;
		if (p1.measure < staffStamping.getStartMeasureIndex())
		{
			//begins before staff
			x1Mm = staffStamping.getMeasureLeadingMm(staffStamping.getStartMeasureIndex());
		}
		else
		{
			//begins within staff
			x1Mm = staffStamping.getXMmAt(p1);
		}
		//clip end to staff
		float x2Mm;
		if (p2.measure > staffStamping.getEndMeasureIndex())
		{
			//ends after staff
			x2Mm = staffStamping.getMeasureEndMm(staffStamping.getEndMeasureIndex());
		}
		else
		{
			//ends within staff
			x2Mm = staffStamping.getXMmAt(p2);
		}
		//spread
		float d1Is = 0;
		float d2Is = 0;
		float defaultSpreadIS = 1.5f;
		if (wedge instanceof Crescendo)
		{
			d2Is = (wedge.getSpread() != null ? wedge.getSpread() : defaultSpreadIS);
		}
		else if (wedge instanceof Diminuendo)
		{
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
	 * at the given {@link BMP} within the given {@link StaffStamping}.
	 */
	private SP computePosition(Direction direction, BMP bmp,
		StaffStamping staffStamping, float defaultLP,
		float defaultLPAbove, float defaultLPBelow)
	{
		Positioning customPos = direction.getPositioning();
		float x, lp;
		
		//default positioning
		x = notNull(staffStamping.getXMmAt(bmp), 0f) + staffStamping.position.x;
		lp = defaultLP;
		
		//custom positioning
		if (customPos instanceof Placement)
		{
			Placement placement = (Placement) customPos;
			if (placement == Placement.Above)
				lp = defaultLPAbove;
			else
				lp = defaultLPBelow;
		}
		else if (customPos instanceof Position)
		{
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
