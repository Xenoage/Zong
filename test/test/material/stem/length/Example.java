package material.stem.length;

import lombok.Getter;
import material.ExampleBase;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Stem length example.
 * 
 * @author Andreas Wenger
 */
public class Example
	implements ExampleBase {
	
	@Getter public String name;
	public int noteLp;
	public StemDirection stemDir;
	public float stemLengthIs;
	public boolean isLengthenedToMiddleLine = false;
	
	
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
