package com.xenoage.zong.renderer.stamping;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

import static com.xenoage.zong.core.music.format.SP.sp;

/**
 * Renderer for a {@link KeySignatureStamping}.
 *
 * @author Andreas Wenger
 */
public class KeySignatureRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link KeySignatureStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		KeySignatureStamping s = (KeySignatureStamping) stamping;

		int fifths = s.key.element.getFifths();
		if (fifths == 0)
			return;
		boolean useSharps = (fifths > 0);
		float distance = s.distanceMm;
		Symbol symbol = args.symbolPool.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		//paint sharps/flats
		fifths = Math.abs(fifths);
		float interlineSpace = s.parentStaff.is;
		for (int i = 0; i < fifths; i++) {
			int linePosition = TraditionalKey.Companion.getLinePosition(i, useSharps, s.key.c4Lp,
				s.key.minLp);
			StaffSymbolRenderer.drawWith(symbol, null,
				sp(s.xMm + i * distance * interlineSpace, linePosition), 1, s.parentStaff, false,
				canvas, args);
		}
	}

}
