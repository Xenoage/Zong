package com.xenoage.zong.renderer.stamping;

import com.xenoage.zong.musiclayout.stampings.TimeStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

import static com.xenoage.zong.core.music.format.SP.sp;

/**
 * Renderer for a {@link TimeStamping}.
 *
 * @author Andreas Wenger
 */
public class TimeRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link TimeStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		TimeStamping s = (TimeStamping) stamping;

		SymbolPool symbolPool = args.symbolPool;
		float interlineSpace = s.parentStaff.is;
		float linesCount = s.parentStaff.linesCount;

		//write numerator digits
		float offsetX = s.numeratorOffsetIs * interlineSpace;
		String num = Integer.toString(s.time.element.getType().getNumerator());
		for (int i = 0; i < num.length(); i++) {
			int d = num.charAt(i) - '0';
			Symbol symbol = symbolPool.getSymbol(CommonSymbol.getDigit(d));
			if (symbol != null) {
				float symbolWidth = symbol.boundingRect.size.width;
				StaffSymbolRenderer.drawWith(symbol, null,
					sp(s.xMm + offsetX, linesCount + 1), 1, s.parentStaff, false, canvas, args);
				offsetX += (symbolWidth + s.digitGapIs) * interlineSpace;
			}
		}

		//write denominator digits
		offsetX = s.denominatorOffsetIs * interlineSpace;
		String den = Integer.toString(s.time.element.getType().getDenominator());
		for (int i = 0; i < den.length(); i++) {
			int d = den.charAt(i) - '0';
			Symbol symbol = symbolPool.getSymbol(CommonSymbol.getDigit(d));
			if (symbol != null) {
				float symbolWidth = symbol.boundingRect.size.width;
				StaffSymbolRenderer.drawWith(symbol, null,
					sp(s.xMm + offsetX, linesCount - 3), 1, s.parentStaff, false, canvas, args);
				offsetX += (symbolWidth + s.digitGapIs) * interlineSpace;
			}
		}
	}

}
