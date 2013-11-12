package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * This class contains layout information
 * about a chord, like its width and the
 * alignment of the notes, dots,
 * accidentals and articulations.
 *
 * @author Andreas Wenger
 */
public final class ChordNotation
  implements Notation
{
  
  private final Chord element;
  private final ElementWidth width;
  private final NotesAlignment notesAlignment;
  private final StemDirection stemDirection;
  private final StemAlignment stemAlignment;
  private final AccidentalsAlignment accidentalsAlignment;
  private final ArticulationsAlignment articulationsAlignment;
  
  
  /**
   * Creates a new ChordElementLayout for the given
   * chord with the given width, notes alignment
   * accidentals alignment and articulations alignment.
   */
  public ChordNotation(Chord element, ElementWidth width, NotesAlignment notesAlignment,
  	StemDirection stemDirection, StemAlignment stemAlignment, AccidentalsAlignment accidentalsAlignment,
  	ArticulationsAlignment articulationsAlignment)
  {
    this.element = element;
    this.width = width;
    this.notesAlignment = notesAlignment;
    this.stemDirection = stemDirection;
    this.stemAlignment = stemAlignment;
    this.accidentalsAlignment = accidentalsAlignment;
    this.articulationsAlignment = articulationsAlignment;
  }
  
  
  @Override public ElementWidth getWidth()
  {
    return width;
  }
  
  
  public NotesAlignment getNotesAlignment()
  {
    return notesAlignment;
  }
  
  
  public StemDirection getStemDirection()
  {
    return stemDirection;
  }
  
  
  public StemAlignment getStemAlignment()
  {
    return stemAlignment;
  }
  
  
  public AccidentalsAlignment getAccidentalsAlignment()
  {
    return accidentalsAlignment;
  }
  
  
  public ArticulationsAlignment getArticulationsAlignment()
  {
    return articulationsAlignment;
  }
  
  
  /**
   * Gets the chord.
   */
  @Override public Chord getMusicElement()
  {
    return element;
  }
  
  
  public ChordNotation withStemAlignment(StemAlignment stemAlignment)
  {
  	return new ChordNotation(element, width, notesAlignment, stemDirection,
  		stemAlignment, accidentalsAlignment, articulationsAlignment);
  }
  

}
