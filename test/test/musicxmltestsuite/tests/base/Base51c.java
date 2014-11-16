package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.info.Rights;

public interface Base51c
	extends Base {

	@Override default String getFileName() {
		return "51c-MultipleRights.xml";
	}

	Rights[] expectedRights = { new Rights("Copyright © XXXX by Y. ZZZZ.", null),
		new Rights("Released To The Public Domain.", null)};

}
