package com.xenoage.zong.musiclayout.text;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.text.FormattedText.fText;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.core.text.Text;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * This class provides text content for {@link TextElement}s which
 * have a null content.
 * 
 * @author Andreas Wenger
 */
public class DefaultTexts {

	public static Text getTextNotNull(TextElement element, SymbolPool symbolPool) {
		if (element.getText() != null)
			return element.getText();
		else if (element instanceof Tempo)
			return getTempoTextNotNull((Tempo) element, symbolPool);
		else
			return ut("???");
	}

	public static FormattedText getTempoTextNotNull(Tempo tempo, SymbolPool symbolPool) {
		FormattedTextStyle style = FormattedTextStyle.defaultStyle;
		if (tempo.getText() != null) {
			//use custom text
			return styleText(tempo.getText(), style);
		}
		else {
			//show meaning, e.g. "♩ = 120"
			CList<FormattedTextElement> elements = clist();
			Fraction beat = tempo.getBaseBeat();
			if (beat.equals(fr(1, 4))) {
				elements.add(new FormattedTextSymbol(symbolPool.getSymbol(CommonSymbol.TextNoteQuarter),
				/* TODO staffStamping.is * FONT_SIZE_IN_IS */12, FormattedTextStyle.defaultColor));
			}
			else if (beat.equals(fr(1, 2))) {
				elements.add(new FormattedTextSymbol(symbolPool.getSymbol(CommonSymbol.TextNoteHalf),
				/* staffStamping.is * FONT_SIZE_IN_IS */12, FormattedTextStyle.defaultColor));
			}
			else {
				elements.add(new FormattedTextString(beat.toString(), style));
			}
			elements.add(new FormattedTextString(" = " + tempo.getBeatsPerMinute(), style));
			FormattedTextParagraph paragraph = new FormattedTextParagraph(elements, Alignment.Left);
			return fText(paragraph);
		}
	}

}
