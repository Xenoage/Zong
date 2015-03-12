package com.xenoage.zong.musiclayout.spacing;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notations.BeamNotation;

/**
 * Horizontal and vertical spacing of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamSpacing {
	
	/** The beam notation. */
	public BeamNotation notation;
	/** The end positions (far from the note) of the stems. */
	public List<SP> stemEndSps;
	
}
