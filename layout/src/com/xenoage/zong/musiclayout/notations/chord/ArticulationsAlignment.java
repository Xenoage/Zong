package com.xenoage.zong.musiclayout.notations.chord;


/**
 * This class computes and stores
 * the alignment of the articulations of a chord.
 *
 * @author Andreas Wenger
 */
public final class ArticulationsAlignment
{ 
  
  private final ArticulationAlignment[] articulations;
  private final float totalHeightIS;
  
  
  /**
   * Creates a new {@link ArticulationsAlignment}.
   * @param articulations  the positions of the articulations
   * @param totalHeightIS  the needed height for all articulations in IS,
   *                       including the space between the outer chord
   *                       and the first articulation
   */
  public ArticulationsAlignment(ArticulationAlignment[] articulations, float totalHeightIS)
  {
  	this.articulations = articulations;
  	this.totalHeightIS = totalHeightIS;
  }
  
  
  /**
   * Gets the articulations.
   */
  public ArticulationAlignment[] getArticulations()
  {
    return articulations;
  }
  
  
  /**
   * Gets the total height of the articulations in IS,
   * including the space between the outer chord
   * and the first articulation.
   */
  public float getTotalHeightIS()
  {
    return totalHeightIS;
  }
  
}
