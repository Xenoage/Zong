package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.stamper.ChordStamper.chordStamper;
import static com.xenoage.zong.musiclayout.stamper.ElementStamper.elementStamper;

import java.util.List;
import java.util.Map;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.cache.OpenLyricsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenSlursCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.LastLyrics;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
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
		StaffStampings staffStampings, StamperContext context,
	//TODO:
			FormattedTextStyle defaultLyricStyle, Map<Beam, BeamSpacing> beams,
			OpenSlursCache openCurvedLinesCache,
			OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		context.layouter.saveMp();
		//iterate over the voices
		for (int iVoice : range(measure.voices)) {
			context.layouter.mp = context.layouter.mp.withVoice(iVoice);
			VoiceSpacing voice = measure.voices.get(iVoice);
			//don't stamp leading rests in non-first voices
			boolean stampLeadingRests = (iVoice == 0);
			//create voice stampings
			ret.addAll(stampVoice(voice, voicesXMm, staffStampings, stampLeadingRests, context, defaultLyricStyle,
				beams, openCurvedLinesCache, openLyricsCache, lastLyrics, openTupletsCache));
		}
		context.layouter.restoreMp();
		return ret;
	}
	
	
	public List<Stamping> stampVoice(VoiceSpacing voice, float voiceXMm,
		StaffStampings staffStampings, boolean stampLeadingRests, StamperContext context,
		//TODO:
		FormattedTextStyle defaultLyricStyle, Map<Beam, BeamSpacing> beams,
		OpenSlursCache openCurvedLinesCache,
		OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		
		//create the voice elements
		boolean onlyRestsSoFar = true;
		for (ElementSpacing spacingElement : voice.elements) {
			MusicElement element = spacingElement.getElement();
			if (element != null /* TODO && (stampRests || !(element instanceof Rest)) */) {
				Notation notation = context.getNotation(element);
				float xMm = voiceXMm + spacingElement.xIs * voice.interlineSpace;
				if (element instanceof Chord) {
					//chord
					onlyRestsSoFar = false;
					Chord chord = (Chord) element;
					BeamSpacing beam = beams.get(chord.getBeam());
					ret.addAll(chordStamper.stampAll(
						(ChordNotation) spacingElement.notation, xMm, beam, staffStampings, context, defaultLyricStyle,
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
