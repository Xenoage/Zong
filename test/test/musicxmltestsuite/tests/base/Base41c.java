package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;


/**
 * A huge orchestra score with 28 parts and different kinds of nested bracketed groups.
 * Each part/group is assigned a name and an abbreviation to be shown before the staff.
 * Also, most of the groups show unbroken barlines, while the barlines are broken between the groups. 
 * 
 * @author Andreas Wenger
 */
public interface Base41c
	extends Base {

	@Override default String getFileName() {
		return "41c-StaffGroups.xml";
	}
	
	String[] expectedPartNames = {
		"Piccolo", "Flute 1", "Flute 2", "Oboe", "English Horn", "Clarinet in Eb",
		"Clarinet in Bb 1", "Clarinet in Bb 2", "Bass Clarinet", "Bassoon 1",
		"Bassoon 2", "Contrabassoon", "Horn in F 1", "Horn in F 2",
		"Trumpet in C 1", "Trumpet in C 2", "Trombone 1", "Trombone 2", "Tuba",
		"Timpani", "Percussion", "Harp", "Piano",
		"Violin I", "Violin II", "Viola", "Cello", "Contrabass",
	};
	
	BracketGroup[] expectedBracketGroups = {
		new BracketGroup(new StavesRange(0, 11), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(1, 2), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(3, 5), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(3, 4), BracketGroup.Style.Line),
		new BracketGroup(new StavesRange(6, 7), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(9, 10), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(12, 13), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(12, 18), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(14, 15), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(16, 17), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(25, 29), BracketGroup.Style.Bracket),
		//implicit groups:
		new BracketGroup(new StavesRange(21, 22), BracketGroup.Style.Brace), //harp
		new BracketGroup(new StavesRange(23, 24), BracketGroup.Style.Brace), //piano
	};
	
	BarlineGroup[] expectedBarlineGroups = {
		new BarlineGroup(new StavesRange(0, 11), BarlineGroup.Style.Common),
		//new BarlineGroup(new StavesRange(3, 5), BarlineGroup.Style.Common), //found in MusicXML, but we remove it: implicitly contained in [0,11]
		//new BarlineGroup(new StavesRange(3, 4), BarlineGroup.Style.Common), //found in MusicXML, but we remove it: implicitly contained in [0,11]
		new BarlineGroup(new StavesRange(12, 18), BarlineGroup.Style.Common),
		new BarlineGroup(new StavesRange(25, 29), BarlineGroup.Style.Common),
		//implicit groups:
		new BarlineGroup(new StavesRange(21, 22), BarlineGroup.Style.Common), //harp
		new BarlineGroup(new StavesRange(23, 24), BarlineGroup.Style.Common), //piano
	};
	
}
