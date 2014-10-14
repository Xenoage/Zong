package musicxmltestsuite.tests.musicxml;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base13b;

import org.junit.Test;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test13b
	implements Base13b, MusicXmlTest {
	
	@Test public void test() {
		TraditionalKey[] expectedKeys = getExpectedKeys();
		MxlPart part = getFirstPart();
		int iKey = 0;
		for (int i = 0; i <= 2; i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlKey key = attr.getKey();
					assertEquals(expectedKeys[iKey].getFifths(), key.getFifths());
					assertEquals(expectedKeys[iKey].getMode(), getEnumValue(""+key.getMode(), Mode.values()));
					iKey++;
					if (iKey >= expectedKeys.length)
						break;
				}
			}
		}
		assertEquals("not all keys found", expectedKeys.length, iKey);
	}

}
