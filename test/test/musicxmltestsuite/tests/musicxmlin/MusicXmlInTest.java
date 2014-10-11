package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base;

import org.junit.Assert;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;

/**
 * Base class for testing the loading of MusicXML data into
 * core data structures ("musicxml-in" project).
 * 
 * @author Andreas Wenger
 */
public interface MusicXmlInTest
	extends Base {
	
	@Override default String getProjectName() {
		return "musicxml-in";
	}
	
	default Score getScore() {
		Base base = (Base) this;
		try {
			String filepath = base.getDirPath() + base.getFileName();
			return new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(filepath), filepath);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + base.getFileName() + ": " + ex.toString());
			return null;
		}
	}
	
	default Staff getFirstStaff() {
		return getScore().getStaff(0);
	}
	
	default void checkDurations(Staff staff, Fraction[] expectedDurations) {
		int iDuration = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				//check duration
				assertEquals("element " + iDuration, expectedDurations[iDuration++], e.getDuration());
			}
		}
		assertEquals("not all element found", expectedDurations.length, iDuration);
	}

}
