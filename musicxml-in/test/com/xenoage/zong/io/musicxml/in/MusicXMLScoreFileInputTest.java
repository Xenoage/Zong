package com.xenoage.zong.io.musicxml.in;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.TestIO;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.util.InconsistentScoreException;
import com.xenoage.zong.test.ScoreTest;


/**
 * Tests for the {@link MusicXMLScoreFileInput} class.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLScoreFileInputTest
{
	
	public static final String dir11 = "../shared/data/test/musicxml11/";
	public static final String dir20 = "../shared/data/test/musicxml20/";
	
	
	@Before public void setUp()
	{
		TestIO.initWithSharedDir();
	}
	
	
	/**
   * It's too hard to check the contents of a MusicXML file
   * automatically. We just try to load the score data and
   * check if they could be loaded without problems and if
   * the scores are consistent.
   * 
   * We check all official MusicXML 1.1 and 2.0 sample files.
   */
  @Test @SuppressWarnings("unused") public void testSampleFiles()
  {
  	long startTime = System.currentTimeMillis();
    for (String file : getSampleFiles())
    {
    	try
    	{
	      Score score = new MusicXMLScoreFileInput().read(new FileInputStream(file), file);
	      ScoreTest.assertConsistency(score);
	      System.out.println("Loaded: " + file);
    	}
    	catch (InconsistentScoreException ex)
      {
        ex.printStackTrace();
        fail("Score inconsistent after loading: " + file);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        fail("Failed to load file: " + file);
      }
    }
    long totalTime = System.currentTimeMillis() - startTime;
    //TEST
    //System.out.println(getClass() + ": " + totalTime);
  }
  
  
  /**
   * It's too hard to check the contents of a MusicXML file
   * automatically. We just try to load the score data and
   * check if they could be loaded without problems.
   * 
   * We check all official MusicXML 1.1 sample files.
   */
  public static List<String> getSampleFiles()
  {
  	List<String> ret = new LinkedList<String>();
    String[] files = new File(dir11).list(FileUtils.getXMLFilter());
    for (String file : files)
    {
    	ret.add(dir11 + file);
    }
    files = new File(dir20).list(FileUtils.getXMLFilter());
    for (String file : files)
    {
    	ret.add(dir20 + file);
    }
    return ret;
  }
  
  
  /**
   * Loads the given file from "../shared/data/test/scores/" as MusicXML 1.1
   * and returns the score.
   */
  public static Score loadXMLTestScore(String filename)
  {
  	try
  	{
  		String filepath = "../shared/data/test/scores/" + filename;
  		return new MusicXMLScoreFileInput().read(new FileInputStream(filepath), filepath);
  	}
  	catch (Exception ex)
  	{
  		ex.printStackTrace();
  		fail("Could not load file: " + filename);
  		return null;
  	}
  }
  
  
  /**
   * Tests a single file.
   */ //*
  @org.junit.Ignore //*/
  @Test @SuppressWarnings("unused") public void testSingleFile()
  {
  	String file = "../shared/data/test/musicxml20/BeetAnGeSample.xml";
  	try
  	{
	    Score score = new MusicXMLScoreFileInput().read(new FileInputStream(file), file);
	    
	    //TEST
	    //ScoreTest.printScore(score);
	  }
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail("Could not load file: " + file);
		}
  }
  

}
