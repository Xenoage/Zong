package com.xenoage.zong.layout.frames;



/**
 * A trivial frame just for testing.
 * 
 * @author Andreas Wenger
 */
public class TestFrame
	extends Frame
{

	public TestFrame()
	{
		super(new FrameData(null, null, 0, null));
	}

	@Override public FrameType getType()
	{
		return null;
	}

	@Override public Frame withData(FrameData data)
	{
		throw new UnsupportedOperationException();
	}

}
