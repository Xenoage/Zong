package com.xenoage.zong.renderer.stamping;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.zong.musiclayout.stampings.NormalTimeStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Renderer for a {@link NormalTimeStamping}.
 *
 * @author Andreas Wenger
 */
public class NormalTimeStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link NormalTimeStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		NormalTimeStamping s = (NormalTimeStamping) stamping;

		SymbolPool symbolPool = args.symbolPool;
		float interlineSpace = s.parentStaff.is;
		float linesCount = s.parentStaff.linesCount;

		//write numerator digits
		float offsetX = s.numeratorOffset * interlineSpace;
		String num = Integer.toString(s.time.getType().getNumerator());
		for (int i = 0; i < num.length(); i++) {
			int d = num.charAt(i) - '0';
			Symbol symbol = symbolPool.getSymbol(CommonSymbol.getDigit(d));
			if (symbol != null) {
				float symbolWidth = symbol.boundingRect.size.width;
				StaffSymbolStampingRenderer.drawWith(symbol, null,
					sp(s.positionX + offsetX, linesCount + 1), 1, s.parentStaff, false, canvas, args);
				offsetX += (symbolWidth + s.digitGap) * interlineSpace;
			}
		}

		//write denominator digits
		offsetX = s.denominatorOffset * interlineSpace;
		String den = Integer.toString(s.time.getType().getDenominator());
		for (int i = 0; i < den.length(); i++) {
			int d = den.charAt(i) - '0';
			Symbol symbol = symbolPool.getSymbol(CommonSymbol.getDigit(d));
			if (symbol != null) {
				float symbolWidth = symbol.boundingRect.size.width;
				StaffSymbolStampingRenderer.drawWith(symbol, null,
					sp(s.positionX + offsetX, linesCount - 3), 1, s.parentStaff, false, canvas, args);
				offsetX += (symbolWidth + s.digitGap) * interlineSpace;
			}
		}
	}

}
