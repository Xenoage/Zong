package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.musicxml.MusicXMLDocument;

/**
 * Test cases for a {@link StavesListReader} class.
 *
 * @author Andreas Wenger
 */
public class StavesListReaderTest {

	/**
	 * Reads and checks the parts and staves of the file "BeetAnGeSample.xml"
	 * from the MusicXML 1.1 sample files.
	 */
	@Test public void testStavesList1() {
		StavesList sl = createStavesList("data/test/scores/musicxml11/BeetAnGeSample.xml");
		if (sl == null)
			return;
		//parts and staves
		assertEquals(2, sl.getParts().size());
		assertEquals(3, sl.getStaves().size());
		assertEquals(0, sl.getPartStaffIndices(sl.getParts().get(0)).getStart());
		assertEquals(0, sl.getPartStaffIndices(sl.getParts().get(0)).getStop());
		assertEquals(1, sl.getPartStaffIndices(sl.getParts().get(1)).getStart());
		assertEquals(2, sl.getPartStaffIndices(sl.getParts().get(1)).getStop());
		//barline groups (implicitly added)
		assertNull(sl.getBarlineGroupByStaff(0));
		assertNotNull(sl.getBarlineGroupByStaff(1));
		assertNotNull(sl.getBarlineGroupByStaff(2));
		assertEquals(1, sl.getBarlineGroupByStaff(1).getStaves().getStart());
		assertEquals(2, sl.getBarlineGroupByStaff(1).getStaves().getStop());
		assertEquals(BarlineGroup.Style.Common, sl.getBarlineGroupByStaff(1).getStyle());
		//bracket groups (implicitly added)
		assertEquals(1, sl.getBracketGroups().size());
		assertEquals(1, sl.getBracketGroups().get(0).getStaves().getStart());
		assertEquals(2, sl.getBracketGroups().get(0).getStaves().getStop());
		assertEquals(BracketGroup.Style.Brace, sl.getBracketGroups().get(0).getStyle());
	}

	/**
	 * Reads and checks the parts and staves of the file "ActorPreludeSample.xml"
	 * from the MusicXML 1.1 sample files.
	 */
	@Test public void testStavesList2() {
		String file = "data/test/scores/musicxml11/ActorPreludeSample.xml";
		//proprietary file. if not there, ignore test
		if (false == jsePlatformUtils().getFilesystemInput().existsFile(file))
			return;
		StavesList sl = createStavesList(file);
		if (sl == null)
			return;
		assertEquals(22, sl.getParts().size());
		assertEquals(23, sl.getStaves().size());
		//barline groups
		//(5, as can be seen on the first page of the PDF)
		assertEquals(5, sl.getBarlineGroups().size());
		for (int i = 0; i < 23; i++) {
			assertNotNull("failed at " + i, sl.getBarlineGroupByStaff(i));
			assertEquals("failed at " + i, BarlineGroup.Style.Common, sl.getBarlineGroupByStaff(i).getStyle());
		}
		//bracket groups
		//(8, as can be seen on the first page of the PDF)
		assertEquals(8, sl.getBracketGroups().size());
	}

	private StavesList createStavesList(String filePath) {
		MusicXMLDocument doc = null;
		try {
			doc = MusicXMLDocument.read(platformUtils().createXmlReader(
				jsePlatformUtils().openFile(filePath)));
		} catch (FileNotFoundException ex) {
			//file not there. ignore, since copyrighted file. - TODO: ask Michael Good for file license
			return null;
		} catch (Exception ex) {
			fail(ex.toString());
		}
		StavesListReader.Value sl = null;
		try {
			sl = StavesListReader.read(doc.getScore());
		} catch (Exception ex) {
			fail(ex.toString());
		}
		return sl.stavesList;
	}

}
