package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import musicxmltestsuite.tests.base.Base32b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;


public class Test32b
	implements Base32b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		for (int i = 0; i < expectedTexts.size(); ) {
			MP mp = expectedTexts.get(i).get1();
			List<Direction> directions = score.getMeasure(mp).getDirections().getAll(mp.beat);
			assertTrue(""+mp, directions.size() > 0);
			for (int d = 0; d < directions.size(); d++) {
				assertTrue(""+mp, directions.get(d) instanceof Words);
				check((Words) directions.get(d), expectedTexts.get(i).get2(), mp);
				i++;
			}
		}
	}
	
	private void check(Words words, Text expectedText, MP mp) {
		FormattedTextElement elem = getFormattedTextElement(words, mp);
		assertEquals(""+mp, expectedText.getText(), elem.getText());
		assertEquals(""+mp, expectedText.getStyle(), elem.getStyle().getFont().getStyle());
		assertEquals(""+mp, expectedText.getColor(), elem.getStyle().getColor());
		//TODO: wrong position coordinates in MusicXML file. for example, placement says "below"
		//but default-y is positive. and default-x should be relative to the measure start
		//assertEquals(""+mp, expectedText.getPlacement(), words.getPositioning());
	}
	
	private FormattedTextElement getFormattedTextElement(Words words, MP mp) {
		assertTrue(""+mp, words.getText() instanceof FormattedText);
		FormattedText wordsText = (FormattedText) words.getText();
		assertEquals(""+mp, 1, wordsText.getParagraphs().size());
		FormattedTextParagraph wordsPara = wordsText.getFirstParagraph();
		assertEquals(""+mp, 1, wordsPara.getElements().size());
		FormattedTextElement wordsElem = wordsPara.getElements().getFirst();
		return wordsElem;
	}

}
