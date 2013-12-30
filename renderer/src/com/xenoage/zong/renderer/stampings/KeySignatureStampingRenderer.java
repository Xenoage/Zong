package com.xenoage.zong.renderer.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Renderer for a {@link KeySignatureStamping}.
 *
 * @author Andreas Wenger
 */
public class KeySignatureStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link KeySignatureStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		KeySignatureStamping s = (KeySignatureStamping) stamping;

		int fifth = s.traditionalKey.getFifth();
		if (fifth == 0)
			return;
		boolean useSharps = (fifth > 0);
		float distance = (useSharps ? s.layoutSettings.spacings.widthSharp
			: s.layoutSettings.spacings.widthFlat);
		Symbol symbol = args.symbolPool.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		//paint sharps/flats
		fifth = Math.abs(fifth);
		float interlineSpace = s.parentStaff.is;
		for (int i = 0; i < fifth; i++) {
			int linePosition = TraditionalKey.getLinePosition(i, useSharps, s.linePositionC4,
				s.linePositionMin);
			StaffSymbolStampingRenderer.drawWith(symbol, null,
				sp(s.positionX + i * distance * interlineSpace, linePosition), 1, s.parentStaff, false,
				canvas, args);
		}
	}

}
