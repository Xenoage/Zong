package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.music.layout.PageBreak;
import com.xenoage.zong.core.music.layout.SystemBreak;


public interface Base52b
	extends Base {

	@Override default String getFileName() {
		return "52b-Breaks.xml";
	}
	
	Break[] expectedBreaks = {
		new Break(null, SystemBreak.NewSystem),
		new Break(PageBreak.NewPage, null),
		null
	};

}
