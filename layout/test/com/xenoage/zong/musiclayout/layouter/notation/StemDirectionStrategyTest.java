package com.xenoage.zong.musiclayout.layouter.notation;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static org.junit.Assert.*;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notations.chord.ChordLinePositions;


/**
 * Test class for the {@link StemDirectionStrategy}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class StemDirectionStrategyTest
{
  
	StemDirectionStrategy strategy = new StemDirectionStrategy();
  
  
	@Test public void computeStemDirectionTest()
	{
		int linesCount = 5;
		int[] linePositions;
		
		//single notes
		linePositions = new int[1];
		for (int i = -2; i<=3; i++)
		{
			linePositions[0] = i;
			assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
		}
		for (int i = 4; i<=13; i++)
		{
			linePositions[0] = i;
			assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
		}

		//Chords with 2 notes
		//Stem down
		linePositions = new int[2];
		linePositions[0] = 2;
		linePositions[1] = 7;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
		
		linePositions[0] = 1;
		linePositions[1] = 9;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = 0;
		linePositions[1] = 9;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = 3;
		linePositions[1] = 6;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = 4;
		linePositions[1] = 6;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = 3;
		linePositions[1] = 7;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = 2;
		linePositions[1] = 8;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions[0] = -1;
		linePositions[1] = 11;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		//stem up
		linePositions[0] = -1;
		linePositions[1] = 6;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 1;
		linePositions[1] = 5;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 2;
		linePositions[1] = 4;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 0;
		linePositions[1] = 7;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = -1;
		linePositions[1] = 8;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = -2;
		linePositions[1] = 9;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = -4;
		linePositions[1] = 9;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		//chords with more than 2 notes
		linePositions = new int[3];
		linePositions[0] = 1;
		linePositions[1] = 3;
		linePositions[2] = 6;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 3;
		linePositions[1] = 6;
		linePositions[2] = 8;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 2;
		linePositions[1] = 5;
		linePositions[2] = 7;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions[0] = 0;
		linePositions[1] = 4;
		linePositions[2] = 7;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions = new int[4];
		linePositions[0] = -4;
		linePositions[1] = 1;
		linePositions[2] = 5;
		linePositions[3] = 8;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		linePositions = new int[5];
		linePositions[0] = -2;
		linePositions[1] = 2;
		linePositions[2] = 5;
		linePositions[3] = 10;
		linePositions[4] = 14;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
	
		//chords with the same distance from top and lowest note
		linePositions = new int[4];
		linePositions[0] = 0;
		linePositions[1] = 4;
		linePositions[2] = 6;
		linePositions[3] = 8;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));
		
		linePositions = new int[3];
		linePositions[0] = 1;
		linePositions[1] = 5;
		linePositions[2] = 7;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions = new int[3];
		linePositions[0] = -1;
		linePositions[1] = 6;
		linePositions[2] = 9;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions = new int[5];
		linePositions[0] = -2;
		linePositions[1] = 1;
		linePositions[2] = 5;
		linePositions[3] = 8;
		linePositions[4] = 10;
		assertEquals(Down, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions = new int[4];
		linePositions[0] = 0;
		linePositions[1] = 2;
		linePositions[2] = 4;
		linePositions[3] = 8;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions = new int[3];
		linePositions[0] = 1;
		linePositions[1] = 3;
		linePositions[2] = 7;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));

		linePositions = new int[5];
		linePositions[0] = -2;
		linePositions[1] = 1;
		linePositions[2] = 3;
		linePositions[3] = 8;
		linePositions[4] = 10;
		assertEquals(Up, strategy.computeStemDirection(clp(linePositions), linesCount));
	}

	
	private ChordLinePositions clp(int[] linePositions)
	{
		return new ChordLinePositions(linePositions);
	}
	
	
}