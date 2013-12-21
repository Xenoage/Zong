package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.base.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.kernel.Tuple2.t;

import com.xenoage.utils.base.settings.Settings;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Attachable;
import com.xenoage.zong.core.music.Globals;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.position.BMP;


/**
 * This class converts dynamics.
 * 
 * @author Uli Teschemacher
 */
public final class MidiVelocityConverter
{

	private static final int defaultvalue = 64;


	public static int getNumberOfVoicesInStaff(Staff staff)
	{
		int voiceCount = 0;
		for (Measure measure : staff.measures)
		{
			int size = measure.voices.size();
			voiceCount = MathUtils.clampMin(voiceCount, size);
		}
		return voiceCount;
	}


	public static int getVelocity(Chord chord, int currentVelocity,
		Globals globals)
	{
		for (Attachable attachment : globals.getAttachments().get(chord))
		{
			if (attachment instanceof Dynamics)
			{
				Dynamics dynamic = (Dynamics) attachment;
				int setting = Settings.getInstance().getSetting(
					dynamic.getType().name(), "playback-dynamics", currentVelocity);
				return convertToMidiVelocity(setting);
			}
		}
		return currentVelocity;
	}


	private static boolean voiceHasDynamics(Staff staff, int voiceIndex,
		Globals globals)
	{
		for (Measure measure : staff.measures)
		{
			if (measure.voices.size() > voiceIndex)
			{
				Vector<VoiceElement> elements = measure.getVoice(voiceIndex).elements;
				for (VoiceElement element : elements)
				{
					for (Attachable attachment : globals.getAttachments().get(element))
					{
						if (attachment instanceof Dynamics)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}


	private static boolean[] voicesInStaffHaveDynamics(Staff staff, Globals globals)
	{
		int numberOfVoices = getNumberOfVoicesInStaff(staff);
		boolean[] dyn = new boolean[numberOfVoices];
		for (int i = 0; i < numberOfVoices; i++)
		{
			dyn[i] = voiceHasDynamics(staff, i, globals);
		}
		return dyn;
	}


	private static boolean staffHasDynamics(boolean[] dynamics)
	{
		for (boolean b : dynamics)
		{
			if (b)
			{
				return true;
			}
		}
		return false;
	}


	private static int getFirstVoiceWithDynamics(boolean[] dynamics)
	{
		for (int i = 0; i < dynamics.length; i++)
		{
			if (dynamics[i])
			{
				return i;
			}
		}
		return 0;
	}


	public static int[] getVoiceforDynamicsInStaff(Staff staff, Globals globals)
	{
		boolean[] voicesInStaffHaveDynamics = voicesInStaffHaveDynamics(staff, globals);
		int[] dynamicVoices = new int[voicesInStaffHaveDynamics.length];
		// If there are no dynamics in any voice, every voice can use its own
		// "dynamics"
		if (!staffHasDynamics(voicesInStaffHaveDynamics))
		{
			for (int i = 0; i < voicesInStaffHaveDynamics.length; i++)
			{
				dynamicVoices[i] = i;
			}
		}
		else
		{
			for (int i = 0; i < dynamicVoices.length; i++)
			{
				if (voicesInStaffHaveDynamics[i])
				{
					dynamicVoices[i] = i;
				}
				else
				{
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
	public static int[] getVelocityAtPosition(Staff staff, int voice,
		BMP bmp, int currentVelocity, Score score)
	{
		Tuple2<DynamicsType, Fraction> latestDynamicsType = getLatestDynamicsType(
			staff, voice, bmp, score);
		if (latestDynamicsType != null)
		{
			int vel[] = new int[2];
			int velocityFactorAtBeat = Settings.getInstance().getSetting(
				latestDynamicsType.get1().name(), "playback-dynamics", -1);
			if (velocityFactorAtBeat == -1)
			{
				vel[0] = currentVelocity;
			}
			else
			{
				vel[0] = convertToMidiVelocity(velocityFactorAtBeat);				
			}
			int subsequentVelocityFactor = Settings.getInstance().getSetting(
				latestDynamicsType.get1().name() + "_subsequent", "playback-dynamics",
				velocityFactorAtBeat);

			if (latestDynamicsType.get2().compareTo(bmp.beat) != 0)
			{
				if (subsequentVelocityFactor != -1)
				{
					vel[0] = convertToMidiVelocity(subsequentVelocityFactor);
				}
				else
				{
					vel[0] = currentVelocity;
				}
			}
			if (subsequentVelocityFactor == -1)
			{
				vel[1] = currentVelocity;
			}
			else
			{
				vel[1] = convertToMidiVelocity(subsequentVelocityFactor);
			}
			return vel;
		}
		else
		{
			int vel[] = { currentVelocity, currentVelocity };
			return vel;
		}
	}


	private static int convertToMidiVelocity(int vel)
	{
		int velocity = defaultvalue * vel / 100;
		return MathUtils.clamp(velocity, 0, 127);
	}
	
	
	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsType(Staff staff,
		int voiceNumber, BMP bmp, Score score)
	{
		Tuple2<DynamicsType, Fraction> latestDynamicsType = getLatestDynamicsBeforePosition(
			staff, voiceNumber, bmp, score);
		int iMeasure = bmp.measure - 1;
		while (iMeasure >= 0 && latestDynamicsType == null)
		{
			latestDynamicsType = getLatestDynamicsInMeasure(staff, voiceNumber, iMeasure, score);
			iMeasure--;
		}
		return latestDynamicsType;
	}


	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsBeforePosition(
		Staff staff, int voiceNumber, BMP position, Score score)
	{
		Measure measure = staff.measures.get(position.measure);
		Voice voice = measure.voices.get(voiceNumber);
		//first look in attached elements
		Tuple2<DynamicsType, Fraction> attached = null;
		for (VoiceElement element : voice.elements)
		{
			BMP elementMP = score.getBMP(element);
			if (elementMP.beat.compareTo(position.beat) < 1)
			{
				for (Attachable attachment : score.globals.getAttachments().get(element))
				{
					if (attachment instanceof Dynamics)
					{
						attached = t(((Dynamics) attachment).getType(), elementMP.beat);
					}
				}
			}
		}
		//then look in measure directions
		Tuple2<DynamicsType, Fraction> inMeasure = null;
		for (BeatE<Direction> direction : measure.directions)
		{
			if (direction.beat.compareTo(position.beat) < 1)
			{
				if (direction.element instanceof Dynamics)
				{
					inMeasure = t(((Dynamics) direction.element).getType(), direction.beat);
				}
			}
		}
		if (attached != null && inMeasure != null)
			return attached.get2().compareTo(inMeasure.get2()) > 0 ?
				attached : inMeasure;
		else if (attached != null)
			return attached;
		else
			return inMeasure;
	}


	private static Tuple2<DynamicsType, Fraction> getLatestDynamicsInMeasure(
		Staff staff, int voiceNumber, int measureNumber, Score score)
	{
		Measure measure = staff.measures.get(measureNumber);
		//first look for attached dynamics
		Tuple2<DynamicsType, Fraction> attached = null;
		if (voiceNumber < measure.voices.size())
		{
			Voice voice = measure.voices.get(voiceNumber);
			for (VoiceElement element : reverseIt(voice.elements))
			{
				for (Attachable attachment : score.globals.getAttachments().get(element))
				{
					if (attachment instanceof Dynamics)
					{
						attached = t(((Dynamics) attachment).getType(), score.getBMP(element).beat);
					  break;
					}
				}
			}
		}
		//then look in measure directions
		Tuple2<DynamicsType, Fraction> inMeasure = null;
		for (BeatE<Direction> direction : measure.directions)
		{
			if (direction.element instanceof Dynamics)
			{
				inMeasure = t(((Dynamics) direction.element).getType(), direction.beat);
			}
		}
		if (attached != null && inMeasure != null)
			return attached.get2().compareTo(inMeasure.get2()) > 0 ?
				attached : inMeasure;
		else if (attached != null)
			return attached;
		else
			return inMeasure;
	}
	
}
