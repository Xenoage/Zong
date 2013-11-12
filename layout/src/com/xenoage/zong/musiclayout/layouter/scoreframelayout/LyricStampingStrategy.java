package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.text.FormattedText.fText;
import static com.xenoage.zong.text.FormattedTextUtils.styleText;

import com.xenoage.utils.base.StringUtils;
import com.xenoage.utils.base.iterators.It;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.text.Lyric;
import com.xenoage.zong.core.music.text.Lyric.SyllableType;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.text.FormattedTextStyle;


/**
 * This strategy computes the stamping of a lyric.
 * 
 * @author Andreas Wenger
 */
public class LyricStampingStrategy
	implements ScoreLayouterStrategy
{
	
	
	/**
	 * Creates and returns the {@link StaffTextStamping} for the given {@link Lyric},
	 * using the given text style, that belongs to the given parent staff.
	 * The horizontal position in mm and the vertical position (baseline)
	 * as a line position are also given.
	 * If it is a extend syllable, null is returned.
	 */
	public StaffTextStamping createSyllableStamping(Lyric lyric, FormattedTextStyle style,
		StaffStamping staffStamping, float positionX, float baseLinePosition)
	{
		if (lyric.getSyllableType() == SyllableType.Extend)
			return null;
		FormattedText text = styleText(lyric.getText(), style, Alignment.Center);
		return new StaffTextStamping(staffStamping, lyric, text,
			sp(positionX, baseLinePosition));
	}
	
	
	/**
	 * Creates and returns the {@link StaffTextStamping} containing
	 * a hypen ("-") between the two given lyric stampings.
	 */
	public StaffTextStamping createHyphenStamping(StaffTextStamping syllableLeft,
		StaffTextStamping syllableRight, FormattedTextStyle style)
	{
		FormattedText text = fText("-", style, Alignment.Center);
		float positionX;
		float widthLeft = syllableLeft.text.getFirstParagraph().getWidthMm();
		if (syllableLeft.parentStaff == syllableRight.parentStaff)
		{
			//syllables are in same staff
			//the horizontal position of the hyphen is in the middle of the empty
			//space between the left and right syllable
			float widthRight = syllableRight.text.paragraphs.getFirst().getWidthMm();
			positionX = ((syllableLeft.position.xMm + widthLeft / 2) +
				(syllableRight.position.xMm - widthRight / 2)) / 2;
		}
		else
		{
			//right syllable is in a new staff
			//place the hypen to the right side of the first syllable
			positionX = (syllableLeft.position.xMm + widthLeft / 2) +
				2 * text.paragraphs.getFirst().getWidthMm();
		}
		return new StaffTextStamping(
			syllableLeft.parentStaff, (Lyric) syllableLeft.musicElement, //hyphen belongs to the left syllable
			text, sp(positionX, syllableLeft.position.lp));
	}
	
	
	/**
	 * Creates and returns at least one {@link StaffTextStamping} containing
	 * an underscore line ("___") behind the given lyric stamping
	 * up to the given notehead stamping.
	 * If more than one line is needed, a stamping for each line is returned.
	 * 
	 * A list of consecutive staff stampings must be given, containing at least
	 * the beginning and the ending staff of the underscore. If the underscore needs
	 * only one staff, it may also be null.
	 * 
	 * The left syllable must be given, even if it is not in the current frame.
	 * The right notehead is optional. If not given, the underscore is drawn over 
	 * all given following staves.
	 */
	public PVector<StaffTextStamping> createUnderscoreStampings(StaffTextStamping syllableLeft,
		NoteheadStamping noteheadRight, FormattedTextStyle style, Vector<StaffStamping> staffStampings)
	{
		if (syllableLeft == null)
			throw new IllegalArgumentException("Left syllable must be given");
		PVector<StaffTextStamping> ret = pvec();
		//measure width of "_"
		float widthU = fText("_", style, Alignment.Center).getFirstParagraph().getWidthMm();
		//compute the horizontal start position, base line and element
		float widthLeft = syllableLeft.text.getFirstParagraph().getWidthMm();
		float startX = syllableLeft.position.xMm + widthLeft / 2 + widthU / 4; //widthU / 4: just some distance
		float baseLine = syllableLeft.position.lp;
		MusicElement element = syllableLeft.musicElement;
		//if end notehead is given, compute the end position
		float endX = 0;
		if (noteheadRight != null)
		{
			endX = noteheadRight.position.xMm;
		}
		//underscore line on a single staff?
		if (noteheadRight != null && syllableLeft.parentStaff == noteheadRight.parentStaff)
		{
			//simple case
			ret = ret.plus(createUnderscoreStamping(startX, endX, baseLine,
				widthU, style, syllableLeft.parentStaff, element));
		}
		else
		{
			It<StaffStamping> staves = new It<StaffStamping>(staffStampings);
			StaffStamping currentStaff = null;
			boolean firstStaffFound = false; //only true, when start stamping is found
			boolean lastStaffFound = false; //only true, when stop stamping is found
			
			//find the start staff
			StaffStamping s1 = syllableLeft.parentStaff;
			while (staves.hasNext())
			{
				currentStaff = staves.next();
				if (currentStaff == s1)
				{
					firstStaffFound = true;
					break;
				}
			}
			//if not found, begin at the very beginning
			if (!firstStaffFound)
			{
				staves = new It<StaffStamping>(staffStampings);
			}
			
			//first staff (if any): begin at the stamping, go to the end of the system
			if (firstStaffFound)
			{
				ret = ret.plus(createUnderscoreStamping(startX, currentStaff.position.x + currentStaff.length,
					baseLine, widthU, style, currentStaff, element));
			}
			
			//create curved lines in the middle staves, from the beginning (without leading spacing)
			//to the end of the system
			while (staves.hasNext())
			{
				currentStaff = staves.next();
				if (noteheadRight != null && currentStaff == noteheadRight.parentStaff)
				{
					//stop, because this is the last staff, where we have the stop notehead
					lastStaffFound = true;
					break;
				}
				//create underscore over whole staff
				ret = ret.plus(createUnderscoreStamping(currentStaff.position.x,
					currentStaff.position.x + currentStaff.length, baseLine, widthU, style, currentStaff, element));
			}
			
			//last staff (if any): begin at the beginning (without leading spacing) of the
			//system, stop at the notehead
			if (lastStaffFound)
			{
				ret = ret.plus(createUnderscoreStamping(currentStaff.position.x,
					noteheadRight.position.xMm, baseLine, widthU, style, currentStaff, element));
			}
			
		}
		return ret;
	}
	
	
	private StaffTextStamping createUnderscoreStamping(float startX, float endX, float baseLine,
		float widthUnderscore, FormattedTextStyle style, StaffStamping staff, MusicElement element)
	{
		//TODO: line does not look continuous in OpenGL.

		//compute number of needed "_"
		int countU = Math.max((int) ((endX - startX) / widthUnderscore) + 1, 1);
		//create text
		FormattedText text = fText(StringUtils.repeat("_", countU), style, Alignment.Left);
		return new StaffTextStamping(staff, (Lyric) element, text, sp(startX, baseLine));
	}
	

}
