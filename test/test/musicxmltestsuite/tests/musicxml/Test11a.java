package musicxmltestsuite.tests.musicxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import musicxmltestsuite.tests.base.Base11a;

import org.junit.Test;

import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlNormalTime;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test11a
	implements Base11a, MusicXmlTest {
	
	@Test public void test() {
		MxlPart part = getFirstPart();
		int iTime = 0;
		for (int i = 0; i < part.getMeasures().size(); i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlNormalTime mxlTime = (MxlNormalTime) attr.getTime().getContent();
					TimeType expectedTime = expectedTimes[iTime++];
					assertEquals("time " + iTime, expectedTime.getNumerator(), mxlTime.getBeats());
					assertEquals("time " + iTime, expectedTime.getDenominator(), mxlTime.getBeatType());
					if (i == 0)
						assertEquals("time " + iTime, MxlTimeSymbol.Common, attr.getTime().getSymbol()); //TODO: bug in MusicXML file, should be "Cut"
					else if (i == 1)
						assertEquals("time " + iTime, MxlTimeSymbol.Common, attr.getTime().getSymbol());
					else
						assertNull("time " + iTime, attr.getTime().getSymbol()); //= Normal
					break; //no more time signature in this measure
				}
			}
		}
		assertEquals("not all times found", expectedTimes.length, iTime);
	}

}
