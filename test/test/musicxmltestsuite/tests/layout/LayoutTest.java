package musicxmltestsuite.tests.layout;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.zong.musiclayout.layouter.Target.targetA4;

import musicxmltestsuite.tests.base.Base;
import musicxmltestsuite.tests.musicxmlin.MusicXmlInTest;

import org.junit.Assert;

import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.layouter.Target;

/**
 * Base class for testing the layout of the MusicXML test files
 * ("layout" project).
 * 
 * @author Andreas Wenger
 */
public interface LayoutTest
	extends MusicXmlInTest {
	
	@Override default String getProjectName() {
		return "layout";
	}
	
	default ScoreFrameLayout getScoreFrameLayout() {
		try {
			return getScoreFrameLayoutOrException();
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + ((Base) this).getFileName() + ": " + ex.toString());
			return null;
		}
	}
	
	default ScoreFrameLayout getScoreFrameLayoutOrException()
		throws Exception {
		Base base = (Base) this;
		String filepath = Base.dirPath + base.getFileName();
		ScoreDoc doc = new MusicXmlScoreDocFileInput().read(jsePlatformUtils().openFile(filepath), filepath);
		ScoreLayout layout = new ScoreLayouter(doc, targetA4()).createScoreLayout();
		return layout.getScoreFrameLayout(0);
	}
	
}
