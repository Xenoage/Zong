package com.xenoage.zong.musiclayout.layouter.beamednotation.direction;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterTest;
import com.xenoage.zong.musiclayout.layouter.beamednotation.direction.SingleMeasureSingleStaffStrategy;
import com.xenoage.zong.musiclayout.notations.beam.BeamStemDirections;
import com.xenoage.zong.musiclayout.notations.chord.ChordLinePositions;


/**
 * Test class for the {@link SingleMeasureSingleStaffStrategy}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class SingleMeasureSingleStaffStrategyTest
{
  
	SingleMeasureSingleStaffStrategy strategy = new SingleMeasureSingleStaffStrategy(
		ScoreLayouterTest.getNotationStrategy());
  
  
  @Test public void computeBeamStemDirectionsTest()
  {
    int linescount = 5;
    int[][] lineposition = new int[4][1];
    StemDirection[] dir;
    BeamStemDirections res;

    lineposition[0][0] = 1;
    lineposition[1][0] = 2;
    lineposition[2][0] = 3;
    lineposition[3][0] = 6;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);
    

    lineposition[0][0] = 5;
    lineposition[1][0] = 3;
    lineposition[2][0] = 2;
    lineposition[3][0] = 0;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);
    
    lineposition[0][0] = 5;
    lineposition[1][0] = 3;
    lineposition[2][0] = 0;
    lineposition[3][0] = 5;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);

    
    lineposition = new int[6][1];
    
    lineposition[0][0] = -2;
    lineposition[1][0] = -1;
    lineposition[2][0] = 0;
    lineposition[3][0] = 1;
    lineposition[4][0] = 2;
    lineposition[5][0] = 3;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);

    
    lineposition[0][0] = 2;
    lineposition[1][0] = 3;
    lineposition[2][0] = 4;
    lineposition[3][0] = 5;
    lineposition[4][0] = 6;
    lineposition[5][0] = 7;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Down, res.getStemDirections()[0]);

    
    lineposition[0][0] = 2;
    lineposition[1][0] = 3;
    lineposition[2][0] = 5;
    lineposition[3][0] = 7;
    lineposition[4][0] = 5;
    lineposition[5][0] = 3;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Down, res.getStemDirections()[0]);

    
    lineposition[0][0] = 14;
    lineposition[1][0] = 9;
    lineposition[2][0] = 5;
    lineposition[3][0] = 7;
    lineposition[4][0] = 1;
    lineposition[5][0] = -2;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Down, res.getStemDirections()[0]);

    
    lineposition[0][0] = 0;
    lineposition[1][0] = -1;
    lineposition[2][0] = -2;
    lineposition[3][0] = 1;
    lineposition[4][0] = 3;
    lineposition[5][0] = 5;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);

    
    lineposition[0][0] = -2;
    lineposition[1][0] = 4;
    lineposition[2][0] = 7;
    lineposition[3][0] = 8;
    lineposition[4][0] = 7;
    lineposition[5][0] = 4;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Down, res.getStemDirections()[0]);

    
    lineposition[0][0] = -3;
    lineposition[1][0] = 4;
    lineposition[2][0] = 7;
    lineposition[3][0] = 8;
    lineposition[4][0] = 7;
    lineposition[5][0] = 4;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Down, res.getStemDirections()[0]);
    

    lineposition[0][0] = 0;
    lineposition[1][0] = 3;
    lineposition[2][0] = 5;
    lineposition[3][0] = 7;
    lineposition[4][0] = 5;
    lineposition[5][0] = 3;
    dir = computeSingleNoteChordsDirections(lineposition);
    
    res = strategy.computeBeamStemDirections(clps(lineposition), dir, linescount);
    assertEquals(StemDirection.Up, res.getStemDirections()[0]);
  }
  
  
  private ChordLinePositions[] clps(int[][] linePositions)
  {
    ChordLinePositions[] ret = new ChordLinePositions[linePositions.length];
    for (int i = 0; i < linePositions.length; i++)
      ret[i] = new ChordLinePositions(linePositions[i]);
    return ret;
  }
  
  
  private StemDirection[] computeSingleNoteChordsDirections(int[][] linePositions)
  {
  	StemDirection[] ret = new StemDirection[linePositions.length];
  	for (int i = 0; i < ret.length; i++)
  	{
  		ret[i] = linePositions[i][0] < 4 ? Up : Down;
  	}
  	return ret;
  }
  

}
