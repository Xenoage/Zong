package com.xenoage.zong.musiclayout;

import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;


/**
 * A {@link MeasureMarks} class stores positioning
 * information on the elements of a measure within
 * a {@link StaffStamping}.
 * 
 * The horizontal coordinates of the start and ending of the
 * measure is saved here.
 * 
 * It contains also the relevant ScorePositions
 * and their horizontal positions within the measure in mm
 * (relative to the beginning of the staff).
 * Coordinates can be converted to ScorePositions and backwards.
 * The voice of the positions are not relevant.
 * 
 * There is no validation of the given data, so be careful what you do.
 * 
 * @author Andreas Wenger
 */
public final class MeasureMarks
{

	private final int measure;
	private final float startMm;
	private final float leadingMm;
  private final float endMm;
  private final PVector<BeatOffset> beatOffsets;
  
  
  /**
   * Creates a new {@link MeasureMarks} instance.
   * @param measure      the index of the measure
   * @param startMm      start position of the measure in mm relative to the beginning of the staff
   * @param leadingMm    end position of the leading spacing in mm relative to the beginning of the staff.
   *                     If there is no leading spacing, this value is equal to <code>startMm</code>
   * @param endMm        end position of the measure in mm relative to the beginning of the staff
   * 
   * @param beatOffsets  offsets for the beats in the measure (at least one)
   */
  public MeasureMarks(int measure, float startMm, float leadingMm, float endMm,
  	PVector<BeatOffset> beatOffsets)
  {
  	if (beatOffsets.size() == 0)
  		throw new IllegalArgumentException("At least one beat must be given");
  	this.measure = measure;
  	this.startMm = startMm;
  	this.leadingMm = leadingMm;
  	this.endMm = endMm;
  	this.beatOffsets = beatOffsets;
  }
  
  
  public int getMeasure()
  {
  	return measure;
  }
  
  
  public float getStartMm()
  {
  	return startMm;
  }
  
  
  public float getLeadingMm()
  {
  	return leadingMm;
  }
  
  
  public float getEndMm()
  {
  	return endMm;
  }
  
  
  public boolean contains(float xMm)
  {
  	return MathUtils.between(xMm, startMm, endMm);
  }
  
  
  /**
   * Gets the beat at the given horizontal position in mm.
   * 
   * If it is between two marks (which will be true almost ever), the
   * the right mark is selected (like it is usual e.g. in text
   * processing applications). If it is behind all known marks,
   * the last known beat is returned.
   */
  public Fraction getBeatAt(float xMm)
  {
  	for (int i = 0; i < beatOffsets.size(); i++)
  	{
  		if (xMm <= beatOffsets.get(i).getOffsetMm())
  			return beatOffsets.get(i).getBeat();
  	}
  	return beatOffsets.getLast().getBeat();
  }
  
  
  /**
   * Gets the horizontal position in mm, relative to the beginning of the staff,
   * of the given beat.
   * If the given beat is not within this list, the next available beat within
   * this measure is used, or the last beat if the given beat is behind all
   * known beats.
   */
  public float getXMmAt(Fraction beat)
  {
  	for (int i = 0; i < beatOffsets.size(); i++)
  	{
  		if (beat.compareTo(beatOffsets.get(i).getBeat()) <= 0)
  			return beatOffsets.get(i).getOffsetMm();
  	}
  	return beatOffsets.getLast().getOffsetMm();
  }
  
  
  /**
   * Gets all {@link BeatOffset}s.
   */
  public PVector<BeatOffset> getBeatOffsets()
  {
  	return beatOffsets;
  }
  
	
}
