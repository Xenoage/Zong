package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.zong.musiclayout.stampings.FlagsStamping;
import com.xenoage.zong.musiclayout.stampings.FlagsStamping.FlagsDirection;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Renderer for a {@link FlagsStamping}.
 *
 * @author Andreas Wenger
 */
public class FlagsStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link FlagsStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		FlagsStamping s = (FlagsStamping) stamping;

		Symbol symbol = args.symbolPool.getSymbol(CommonSymbol.NoteFlag);
		boolean flagsMirrored = (s.flagsDirection == FlagsDirection.Up);
		float flagsDistance = FlagsStamping.getFlagsDistance(s.flagsDirection, s.scaling);
		//draw all flags
		for (int i : range(s.flagsCount)) {
			StaffSymbolStampingRenderer.drawWith(symbol, null, sp(s.position.xMm, s.position.lp + //TODO: flag position is not correct yet
				flagsDistance * 0.2f /* move a little bit into the stem */+ i * 2 * flagsDistance),
				s.scaling, s.parentStaff, flagsMirrored, canvas, args);
		}
	}

}
