package musicxmltestsuite.tests.musicxml;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.ToDo;
import musicxmltestsuite.tests.base.Base13a;

import org.junit.Test;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test13a
	implements Base13a, MusicXmlTest {
	
	@ToDo("Zong! supports only -7 to +7, starting in measure 9, ending in measure 38")
	
	@Test public void test() {
		MxlPart part = getFirstPart();
		TraditionalKey[] expectedKeys = getExpectedKeys();
		int iKey = 0;
		for (int i = 8; i <= 37; i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlKey key = attr.getKey();
					assertEquals(expectedKeys[iKey].getFifths(), key.getFifths());
					assertEquals(expectedKeys[iKey].getMode(), getEnumValue(""+key.getMode(), Mode.values()));
					iKey++;
				}
			}
		}
	}

}
