package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.stamper.ElementStamper.elementStamper;

import java.util.List;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.cache.OpenBeamsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenLyricsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenSlursCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.LastLyrics;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.RestSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates the {@link Stamping}s for {@link VoiceElement}s.
 * 
 * @author Andreas Wenger
 */
public class VoiceStamper {
	
	public static final VoiceStamper voiceStamper = new VoiceStamper();
	
	
	public List<Stamping> stampVoices(MeasureSpacing measure, float voicesXMm,
		StamperContext context,
	//TODO:
			FormattedTextStyle defaultLyricStyle, OpenBeamsCache openBeamsCache,
			OpenSlursCache openCurvedLinesCache,
			OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		context.layouter.saveMp();
		//iterate over the voices
		for (int iVoice : range(measure.getVoiceSpacings())) {
			context.layouter.mp = context.layouter.mp.withVoice(iVoice);
			VoiceSpacing voice = measure.getVoiceSpacings().get(iVoice);
			//don't stamp leading rests in non-first voices
			boolean stampLeadingRests = (iVoice == 0);
			//create voice stampings
			ret.addAll(stampVoice(voice, voicesXMm, stampLeadingRests, context, defaultLyricStyle,
				openBeamsCache, openCurvedLinesCache, openLyricsCache, lastLyrics, openTupletsCache));
		}
		context.layouter.restoreMp();
		return ret;
	}
	
	
	public List<Stamping> stampVoice(VoiceSpacing voice, float voiceXMm,
		boolean stampLeadingRests, StamperContext context,
		//TODO:
		FormattedTextStyle defaultLyricStyle, OpenBeamsCache openBeamsCache,
		OpenSlursCache openCurvedLinesCache,
		OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		
		//create the voice elements
		boolean onlyRestsSoFar = true;
		for (ElementSpacing spacingElement : voice.elements) {
			MusicElement element = spacingElement.getElement();
			if (element != null /* TODO && (stampRests || !(element instanceof Rest)) */) {
				Notation notation = context.getNotation(element);
				float xMm = voiceXMm + spacingElement.offsetIs * voice.interlineSpace;
				if (element instanceof Chord) {
					//chord
					onlyRestsSoFar = false;
					ret.addAll(ChordStamper.chordStamper.stampWithAttachments(
						(ChordNotation) spacingElement.notation, xMm, context, defaultLyricStyle, openBeamsCache,
						openCurvedLinesCache, openLyricsCache, lastLyrics, openTupletsCache));
				}
				else if (spacingElement instanceof RestSpacing) {
					//rest
					if (false == onlyRestsSoFar || stampLeadingRests) {
						//not a leading rest, or a leading rest which should be stamped
						ret.add(elementStamper.createRestStamping(
							(RestSpacing) spacingElement, xMm, context));
					}
				}
				else {
					throw new IllegalArgumentException("Notation not supported: " + notation);
				}
			}
		}
		return ret;
	}

}
