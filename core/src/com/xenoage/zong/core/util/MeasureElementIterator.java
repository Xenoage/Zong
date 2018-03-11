package com.xenoage.zong.core.util;

import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.core.position.MP.unknown;
import static com.xenoage.zong.core.position.MP.unknownMp;

import java.util.Iterator;
import java.util.NoSuchElementException;

import lombok.Getter;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.position.MP;

/**
 * An iterator over all {@link MeasureElement}s in a score.
 * 
 * Iterates over the staves, measures and its measure elements.
 * The elements within a measure may be unsorted by beat.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementIterator
	implements Iterator<MeasureElement>, Iterable<MeasureElement> {
	
	private final Score score;
	
	@Getter private MP mp = Companion.getUnknownMp();
	private BeatEList<MeasureElement> elements;
	private int nextElementIndex = 0;
	private MP nextMp = Companion.getMp0();

	
	public MeasureElementIterator(Score score) {
		this.score = score;
		elements = score.getMeasure(nextMp).getMeasureElements();
		findNext();
	}

	@Override public boolean hasNext() {
		return nextMp != null;
	}

	@Override public MeasureElement next() {
		if (!hasNext())
			throw new NoSuchElementException();
		mp = nextMp;
		MeasureElement element = elements.getElements().get(nextElementIndex).element;
		nextElementIndex++;
		findNext();
		return element;
	}
	
	private void findNext() {
		while (true) {
			if (nextElementIndex < elements.size()) {
				//next element within measure exists
				nextMp = Companion.atBeat(nextMp.getStaff(), nextMp.getMeasure(), Companion.getUnknown(),
					elements.getElements().get(nextElementIndex).beat);
				break;
			}
			else {
				nextElementIndex = 0;
				if (nextMp.getMeasure() + 1 < score.getMeasuresCount()) {
					//next measure within staff
					nextMp = Companion.atMeasure(nextMp.getStaff(), nextMp.getMeasure() + 1);
				}
				else if (nextMp.getStaff() + 1 < score.getStavesCount()) {
					//next staff
					nextMp = Companion.atMeasure(nextMp.getStaff() + 1, 0);
				}
				else {
					//finished
					nextMp = null;
					break;
				}
				elements = score.getMeasure(nextMp).getMeasureElements();
			}
		}
	}

	@Override public Iterator<MeasureElement> iterator() {
		return this;
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
