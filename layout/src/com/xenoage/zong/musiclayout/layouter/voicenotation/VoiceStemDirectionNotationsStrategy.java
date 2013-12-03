package com.xenoage.zong.musiclayout.layouter.voicenotation;

import com.xenoage.zong.musiclayout.layouter.ScoreLayouterContext;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;

/**
 * This strategy computes the directions of the stems
 * if there is more than one voice in a staff.
 * 
 * If stem directions have to be changed, their chord
 * notations are recomputed too.
 * 
 * TODO: this strategy has to be revised in a team meeting.
 * at the moment is does nothing (by returning immediately).
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class VoiceStemDirectionNotationsStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	@SuppressWarnings("unused") private final NotationStrategy notationStrategy;


	/**
	 * Creates a new {@link VoiceStemDirectionNotationsStrategy}.
	 */
	public VoiceStemDirectionNotationsStrategy(NotationStrategy notationStrategy) {
		this.notationStrategy = notationStrategy;
	}

	/**
	 * TODO
	 * @param staff      
	 * @param notations  
	 * @param lc         
	 */
	public NotationsCache computeNotations(int staff, NotationsCache notations,
		ScoreLayouterContext lc) {
		//TEST/TODO/TEAM: do nothing. this strategy has to be revised in a team meeting
		return new NotationsCache();
		/*
		Score score = lc.getScore();
		int numberOfMeasures = score.getMeasuresCount();
		int[] numberOfNotes = new int[]{0,0};
		long[] summatedLinesCount = new long[]{0,0};
		Hashtable<Integer, ArrayList<Chord>> voicechords = new Hashtable<Integer, ArrayList<Chord>>();
		voicechords.put(0, new ArrayList<Chord>());
		voicechords.put(1, new ArrayList<Chord>());
		
		for (int iMeasure = 0; iMeasure < numberOfMeasures; iMeasure++)
		{
			Measure measure = score.getMeasure(atMeasure(staff, iMeasure));
			List<Voice> voices = measure.getVoices();
			for (int b = 0; b < 2; b++)
			{
				if (b < voices.size())	//check if there are "enough" voices
				{
					Voice voice = voices.get(b);
					Vector<VoiceElement> musicElements = voice.getElements();
					for (MusicElement me : musicElements)
					{
						if (me instanceof Chord)
						{
							//add chord to the arraylist of the voice for correction and count the linepositions
							Chord chord = (Chord)me;
							voicechords.get(b).add(chord);
							ChordNotation notation = notations.getChord(chord);
							ChordLinePositions clp = notation.getNotesAlignment().getLinePositions();
							for (int c = 0; c < clp.getNotesCount(); c++)
							{
								summatedLinesCount[b] += clp.get(c);
								numberOfNotes[b]++;
							}
						}
					}
				}
			}
		}
		
		//TODO: the following line was just added by Andi to avoid  / by zero exception
		if (summatedLinesCount[1] == 0 || summatedLinesCount[0] == 0) return NotationsCache.create();
		
		//check whether it is needed to change the stemdirection - if one voice is much smaller there is no need (less than 20%)
		NotationsCache ret = NotationsCache.create();
		if (!(summatedLinesCount[1]/summatedLinesCount[0]> 0.8 && summatedLinesCount[0]/summatedLinesCount[1] > 0.8))
		{
			int up,down;
			float[] averageLinePosition = new float[2];
			for (int i = 0; i < 2; i++)
			{
				if (numberOfNotes[i] > 0) //AVOID java.lang.ArithmeticException: / by zero
					averageLinePosition[i] = summatedLinesCount[i] / numberOfNotes[i];
			}
			if (averageLinePosition[0] >= averageLinePosition[1])
			{
				//voice 1 up - voice 2 down
				up = 0;
				down = 1;
			}
			else
			{
				//voice 1 down - voice 2 up
				up = 1;
				down = 0;
			}
			for (Chord chord : voicechords.get(up))
			{
				ret.add(notationStrategy.computeChord(chord, Up, score, lc.layoutSettings), chord);
			}
			for (Chord chord : voicechords.get(down))
			{
				ret.add(notationStrategy.computeChord(chord, Down, score, lc.layoutSettings), chord);
			}
		}
		return ret;
		//*/
	}

}
