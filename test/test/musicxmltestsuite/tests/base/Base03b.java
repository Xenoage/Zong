package musicxmltestsuite.tests.base;

/**
 * Two voices with a backup, that does not jump to the beginning for the measure for voice 2,
 * but somewhere in the middle. Voice 2 thus won’t have any notes or rests for the first beat
 * of the measures.  
 * 
 * @author Andreas Wenger
 */
public interface Base03b
	extends Base {

	@Override default String getFileName() {
		return "03b-Rhythm-Backup.xml";
	}

}
