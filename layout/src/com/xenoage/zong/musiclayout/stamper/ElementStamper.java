package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.zong.core.music.format.SP.sp;

import java.util.HashMap;

import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.Spacings;
import com.xenoage.zong.musiclayout.spacing.RestSpacing;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.NormalTimeStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Creates the {@link Stamping}s for elements like rests,
 * clefs, keys and time signatures.
 * 
 * @author Andreas Wenger
 */
public class ElementStamper {
	
	public static final ElementStamper elementStamper = new ElementStamper();
	

	/**
	 * Creates a stamping for the given rest.
	 */
	public StaffSymbolStamping createRestStamping(RestSpacing rest, float xMm,
		StamperContext context) {
		Symbol symbol = getRestSymbol(rest.getNotation().duration, context);
		SP sp = sp(xMm, rest.lp);
		return new StaffSymbolStamping(rest.notation, context.staff, symbol, null, sp, 1, false);
	}

	/**
	 * Creates a stamping for the given clef.
	 */
	public StaffSymbolStamping createClefStamping(ClefNotation clef, float xMm,
		StamperContext context) {
		ClefType clefType = clef.element.getType();
		return new StaffSymbolStamping(clef, context.staff,
			context.getSymbol(CommonSymbol.getClef(clefType.getSymbol())), null,
			sp(xMm, clefType.getLp()), clef.scaling, false);
	}

	/**
	 * Creates a stamping for the given key signature.
	 */
	public KeySignatureStamping createKeyStamping(TraditionalKeyNotation key, float xMm,
		StamperContext context) {
		boolean useSharps = key.element.getFifths() > 0;
		Symbol symbol = context.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		Spacings spacings = context.getSettings().spacings;
		float distance = (useSharps ? spacings.widthSharp : spacings.widthFlat);
		return new KeySignatureStamping(key, xMm, context.staff, symbol, distance);
	}

	/**
	 * Creates a stamping for the given time signature.
	 */
	public Stamping createTimeStamping(TimeNotation time, float xMm, StamperContext context) {
		if (time.element.getType() == TimeType.timeCommon) {
			return new StaffSymbolStamping(time, context.staff, context.getSymbol(CommonSymbol.TimeCommon),
				null, sp(xMm, context.staff.linesCount - 1), 1f, false);
		}
		else {
			return new NormalTimeStamping(time, xMm, time.numeratorOffset,
				time.denominatorOffset, time.digitGap, context.staff);
		}
	}
	
	
	private static HashMap<DurationInfo.Type, CommonSymbol> durationSymbolMapping =
		new HashMap<DurationInfo.Type, CommonSymbol>();

	static {
		durationSymbolMapping.put(DurationInfo.Type.Whole, CommonSymbol.RestWhole);
		durationSymbolMapping.put(DurationInfo.Type.Half, CommonSymbol.RestHalf);
		durationSymbolMapping.put(DurationInfo.Type.Quarter, CommonSymbol.RestQuarter);
		durationSymbolMapping.put(DurationInfo.Type.Eighth, CommonSymbol.RestEighth);
		durationSymbolMapping.put(DurationInfo.Type._16th, CommonSymbol.Rest16th);
		durationSymbolMapping.put(DurationInfo.Type._32th, CommonSymbol.Rest32th);
		durationSymbolMapping.put(DurationInfo.Type._64th, CommonSymbol.Rest64th);
		durationSymbolMapping.put(DurationInfo.Type._128th, CommonSymbol.Rest128th);
		durationSymbolMapping.put(DurationInfo.Type._256th, CommonSymbol.Rest256th);
	}
	
	private Symbol getRestSymbol(DurationInfo.Type duration, StamperContext context) {
		CommonSymbol cs = durationSymbolMapping.get(duration);
		SymbolPool symbols = context.layouter.symbols;
		if (cs != null)
			return symbols.getSymbol(cs);
		else
			return symbols.getWarningSymbol();
	}

}
