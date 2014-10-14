package musicxmltestsuite.tests.base;

/**
 * Senza-misura time signature.
 * 
 * @author Andreas Wenger
 */
public interface Base11h
	extends Base {

	@Override default String getFileName() {
		return "11h-TimeSignatures-SenzaMisura.xml";
	}

}
