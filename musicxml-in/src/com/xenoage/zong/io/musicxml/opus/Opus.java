package com.xenoage.zong.io.musicxml.opus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.xenoage.zong.io.musicxml.in.OpusFileInput;


/**
 * MusicXML opus, which can contain multiple scores and
 * also other opera.
 * 
 * @author Andreas Wenger
 */
public class Opus
	implements OpusItem
{
	
	private final String title;
	private final List<OpusItem> items;
	
	
	public Opus(String title, List<OpusItem> items)
	{
		this.title = title;
		this.items = items;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	
	public List<OpusItem> getItems()
	{
		return items;
	}
	
	
	/**
	 * Gets a (flattened) list of all filenames in this opus. If this file
	 * contains no opus but a single score, the filename of the single score
	 * is returned.
	 */
	public List<String> getScoreFilenames()
		throws IOException
	{
		LinkedList<String> ret = new LinkedList<String>();
		getScoreFilenames(new OpusFileInput().resolveOpusLinks(this, ""), ret);
		return ret;
	}
	
	
	private void getScoreFilenames(Opus resolvedOpus, LinkedList<String> acc)
	{
		for (OpusItem item : resolvedOpus.getItems())
		{
			if (item instanceof Score)
				acc.add(((Score)item).getHref());
			else if (item instanceof Opus)
				getScoreFilenames((Opus) item, acc);
		}
	}
	

}
