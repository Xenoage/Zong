package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.zong.core.text.UnformattedText.ut;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.Parser;
import com.xenoage.zong.commands.core.music.lyric.LyricAdd;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.musicxml.types.MxlLyric;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent.MxlLyricContentType;

/**
 * Reads {@link Lyric}s from {@link MxlLyric} elements of {@link MxlNote}s.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class LyricReader {
	
	private final List<MxlNote> mxlNotes;
	
	
	public void readToChord(Chord chord) {
		List<Lyric> lyrics = readLyrics();
		for (Lyric lyric : lyrics)
			new LyricAdd(lyric, chord).execute();
	}
	
	private List<Lyric> readLyrics() {
		//not supported yet: in MusicXML also rests can have lyrics. see measure 36 in Echigo-Jishi
		List<Lyric> ret = alist();
		for (MxlNote mxlNote : mxlNotes) {
			for (MxlLyric mxlLyric : it(mxlNote.getLyrics())) {
				int verse = readVerse(mxlLyric);
				MxlLyricContentType mxlLCType = mxlLyric.getContent().getLyricContentType();
				if (mxlLCType == MxlLyricContentType.SyllabicText) {
					MxlSyllabicText mxlSyllabicText = (MxlSyllabicText) mxlLyric.getContent();
					SyllableType syllableType = readSyllableType(mxlLyric);
					//the next element must be the text element
					ret.add(new Lyric(ut(mxlSyllabicText.getText().getValue()), syllableType, verse));
				}
				else if (mxlLCType == MxlLyricContentType.Extend) {
					//extend - TODO: extension to next chord!
					ret.add(Lyric.lyricExtend(verse));
				}
			}
		}
		return ret;
	}
	
	private int readVerse(MxlLyric mxlLyric) {
		//not supported yet: number which are not integer and
		//name instead of or additional to the number attribute (see MusicXML Test 61g)
		String number = mxlLyric.getNumber();
		if (number != null) {
			Integer i = Parser.parseIntegerNull(number);
			if (i != null)
				return i - 1;
		}
		return 0;
	}
	
	private SyllableType readSyllableType(MxlLyric mxlLyric) {
		MxlSyllabicText mxlSyllabicText = (MxlSyllabicText) mxlLyric.getContent();
		//a syllable
		switch (mxlSyllabicText.getSyllabic()) {
			case Single:
				return SyllableType.Single;
			case Begin:
				return SyllableType.Begin;
			case Middle:
				return SyllableType.Middle;
			case End:
				return SyllableType.End;
		}
		return null;
	}

}
