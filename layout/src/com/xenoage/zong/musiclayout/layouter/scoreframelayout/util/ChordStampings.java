package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import static com.xenoage.utils.collections.CollectionUtils.addAll;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.Chord;
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
 * Layout information on a chord: a list of noteheads,
 * leger lines, dots, accidentals and a stem and flag.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ChordStampings {

	public Chord chord;
	public float xMm;
	public StaffStamping staff;
	public NoteheadStamping[] noteheads;
	public ProlongationDotStamping[] dots;
	public AccidentalStamping[] accidentals;
	public LegerLineStamping[] legerLines;
	public ArticulationStamping[] articulations;
	public FlagsStamping flags;
	public StemStamping stem;

	
	public NoteheadStamping getFirstNotehead() {
		return noteheads[0];
	}
	
	public NoteheadStamping getLastNotehead() {
		return noteheads[noteheads.length - 1];
	}

	public void addAllTo(List<Stamping> list) {
		addAll(list, noteheads);
		addAll(list, dots);
		addAll(list, accidentals);
		addAll(list, legerLines);
		addAll(list, articulations);
		if (stem != null)
			list.add(stem);
		if (flags != null)
			list.add(flags);
	}

}
