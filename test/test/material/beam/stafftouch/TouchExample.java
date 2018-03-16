package material.beam.stafftouch;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.spacer.beam.placement.SingleStaffBeamPlacer.Placement;
import lombok.Getter;
import material.ExampleBase;
import material.Suite;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.StaffLines.staff5Lines;

/**
 * Example of a beams touching the staff.
 * 
 * @author Andreas Wenger
 */
public class TouchExample
	implements ExampleBase {
	
	public static List<Suite<TouchExample>> all = alist(new ZongStaffTouch());
	
	@Getter public String name;
	public Placement placement;
	public StemDirection stemDir;
	public float beamHeightIs;
	public StaffLines staffLines = Companion.getStaff5Lines();
	public boolean touch = false;
	
	public TouchExample touch() {
		this.touch = true;
		return this;
	}

	public static TouchExample example8th(String name, double leftLp, double rightLp,
		StemDirection stemDir) {
		TouchExample ret = new TouchExample();
		ret.name = name;
		ret.placement = new Placement((float) leftLp, (float) rightLp);
		ret.stemDir = stemDir;
		ret.beamHeightIs = 0.5f;
		return ret;
	}
	
	public static TouchExample example32nd(String name, double leftLp, double rightLp,
		StemDirection stemDir) {
		TouchExample ret = new TouchExample();
		ret.name = name;
		ret.placement = new Placement((float) leftLp, (float) rightLp);
		ret.stemDir = stemDir;
		ret.beamHeightIs = 0.5f + 0.25f + 0.5f + 0.25f + 0.5f; //3 lines, 2 gaps
		return ret;
	}
	
	
	
}
