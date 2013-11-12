package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.utils.pdlib.Vector;


/**
 * This class stores
 * the alignment of the accidentals of a chord.
 * 
 * Some rules are adepted from
 * "Ross: The Art of Music Engraving", page 130 ff, and
 * "Chlapik: Die Praxis des Notengraphikers", page 48 ff.
 *
 * @author Andreas Wenger
 */
public final class AccidentalsAlignment
{
  
  private final Vector<AccidentalAlignment> accidentals;
  private final float width;
  
  
  /**
   * Creates a new {@link AccidentalsAlignment}.
   * @param accidentals  the positions of the accidentals
   * @param width        the needed width for all accidentals
   */
  public AccidentalsAlignment(Vector<AccidentalAlignment> accidentals, float width)
  {
  	//must be sorted upwards
  	for (int i = 0; i < accidentals.size() - 1; i++)
  	{
  		if (accidentals.get(i).getLinePosition() > accidentals.get(i + 1).getLinePosition())
  			throw new IllegalArgumentException("Accidentals must be sorted upwards");
  	}
  	this.accidentals = accidentals;
  	this.width = width;
  }
  
  
  /**
   * Gets the accidentals of this chord.
   */
  public Vector<AccidentalAlignment> getAccidentals()
  {
    return accidentals;
  }
  
  
  /**
   * Gets the width of the accidentals of this chord.
   * This is the distance between the left side of
   * the leftmost accidental and the beginning of
   * the notes.
   */
  public float getWidth()
  {
    return width;
  }

}
