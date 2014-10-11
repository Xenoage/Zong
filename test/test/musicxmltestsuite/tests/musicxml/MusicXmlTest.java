package musicxmltestsuite.tests.musicxml;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import musicxmltestsuite.tests.base.Base;

import org.junit.Assert;

import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * Base class for testing the loading of MusicXML files into
 * MusicXML data structures ("musicxml" project).
 * 
 * @author Andreas Wenger
 */
public interface MusicXmlTest
	extends Base {
	
	@Override default String getProjectName() {
		return "musicxml";
	}
	
	default MusicXMLDocument getDoc() {
		Base base = (Base) this;
		try {
			return MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(base.getDirPath() + base.getFileName())));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + base.getFileName() + ": " + ex.toString());
			return null;
		}
	}
	
	default MxlScorePartwise getScore() {
		return getDoc().getScore();
	}
	
	default MxlPart getFirstPart() {
		return getDoc().getScore().getParts().get(0);
	}
	
	default MxlMeasure getFirstMeasure() {
		return getDoc().getScore().getParts().get(0).getMeasures().get(0);
	}

}
