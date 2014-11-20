package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.jse.async.Sync.sync;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import musicxmltestsuite.tests.base.Base;
import musicxmltestsuite.tests.base.Base90a;

import org.junit.Test;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musicxml.in.MusicXmlFileReader;

public class Test90a
	implements Base90a, MusicXmlInTest {

	@Test public void testOpenFile() {
		String filepath = Base.dirPath + getFileName();
		try {
			List<Score> scores = sync(new MusicXmlFileReader(io().openFile(filepath), filepath,
				l -> CollectionUtils.alist("20a-Compressed-MusicXML.xml")));
			assertEquals(1, scores.size());
			assertEquals("Compressed MusicXML file", scores.get(0).getInfo().getMovementTitle());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}
