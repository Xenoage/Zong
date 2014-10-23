package musicxmltestsuite.tests.base;


/**
 * A note can be the end of one slur and the start of a new slur.
 * Also, in MusicXML, nested slurs are possible like in the second measure
 * where one slur goes over all four notes, and another slur goes from
 * the second to the third note. 
 * 
 * @author Andreas Wenger
 */
public interface Base33c
	extends Base {

	@Override default String getFileName() {
		return "33c-Spanners-Slurs.xml";
	}
	
}
