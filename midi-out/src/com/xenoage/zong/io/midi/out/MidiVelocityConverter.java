package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.kernel.Tuple2.t;

import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.position.MP;

/**
 * This class converts dynamics.
 * 
 * @author Uli Teschemacher
 */
public final class MidiVelocityConverter {

	private static final int defaultvalue = 64;


	private static boolean voiceHasDynamics(Staff staff, int voiceIndex) {
		for (Measure measure : staff.getMeasures()) {
			if (measure.getVoices().size() > voiceIndex) {
				List<VoiceElement> elements = measure.getVoice(voiceIndex).getElements();
				for (VoiceElement element : elements) {
					if (element instanceof Chord) { //TODO: may also be attached to rests?
						Chord chord = (Chord) element;
						for (Direction direction : chord.getDirections()) {
							if (direction instanceof Dynamics) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean[] voicesInStaffHaveDynamics(Staff staff) {
		int numberOfVoices = staff.getVoicesCount();
		boolean[] dyn = new boolean[numberOfVoices];
		for (int i = 0; i < numberOfVoices; i++) {
			dyn[i] = voiceHasDynamics(staff, i);
		}
		return dyn;
	}

	private static boolean staffHasDynamics(boolean[] dynamics) {
		for (boolean b : dynamics) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	private static int getFirstVoiceWithDynamics(boolean[] dynamics) {
		for (int i = 0; i < dynamics.length; i++) {
			if (dynamics[i]) {
				return i;
			}
		}
		return 0;
	}

	public static int[] getVoiceforDynamicsInStaff(Staff staff) {
		boolean[] voicesInStaffHaveDynamics = voicesInStaffHaveDynamics(staff);
		int[] dynamicVoices = new int[voicesInStaffHaveDynamics.length];
		// If there are no dynamics in any voice, every voice can use its own
		// "dynamics"
		if (!staffHasDynamics(voicesInStaffHaveDynamics)) {
			for (int i = 0; i < voicesInStaffHaveDynamics.length; i++) {
				dynamicVoices[i] = i;
			}
		}
		else {
			for (int i = 0; i < dynamicVoices.length; i++) {
				if (voicesInStaffHaveDynamics[i]) {
					dynamicVoices[i] = i;
				}
				else {
					dynamicVoices[i] = getFirstVoiceWithDynamics(voicesInStaffHaveDynamics);
				}
			}
		}
		return dynamicVoices;
	}

	/**
	 * 
	 * @param staff
	 * @param voice
	 * @param position
	 * @param currentVelocity
	 * @return The first value is the current velocity, the second one is the
	 * velocity of the subsequent chords.
	 */
	public static int[] getVelocityAtPosition(Staff staff, int voice, MP mp, int currentVelocity,
		Score score) {
		Tuple2<DynamicsType, Fraction> latestDynamicsType = getLatestDynamicsType(staff, voice, mp,
			score);
		if (latestDynamicsType != null) {
			int vel[] = new int[2];
			int velocityFactorAtBeat = -1; /* TODO: what is this?  Settings.getInstance().getSetting(
				latestDynamicsType.get1().name(), "playback-dynamics", -1); */
			if (velocityFactorAtBeat == -1) {
				vel[0] = currentVelocity;
			}
			else {
				vel[0] = convertToMidiVelocity(velocityFactorAtBeat);
			}
			int subsequentVelocityFactor = velocityFactorAtBeat; /* TODO: what is this?  Settings.getInstance()
				.getSetting(latestDynamicsType.get1().name() + "_subsequent", "playback-dynamics",
					velocityFactorAtBeat); */

			if (latestDynamicsType.get2().compareTo(mp.beat) != 0) {
				if (subsequentVelocityFactor != -1) {
					vel[0] = convertToMidiVelocity(subsequentVelocityFactor);
				}
				else {
					vel[0] = currentVelocity;
				}
			}
			if (subsequentVelocityFactor == -1) {
				vel[1] = currentVelocity;
			}
			else {
				vel[1] = convertToMidiVelocity(subsequentVelocityFactor);
			}
			return vel;
		}
		else {
			int vel[] = { currentVelocity, currentVelocity };
			return vel;
		}
	}

	private static int convertToMidiVelocity(int vel) {
		int velocity = defaultvalue * vel / 100;
		return MathUtils.clamp(velocity, 0, 127);
	}

	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsType(Staff staff, int voiceNumber,
		MP mp, Score score) {
		Tuple2<DynamicsType, Fraction> latestDynamicsType = getLatestDynamicsBeforePosition(staff,
			voiceNumber, mp, score);
		int iMeasure = mp.measure - 1;
		while (iMeasure >= 0 && latestDynamicsType == null) {
			latestDynamicsType = getLatestDynamicsInMeasure(staff, voiceNumber, iMeasure, score);
			iMeasure--;
		}
		return latestDynamicsType;
	}

	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsBeforePosition(Staff staff,
		int voiceNumber, MP position, Score score) {
		Measure measure = staff.getMeasure(position.measure);
		Voice voice = measure.getVoice(voiceNumber);
		//first look in attached elements
		Tuple2<DynamicsType, Fraction> attached = null;
		for (VoiceElement element : voice.getElements()) {
			Fraction elementBeat = voice.getBeat(element);
			if (elementBeat.compareTo(position.beat) < 1) {
				if (element instanceof Chord) { //TODO: can dynamics also be attached to rests?
					Chord chord = (Chord) element;
					for (Direction direction : chord.getDirections()) {
						if (direction instanceof Dynamics) {
							attached = t(((Dynamics) direction).getType(), elementBeat);
						}
					}
				}
			}
		}
		//then look in measure directions
		Tuple2<DynamicsType, Fraction> inMeasure = null;
		if (measure.getDirections() != null) {
			for (BeatE<Direction> direction : measure.getDirections()) {
				if (direction.beat.compareTo(position.beat) < 1) {
					if (direction.element instanceof Dynamics) {
						inMeasure = t(((Dynamics) direction.element).getType(), direction.beat);
					}
				}
			}
		}
		if (attached != null && inMeasure != null)
			return attached.get2().compareTo(inMeasure.get2()) > 0 ? attached : inMeasure;
		else if (attached != null)
			return attached;
		else
			return inMeasure;
	}

	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsInMeasure(Staff staff,
		int voiceNumber, int measureNumber, Score score) {
		Measure measure = staff.getMeasure(measureNumber);
		//first look for attached dynamics
		Tuple2<DynamicsType, Fraction> attached = null;
		if (voiceNumber < measure.getVoices().size()) {
			Voice voice = measure.getVoices().get(voiceNumber);
			for (VoiceElement element : reverseIt(voice.getElements())) {
				if (element instanceof Chord) { //TODO: can dynamics also be attached to rests?
					Chord chord = (Chord) element;
					for (Direction direction : chord.getDirections()) {
						if (direction instanceof Dynamics) {
							attached = t(((Dynamics) direction).getType(), voice.getBeat(chord));
						}
					}
				}
			}
		}
		//then look in measure directions
		Tuple2<DynamicsType, Fraction> inMeasure = null;
		if (measure.getDirections() != null) {
			for (BeatE<Direction> direction : measure.getDirections()) {
				if (direction.element instanceof Dynamics) {
					inMeasure = t(((Dynamics) direction.element).getType(), direction.beat);
				}
			}
		}
		if (attached != null && inMeasure != null)
			return attached.get2().compareTo(inMeasure.get2()) > 0 ? attached : inMeasure;
		else if (attached != null)
			return attached;
		else
			return inMeasure;
	}

}
