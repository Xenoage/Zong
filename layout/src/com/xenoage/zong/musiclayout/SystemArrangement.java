package com.xenoage.zong.musiclayout;


import static com.xenoage.utils.base.collections.CollectionUtils.sum;

import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;


/**
 * A system arrangement is the horizontal and
 * vertical spacing of a system.
 * 
 * It contains the indices of the first and last
 * measure of the system, the widths of all
 * measure columns, the width of the system
 * (which may be longer than the width used
 * by the measures), the distances between the
 * staves and the vertical offset of the system.
 *
 * @author Andreas Wenger
 */
public final class SystemArrangement
{

	//index of the first and last measure
	private final int startMeasureIndex;
	private final int endMeasureIndex;
	
	//list of measure column spacings
	//this will often contain references to the measure column spacings
	//that were computed before, but it can also store new measure column spacings
	//that were created because for example a leading spacing was added
	private final Vector<ColumnSpacing> columnSpacings;
	
	//left and right margin of the system in mm
	private final float marginLeft;
	private final float marginRight;

	//width of the system in mm (may be longer than the summed up widths
	//of the measure columns, e.g. to create empty staves)
	private final float systemWidth;

	//heights and distances of the staves in mm
	private final Vector<Float> staffHeights; //(#staves-1) items
	private final Vector<Float> staffDistances; //(#staves-2) items
	private final float totalHeight;
	
	//vertical offset of the system in mm
	private final float offsetY;


	/**
	 * Creates a {@link SystemArrangement}.
	 * @param startMeasureIndex  index of the first measure in this system, or -1 if none
	 * @param endMeasureIndex    index of the last measure in this system, or -1 if none
	 * @param columnSpacings     list of the layouts of the measure columns in this system
	 * @param marginLeft         left margin of the system in mm
	 * @param marginRight        right margin of the system in mm
	 * @param systemWidth        width of the system in mm (may be longer than the used width) without left margin
	 * @param staffHeights       the heights of each staff in mm
	 * @param staffDistances     the distances of the staves in mm (one less then the number of staves)
	 * @param offsetY            vertical offset of the system in mm
	 */
	public SystemArrangement(int startMeasureIndex, int endMeasureIndex,
		Vector<ColumnSpacing> columnSpacings, float marginLeft, float marginRight, float systemWidth,
		Vector<Float> staffHeights, Vector<Float> staffDistances, float offsetY)
	{
		if (staffHeights.size() != staffDistances.size() + 1)
		{
			throw new IllegalArgumentException("There must be one more staff height that staff distance");
		}
		this.startMeasureIndex = startMeasureIndex;
		this.endMeasureIndex = endMeasureIndex;
		this.columnSpacings = columnSpacings;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.systemWidth = systemWidth;
		this.staffHeights = staffHeights;
		this.staffDistances = staffDistances;
		this.totalHeight = sum(staffHeights) + sum(staffDistances);
		this.offsetY = offsetY;
	}


	/**
	 * Gets the total height of this system in mm.
	 */
	public float getHeight()
	{
		return totalHeight;
	}


	/**
	 * Gets the index of the first measure of the system.
	 */
	public int getStartMeasureIndex()
	{
		return startMeasureIndex;
	}


	/**
	 * Gets the index of the last measure of the system.
	 */
	public int getEndMeasureIndex()
	{
		return endMeasureIndex;
	}


	/**
	 * Gets the height of the staff with the given index.
	 */
	public float getStaffHeight(int index)
	{
		return staffHeights.get(index);
	}
	
	
	/**
	 * Gets the distance between the previous and the given staff.
	 */
	public float getStaffDistance(int index)
	{
		return (index > 0 ? staffDistances.get(index - 1) : 0);
	}
	
	
	/**
	 * Gets the left margin of the system in mm.
	 */
	public float getMarginLeft()
	{
		return marginLeft;
	}
	
	
	/**
	 * Gets the right margin of the system in mm.
	 */
	public float getMarginRight()
	{
		return marginRight;
	}
	
	
	/**
	 * Gets the width of the system (without the horizontal offset).
	 * It may not be equal to the used width. To get the used
	 * width, call getUsedWidth. 
	 */
	public float getWidth()
	{
		return systemWidth;
	}
	
	
	/**
	 * Gets the used width of the system.
	 */
	public float getUsedWidth()
	{
		float ret = 0;
		for (ColumnSpacing mcs : columnSpacings)
		{
			ret += mcs.getWidth();
		}
		return ret;
	}


	/**
	 * Gets the list of the layouts of the measure columns
	 * in this system.
	 */
	public Vector<ColumnSpacing> getColumnSpacings()
	{
		return columnSpacings;
	}
	
	
	/**
   * Gets the vertical offset of the system in mm,
   * relative to the top 
   */
  public float getOffsetY()
  {
    return offsetY;
  }
  
  
  /**
   * Sets the width of the system in mm and returns
   * the changed system arrangement.
   */
  public SystemArrangement withSystemWidth(float systemWidth)
  {
    return new SystemArrangement(startMeasureIndex, endMeasureIndex, columnSpacings,
    	marginLeft, marginRight, systemWidth, staffHeights, staffDistances, offsetY);
  }
  
  
  /**
   * Sets the vertical offset of the system in mm and returns
   * the changed system arrangement.
   */
  public SystemArrangement withOffsetY(float offsetY)
  {
    return new SystemArrangement(startMeasureIndex, endMeasureIndex, columnSpacings,
    	marginLeft, marginRight, systemWidth, staffHeights, staffDistances, offsetY);
  }
  
  
  /**
   * Sets the measure column spacings and width of the system in mm and returns
   * the changed system arrangement.
   */
  public SystemArrangement withSpacings(Vector<ColumnSpacing> measureColumnSpacings,
  	float systemWidth)
  {
    return new SystemArrangement(startMeasureIndex, endMeasureIndex, measureColumnSpacings,
    	marginLeft, marginRight, systemWidth, staffHeights, staffDistances, offsetY);
  }
  

}
