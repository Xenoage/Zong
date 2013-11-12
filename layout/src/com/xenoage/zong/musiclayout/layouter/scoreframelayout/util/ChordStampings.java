package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import java.util.LinkedList;

import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.musiclayout.layouter.cache.util.OpenBeamMiddleStem;
import com.xenoage.zong.musiclayout.stampings.AccidentalStamping;
import com.xenoage.zong.musiclayout.stampings.ArticulationStamping;
import com.xenoage.zong.musiclayout.stampings.FlagsStamping;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.ProlongationDotStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;


/**
 * The stampings belonging to a chord: a list of noteheads,
 * leger lines, dots, accidentals and a stem and flag.
 * 
 * @author Andreas Wenger
 */
public final class ChordStampings
{
	
	public final float positionX;
	public final StaffStamping staffStamping;
	
	public final Vector<NoteheadStamping> noteheads;
	public final Vector<LegerLineStamping> legerLines;
	public final Vector<ProlongationDotStamping> dots;
	public final Vector<AccidentalStamping> accidentals;
	public final Vector<ArticulationStamping> articulations;
	public final FlagsStamping flags;
	
	//stamped or open stem
	public final StemStamping stem;
	public final OpenBeamMiddleStem openStem;
	
	
	public ChordStampings(float positionX, StaffStamping staffStamping,
		Vector<NoteheadStamping> noteheads, Vector<LegerLineStamping> legerLines,
		Vector<ProlongationDotStamping> dots, Vector<AccidentalStamping> accidentals,
		Vector<ArticulationStamping> articulations, FlagsStamping flags,
		StemStamping stem, OpenBeamMiddleStem openStem)
	{
		this.positionX = positionX;
		this.staffStamping = staffStamping;
		this.noteheads = noteheads;
		this.legerLines = legerLines;
		this.dots = dots;
		this.accidentals = accidentals;
		this.articulations = articulations;
		this.flags = flags;
		this.stem = stem;
		this.openStem = openStem;
	}
	
	
	public void addAllTo(LinkedList<Stamping> list)
	{
		list.addAll(noteheads);
		list.addAll(legerLines);
		list.addAll(dots);
		list.addAll(accidentals);
		list.addAll(articulations);
    if (stem != null)
    	list.add(stem);
    if (flags != null)
    	list.add(flags);
	}
	
	
}
