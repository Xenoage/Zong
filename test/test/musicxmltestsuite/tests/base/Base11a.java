package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.time.TimeType;


public interface Base11a
	extends Base {

	@Override default String getFileName() {
		return "11a-TimeSignatures.xml";
	}

	TimeType[] expectedTimes = {
			TimeType.Companion.getTimeAllaBreve(), TimeType.Companion.getTimeCommon(), TimeType.Companion.getTime_2_2(),
		TimeType.Companion.timeType(3, 2), TimeType.Companion.getTime_2_4(), TimeType.Companion.getTime_3_4(), TimeType.Companion.getTime_4_4(),
		TimeType.Companion.timeType(5, 4), TimeType.Companion.timeType(3, 8), TimeType.Companion.getTime_6_8(), TimeType.Companion.timeType(12, 8) };
}
