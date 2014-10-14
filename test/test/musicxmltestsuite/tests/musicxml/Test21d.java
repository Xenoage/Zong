package musicxmltestsuite.tests.musicxml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base21d;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlWords;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent.MxlDirectionTypeContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test21d
	implements Base21d, MusicXmlTest {
	
	@Test public void test() {
		MxlPart part = getFirstPart();
		List<Tuple2<MP, ? extends Direction>> expectedDirections = getExpectedDirections();
		//check only directions in this test	
		int iDirection = 0;
		for (int iMeasure = 0; iMeasure <= 1; iMeasure++) {
			MxlMeasure measure = part.getMeasures().get(iMeasure);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Direction) {
					//check type
					MxlDirection dir = (MxlDirection) data;
					MxlDirectionTypeContent content = dir.getDirectionTypes().get(0).getContent();
					if (iDirection == 0) {
						//Words "Largo"
						assertEquals(0, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Words, content.getDirectionTypeContentType());
						assertEquals("Largo", ((MxlWords) content).getFormattedText().getValue());
					}
					else if (iDirection == 1) {
						//Dynamics "fp"
						assertEquals(0, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Dynamics, content.getDirectionTypeContentType());
						assertEquals(DynamicsType.fp, ((MxlDynamics) content).getElement());
					}
					else if (iDirection == 2) {
						//Dynamics "p"
						assertEquals(1, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Dynamics, content.getDirectionTypeContentType());
						assertEquals(DynamicsType.p, ((MxlDynamics) content).getElement());
					}
					iDirection++;
				}
			}
		}
		assertEquals("not all directions found", expectedDirections.size(), iDirection);
	}

}
