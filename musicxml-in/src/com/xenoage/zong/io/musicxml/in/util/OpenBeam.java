package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.zong.core.music.chord.Chord;

import lombok.Getter;

/**
 * An unclosed beam, needed during MusicXML import.
 * The connected chords are collected here.
 * 
 * See {@link OpenBeams} for more details.
 * 
 * @author Andreas Wenger
 */
public class OpenBeam {
	
	@Getter private List<Chord> chords = alist();
	
	public void addChord(Chord chord) {
		chords.add(chord);
	}

}
