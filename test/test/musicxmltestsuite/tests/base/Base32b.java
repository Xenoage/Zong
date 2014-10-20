package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.color.ColorUtils.getColor;
import static com.xenoage.utils.font.FontStyle.Bold;
import static com.xenoage.utils.font.FontStyle.fontStyle;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import musicxmltestsuite.ToDo;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.position.MP;

/**
 * Text markup: different font sizes, weights and colors.  
 * 
 * @author Andreas Wenger
 */
@ToDo("text styling")
public interface Base32b
	extends Base {

	@Override default String getFileName() {
		return "32b-Articulations-Texts.xml";
	}
	
	@Data @AllArgsConstructor public class Text {
		String text;
		float size;
		FontStyle style;
		Color color;
		Placement placement;
	}
	
	List<Tuple2<MP, Text>> expectedTexts = getExpectedTexts();
	
	static List<Tuple2<MP, Text>> getExpectedTexts() {
		List<Tuple2<MP, Text>> ret = alist();
		int small = 2;
		int medium = 3;
		int large = 4;
		//font sizes are arbitrary, but large must be bigger than medium and so on
		ret.add(t(atBeat(0, 0, 0, fr(0, 8)),
			new Text("Normal, Medium", medium, FontStyle.normal, Color.black, Placement.Above)));
		ret.add(t(atBeat(0, 0, 0, fr(1, 1)), //TODO: wrong duration in MusicXML file, must be 48
			new Text("Bold, Medium", medium, fontStyle(Bold), Color.black, Placement.Below)));
		ret.add(t(atBeat(0, 1, 0, fr(0, 4)),
			new Text("Normal, Large", large, FontStyle.normal, Color.black, Placement.Above)));
		ret.add(t(atBeat(0, 1, 0, fr(4, 4)),
			new Text("Bold, Large", large, fontStyle(Bold), Color.black, Placement.Below)));
		ret.add(t(atBeat(0, 2, 0, fr(0, 4)),
			new Text("Normal, Small", small, FontStyle.normal, Color.black, Placement.Above)));
		ret.add(t(atBeat(0, 2, 0, fr(4, 4)),
			new Text("Bold, Small", small, fontStyle(Bold), Color.black, Placement.Below)));
		ret.add(t(atBeat(0, 2, 0, fr(4, 4)),
			new Text("Normal, Small, Colored, Below", small, FontStyle.normal, getColor("#FF8000"), Placement.Below)));
		return ret;
	}

}
