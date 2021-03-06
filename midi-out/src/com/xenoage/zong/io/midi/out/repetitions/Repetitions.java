package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Countable;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.Time;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.util.Iterator;
import java.util.List;

import static com.xenoage.utils.collections.CList.clist;

/**
 * This class stores the repetitions in a score by
 * remembering the sequential repetitions which have end be played,
 * defined by start and end {@link MP}s.
 *
 * Consecutive {@link Repetition}s are merged, e.g.
 * "measure 1 to measure 3" and "measure 3 to measure 4"
 * are merged to "measure 1 to measure 4".
 * 
 * @author Andreas Wenger
 */
@Const @EqualsAndHashCode @ToString
public final class Repetitions
	implements Iterable<Repetition>, Countable {

	/** The list of repetitions in chronological order. */
	private final IList<Repetition> repetitions;


	public Repetitions(List<Repetition> repetitions) {
		this.repetitions = mergeRepetitions(repetitions);
	}

	public Repetition get(int index) {
		return repetitions.get(index);
	}

	@Override public int getCount() {
		return repetitions.size();
	}

	static IList<Repetition> mergeRepetitions(List<Repetition> repetitions) {
		CList<Repetition> merged = clist();
		//merge consecutive repetitions
		Time start = null;
		Time end = null;
		for (val repetition : repetitions) {
			if (start == null) {
				//first repetition
				start = repetition.start;
				end = repetition.end;
			}
			else if (repetition.start.equals(end)) {
				//this repetition begins at the ending of the last one; continue it
				end = repetition.end;
			}
			else {
				//new repetition found. remember old one and start new one
				merged.add(new Repetition(start, end));
				start = repetition.start;
				end = repetition.end;
			}
		}
		if (start != null) {
			//close last repetition
			merged.add(new Repetition(start, end));
		}
		return merged.close();
	}

	@Override public Iterator<Repetition> iterator() {
		return repetitions.iterator();
	}
}
