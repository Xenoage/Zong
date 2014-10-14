package musicxmltestsuite;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.MxlNormalTime;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.MxlWords;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent.MxlDirectionTypeContentType;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * Test the {@link MusicXMLDocument} class for the documents in the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * This class tests the loading of MusicXML files into MusicXML data structures.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLDocumentTestSuiteTest
	extends MusicXMLTestSuiteBase<MusicXMLDocument> {
	
	//the currently tested document
	private MusicXMLDocument doc;
	
	@Override public MusicXMLDocument loadData(String file) {
		try {
			return doc = MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(dir + file)));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + file + ": " + ex.toString());
			return null;
		}
	}

	@Override public void test_21d() {
		super.test_21d();
		List<Tuple2<MP, ? extends Direction>> expectedDirections = get_21d_Directions();
		//check only directions in this test	
		MxlPart part = doc.getScore().getParts().get(0);
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
