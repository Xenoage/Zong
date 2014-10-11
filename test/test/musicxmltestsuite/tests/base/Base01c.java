package musicxmltestsuite.tests.base;

/**
 * The {@literal <voice>} element of notes is optional in MusicXML (although Dolet always writes it out).
 * Here, there is one note with lyrics, but without a voice assigned.
 * It should still be correctly converted.  
 * 
 * @author Andreas Wenger
 */
public interface Base01c
	extends Base {

	@Override default String getFileName() {
		return "01c-Pitches-NoVoiceElement.xml";
	}

}
