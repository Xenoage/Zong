package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import com.xenoage.zong.musiclayout.SystemArrangement;


/**
 * This horizontal system filling strategy
 * changes the length of all staves of
 * the given system to their maximum width.
 * 
 * @author Andreas Wenger
 */
public class EmptyStavesHorizontalSystemFillingStrategy
  implements HorizontalSystemFillingStrategy
{
	
	private static EmptyStavesHorizontalSystemFillingStrategy instance = null;
	
	
	public static EmptyStavesHorizontalSystemFillingStrategy getInstance()
	{
		if (instance == null)
			instance = new EmptyStavesHorizontalSystemFillingStrategy();
		return instance;
	}
	
	
	private EmptyStavesHorizontalSystemFillingStrategy()
	{
	}
	
  
  /**
   * Changes the length of all staves of the given system to their maximum width.
   */
  @Override public SystemArrangement computeSystemArrangement(
  	SystemArrangement systemArrangement, float usableWidth)
  {
  	return systemArrangement.withSystemWidth(usableWidth);
  }
  

}
