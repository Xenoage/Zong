package com.xenoage.zong.musiclayout.notations.chord;



/**
 * This class represents the alignment
 * of the notes and the dots of a chord.
 *
 * @author Andreas Wenger
 */
public final class NotesAlignment
{
	
  private final float width;
  
  private final NoteAlignment[] notes; //always sorted upwards
  
  private final float[] dotsOffsets;
  private final int[] dotLPs;
  
  private final float stemOffset;
  private final float leftSuspendedWidth;
  
  
  /**
   * Creates a new {@link NotesAlignment}.
   * @param dotLPs  line positions of the dots, or an empty array if chord has no dots
   */
  public NotesAlignment(float width, NoteAlignment[] notes, float[] dotsOffsets,
  	int[] dotLPs, float stemOffset, float leftSuspendedWidth)
  {
  	this.width = width;
  	this.notes = notes;
  	this.dotsOffsets = dotsOffsets;
  	this.dotLPs = dotLPs;
  	this.stemOffset = stemOffset;
    this.leftSuspendedWidth = leftSuspendedWidth;
  }
  
  
  /**
   * Gets the horizontal offset of the stem in spaces.
   */
  public float getStemOffset()
  {
    return stemOffset;
  }
  
  
  /**
   * Gets the width of the chord notes and dots in spaces.
   */
  public float getWidth()
  {
    return width;
  }
  
  
  /**
   * Gets the number of notes.
   */
  public int getNotesCount()
  {
    return notes.length;
  }
  
  
  /**
   * Gets the alignment of the note with the given index.
   * The notes are sorted upwards, that means, the lowest
   * note has index 0.
   */
  public NoteAlignment getNoteAlignment(int index)
  {
    return notes[index];
  }
  
  
  /**
   * Gets the number of dots per note.
   */
  public int getDotsPerNoteCount()
  {
    return dotsOffsets.length;
  }
  
  
  /**
   * Gets the alignment of the top note of the chord.
   */
  public NoteAlignment getTopNoteAlignment()
  {
  	return notes[notes.length - 1];
  }
  
  
  /**
   * Gets the alignment of the bottom note of the chord.
   */
  public NoteAlignment getBottomNoteAlignment()
  {
  	return notes[0];
  }
  
  
  /**
   * Gets the line positions of the dots, or an empty array, if the
   * chord has no dots.
   */
  public int[] getDotLPs()
  {
    return dotLPs;
  }
  
  
  /**
   * Gets the width of the left suspended notes in IS.
   * If there are no left-suspended notes, this will be 0.
   */
  public float getLeftSuspendedWidth()
  {
  	return leftSuspendedWidth;
  }
  
  
  /**
   * Gets the offset of the dots with the given index (0 or 1).
   */
  public float getDotsOffset(int dot)
  {
    return dotsOffsets[dot];
  }
  
  
  /**
   * Gets the list of note alignments.
   * The notes are sorted upwards, that means, the
   * lowest note has index 0.
   */
  public NoteAlignment[] getNoteAlignments()
  {
    return notes;
  }
  
  
  /**
   * Gets the line positions of the chord (convenience method).
   */
  public ChordLinePositions getLinePositions()
  {
  	int[] ret = new int[notes.length];
  	for (int i = 0; i < ret.length; i++)
  		ret[i] = notes[i].getLinePosition();
  	return new ChordLinePositions(ret);
  }

}
