package com.xenoage.zong.core.util;

import static com.xenoage.zong.core.position.MP.mp0;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

import lombok.Getter;

/**
 * An iterator over all {@link Beam}s in a score.
 * 
 * @author Andreas Wenger
 */
public class BeamIterator
	implements Iterator<Beam>, Iterable<Beam> {
	
	private VoiceElementIterator it;
	@Getter private MP mp = mp0;
	private MP nextMp = null;
	private Beam nextBeam = null;
	
	
	public BeamIterator(Score score) {
		it = new VoiceElementIterator(score);
		findNext();
	}

	@Override public boolean hasNext() {
		return nextBeam != null;
	}

	@Override public Beam next() {
		if (!hasNext())
			throw new NoSuchElementException();
		Beam ret = nextBeam;
		mp = nextMp;
		findNext();
		return ret;
	}
	
	private void findNext() {
		while (it.hasNext()) {
			VoiceElement e = it.next();
			if (e instanceof Chord) {
				Chord c = (Chord) e;
				Beam beam = c.getBeam();
				//return beam when its end was found
				if (beam != null && beam.getStop().getChord() == c) {
					nextMp = it.getMp();
					nextBeam = beam;
					return;
				}
			}
		}
		nextMp = null;
		nextBeam = null;
	}

	@Override public Iterator<Beam> iterator() {
		return this;
	}

}
