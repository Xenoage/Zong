package material.stem.length;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.StaffLines.staff5Lines;

import java.util.List;

import lombok.Getter;
import material.ExampleBase;
import material.Suite;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Stem length example.
 * 
 * @author Andreas Wenger
 */
public class Example
	implements ExampleBase {
	
	public static List<Suite<Example>> all = alist(new ChlapikStemLength(),
		new RossStemLength(), new ZongStemLength());
	
	@Getter public String name;
	public int noteLp;
	public StemDirection stemDir;
	public float stemLengthIs;
	public boolean isLengthenedToMiddleLine = false;
	public final StaffLines staffLines = staff5Lines;
	
	
	public static Example example(String name, int noteLp,
		StemDirection stemDir, double stemLengthIs) {
		Example example = new Example();
		example.name = name;
		example.noteLp = noteLp;
		example.stemDir = stemDir;
		example.stemLengthIs = (float) stemLengthIs;
		return example;
	}
	
	public Example toMiddleLine() {
		isLengthenedToMiddleLine = true;
		return this;
	}

}
