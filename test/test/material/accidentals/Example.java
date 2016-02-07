package material.accidentals;

import static com.xenoage.utils.collections.ArrayUtils.toIntArray;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import material.ExampleBase;

/**
 * Example of a chord with accidentals.
 * Can be used to test {@link AccidentalsNotation}s.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor(staticName="example") @Getter
public class Example
	implements ExampleBase {
	
	String name;
	
	//input
	List<Pitch> pitches;
	NoteDisplacement[] notes;
	MusicContext context;
	
	//expected results
	float expectedAccsWidthIs;
	float[] expectedAccsXIs; //or null, when not defined
	
	public int[] getExpectedAccsLp() {
		List<Integer> lps = alist();
		for (Pitch pitch : pitches)
			if (context.getAccidental(pitch) != null)
				lps.add(context.getLp(pitch));
		return toIntArray(lps);
	}
	
	public int getAccsCount() {
		return getExpectedAccsLp().length;
	}
	
}
