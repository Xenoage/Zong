package com.xenoage.zong.core.util;

import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.unknown;
import static com.xenoage.zong.core.position.MP.unknownBeat;
import static com.xenoage.zong.core.position.MP.unknownMp;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.Getter;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.position.MP;

/**
 * An iterator over all {@link ColumnElement}s in a score.
 * 
 * The elements within a column may be unsorted by beat, but
 * first the elements with known beats are returned, then
 * the elements without beats.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementIterator
	implements Iterator<ColumnElement>, Iterable<ColumnElement> {
	
	private final Score score;
	
	@Getter private MP mp = unknownMp;
	
	private BeatEList<ColumnElement> elementsWithBeats;
	private List<ColumnElement> elementsWithoutBeats;
	private int nextList = 0;
	private int nextElement = 0;
	private int nextMeasure = 0;

	
	public ColumnElementIterator(Score score) {
		this.score = score;
		getNextLists();
		findNext();
	}

	@Override public boolean hasNext() {
		return nextMeasure != -1;
	}

	@Override public ColumnElement next() {
		if (!hasNext())
			throw new NoSuchElementException();
		ColumnElement element;
		Fraction beat = unknownBeat;
		if (nextList == 0) {
			BeatE<ColumnElement> e = elementsWithBeats.getElements().get(nextElement);
			element = e.element;
			beat = e.beat;
		}
		else {
			element = elementsWithoutBeats.get(nextElement);
		}
		mp = atBeat(unknown, nextMeasure, unknown, beat);
		nextElement += 1;
		findNext();
		return element;
	}
	
	private void findNext() {
		while (true) {
			if (nextList == 0) {
				//current list is with beats
				if (nextElement < elementsWithBeats.size()) {
					//next element with beat exists
					break;
				}
				else {
					//go to next list
					nextList += 1;
					nextElement = 0;
				}
			}
			else if (nextList == 1 && nextElement < elementsWithoutBeats.size()) {
				//next element without beat exists
				break;
			}
			else if (nextMeasure + 1 < score.getMeasuresCount()) {
				//next measure column
				nextMeasure += 1;
				nextList = 0;
				nextElement = 0;
				getNextLists();
			}
			else {
				//finished
				nextMeasure = -1;
				break;
			}
		}
	}
	
	private void getNextLists() {
		elementsWithBeats = score.getColumnHeader(nextMeasure).getColumnElementsWithBeats();
		elementsWithoutBeats = score.getColumnHeader(nextMeasure).getColumnElementsWithoutBeats();
	}

	@Override public Iterator<ColumnElement> iterator() {
		return this;
	}
	
	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

}
