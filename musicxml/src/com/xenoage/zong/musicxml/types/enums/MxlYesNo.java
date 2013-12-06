package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.invalid;

import org.w3c.dom.Element;


/**
 * Reader and writer methods for MusicXML's yes-no,
 * since yes-no is represented by boolean values in this project.
 * 
 * @author Andreas Wenger
 */
public class MxlYesNo
{
	
	
	public static boolean read(String s, Element e)
	{
		if (s.equals("yes"))
			return true;
		else if (s.equals("no"))
			return false;
		throw invalid(e);
	}
	
	
	public static Boolean readNull(String s, Element e)
	{
		if (s == null)
			return null;
		else
			return read(s, e);
	}
	
	
	public static String write(boolean v)
	{
		return v ? "yes" : "no";
	}

}
