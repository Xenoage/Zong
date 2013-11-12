package com.xenoage.zong.musiclayout.layouter.cache.util;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.stampings.StemStamping;


/**
 * This class is used by the layouter
 * to collect the stems connected by one beam.
 * 
 * The middle stems are stamped last, so only
 * their horizontal and both possible vertical
 * start positions are saved.
 *
 * @author Andreas Wenger
 */
public class BeamedStemStampings
{
	
  private final Beam beam;
  private final StemStamping firstStem;
  private final PVector<OpenBeamMiddleStem> middleStems;
  private final StemStamping lastStem;
  
  
  /**
   * Creates an empty list of beamed stems
   * for the given {@link Beam} instance.
   */
  public BeamedStemStampings(Beam beam)
	{
		this(beam, null, new PVector<OpenBeamMiddleStem>(), null);
	}
  
  
  private BeamedStemStampings(Beam beam, StemStamping firstStem,
		PVector<OpenBeamMiddleStem> middleStems, StemStamping lastStem)
	{
		this.beam = beam;
		this.firstStem = firstStem;
		this.middleStems = middleStems;
		this.lastStem = lastStem;
	}
  
  
  /**
   * Gets the {@link Beam} instance this list of beamed
   * stems belongs to.
   */
  public Beam getBeam()
  {
    return beam;
  }


	/**
   * Gets the first stem of the beam.
   */
  public StemStamping getFirstStem()
  {
    return this.firstStem;
  }
  
  
  /**
   * Sets the first stem of the beam.
   */
  public BeamedStemStampings withFirstStem(StemStamping firstStem)
  {
    return new BeamedStemStampings(beam, firstStem, middleStems, lastStem);
  }
  
  
  /**
   * Gets the number of stems.
   */
  public int getStemsCount()
  {
    return this.middleStems.size() + 2;
  }
  
  
  /**
   * Adds a stem to the middle part of beam.
   */
  public BeamedStemStampings plusMiddleStem(OpenBeamMiddleStem middleStem)
  {
  	return new BeamedStemStampings(beam, firstStem, middleStems.plus(middleStem), lastStem);
  }
  
  
  /**
   * Gets an iterator over the middle stems.
   */
  public PVector<OpenBeamMiddleStem> getMiddleStems()
  {
  	return middleStems;
  }
  
  
  /**
   * Gets the last stem of the beam.
   */
  public StemStamping getLastStem()
  {
    return this.lastStem;
  }
  
  
  /**
   * Sets the last stem of the beam.
   */
  public BeamedStemStampings withLastStem(StemStamping lastStem)
  {
  	return new BeamedStemStampings(beam, firstStem, middleStems, lastStem);
  }
  
  
  /**
   * Gets the horizontal position of the stem with the given index
   * (it may be the first, last or any middle stem).
   */
  public float getStemX(int index)
  {
  	if (index == 0)
  		return this.firstStem.xMm;
  	else if (index == middleStems.size() + 1)
  		return this.lastStem.xMm;
  	else
  		return this.middleStems.get(index - 1).positionX;
  }
  

}
