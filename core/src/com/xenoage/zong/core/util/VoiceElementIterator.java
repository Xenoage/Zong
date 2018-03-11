package com.xenoage.zong.core.util;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.mp;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.core.position.MP.unknownMp;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.Getter;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.position.MP;

/**
 * An iterator over all {@link VoiceElement}s in a score.
 * 
 * Iterates over the staves, measures, voices and its voice elements.
 * 
 * @author Andreas Wenger
 */
public class VoiceElementIterator
	implements Iterator<VoiceElement>, Iterable<VoiceElement> {
	
	private final Score score;
	
	@Getter private MP mp = Companion.getUnknownMp();
	
	private List<VoiceElement> elements;
	private MP nextMp = Companion.getMp0();

	
	public VoiceElementIterator(Score score) {
		this.score = score;
		elements = score.getVoice(nextMp).getElements();
		findNext();
	}

	@Override public boolean hasNext() {
		return nextMp != null;
	}

	@Override public VoiceElement next() {
		if (!hasNext())
			throw new NoSuchElementException();
		mp = nextMp;
		VoiceElement element = elements.get(nextMp.getElement());
		nextMp = nextMp.withElement(nextMp.getElement() + 1);
		nextMp = nextMp.withBeat(nextMp.getBeat().add(element.getDuration()));
		findNext();
		return element;
	}
	
	private void findNext() {
		while (true) {
			if (nextMp.getElement() < elements.size()) {
				//next element within voice exists
				break;
			}
			else {
				if (nextMp.getVoice() + 1 < score.getMeasure(nextMp).getVoices().size()) {
					//next measure within staff
					nextMp = Companion.mp(nextMp.getStaff(), nextMp.getMeasure(), nextMp.getVoice() + 1, Companion.get_0(), 0);
				}
				else if (nextMp.getMeasure() + 1 < score.getMeasuresCount()) {
					//next measure within staff
					nextMp = Companion.mp(nextMp.getStaff(), nextMp.getMeasure() + 1, 0, Companion.get_0(), 0);
				}
				else if (nextMp.getStaff() + 1 < score.getStavesCount()) {
					//next staff
					nextMp = Companion.mp(nextMp.getStaff() + 1, 0, 0, Companion.get_0(), 0);
				}
				else {
					//finished
					nextMp = null;
					break;
				}
				elements = score.getVoice(nextMp).getElements();
			}
		}
	}

	@Override public Iterator<VoiceElement> iterator() {
		return this;
	}
	
	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

}
