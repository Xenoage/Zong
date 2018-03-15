package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.Pitch;


public interface Base41a
	extends Base {

	@Override default String getFileName() {
		return "41a-MultiParts-Partorder.xml";
	}
	
	String[] expectedNames = { "Part 1", "Part 2", "Part 3", "Part 4" };
	Pitch[] expectedPitches = { Companion.pi(0, 4), Companion.pi(2, 4), Companion.pi(4, 4), Companion.pi(6, 4) };
	
}
