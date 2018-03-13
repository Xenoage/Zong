package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.Duration;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.Spacings;
import com.xenoage.zong.musiclayout.spacing.RestSpacing;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.TimeStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import lombok.val;

import java.util.HashMap;

import static com.xenoage.zong.core.music.format.SP.sp;

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
		val staff = context.getCurrentStaffStamping();
		Symbol symbol = getRestSymbol(rest.getNotation().duration, context);
		SP sp = sp(xMm, rest.lp);
		return new StaffSymbolStamping(rest.notation, staff, symbol, null, sp, 1, false);
	}

	/**
	 * Creates a stamping for the given clef.
	 */
	public StaffSymbolStamping createClefStamping(ClefNotation clef, float xMm,
			StamperContext context) {
		val staff = context.getCurrentStaffStamping();
		ClefType clefType = clef.element.getType();
		return new StaffSymbolStamping(clef, staff,
			context.getSymbol(CommonSymbol.getClef(clefType.getSymbol())), null,
			sp(xMm, clefType.getLp()), clef.scaling, false);
	}

	/**
	 * Creates a stamping for the given key signature.
	 */
	public KeySignatureStamping createKeyStamping(TraditionalKeyNotation key, float xMm,
			StamperContext context) {
		val staff = context.getCurrentStaffStamping();
		boolean useSharps = key.element.getFifths() > 0;
		Symbol symbol = context.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		Spacings spacings = context.getSettings().spacings;
		float distance = (useSharps ? spacings.widthSharp : spacings.widthFlat);
		return new KeySignatureStamping(key, xMm, staff, symbol, distance);
	}

	/**
	 * Creates a stamping for the given time signature.
	 */
	public Stamping createTimeStamping(TimeNotation time, float xMm, StamperContext context) {
		val staff = context.getCurrentStaffStamping();
		if (time.element.getType() == TimeType.timeCommon) {
			return new StaffSymbolStamping(time, staff, context.getSymbol(CommonSymbol.TimeCommon),
				null, sp(xMm, staff.linesCount - 1), 1f, false);
		}
		else {
			return new TimeStamping(time, xMm, time.numeratorOffset,
				time.denominatorOffset, time.digitGap, staff);
		}
	}
	
	
	private static HashMap<Duration.Type, CommonSymbol> durationSymbolMapping = new HashMap<>();

	static {
		durationSymbolMapping.put(Duration.INSTANCE.Type.Whole, CommonSymbol.RestWhole);
		durationSymbolMapping.put(Duration.INSTANCE.Type.Half, CommonSymbol.RestHalf);
		durationSymbolMapping.put(Duration.INSTANCE.Type.Quarter, CommonSymbol.RestQuarter);
		durationSymbolMapping.put(Duration.INSTANCE.Type.Eighth, CommonSymbol.RestEighth);
		durationSymbolMapping.put(Duration.INSTANCE.Type._16th, CommonSymbol.Rest16th);
		durationSymbolMapping.put(Duration.INSTANCE.Type._32th, CommonSymbol.Rest32th);
		durationSymbolMapping.put(Duration.INSTANCE.Type._64th, CommonSymbol.Rest64th);
		durationSymbolMapping.put(Duration.INSTANCE.Type._128th, CommonSymbol.Rest128th);
		durationSymbolMapping.put(Duration.INSTANCE.Type._256th, CommonSymbol.Rest256th);
	}
	
	private Symbol getRestSymbol(Duration.Type duration, StamperContext context) {
		CommonSymbol cs = durationSymbolMapping.get(duration);
		SymbolPool symbols = context.layouter.symbols;
		if (cs != null)
			return symbols.getSymbol(cs);
		else
			return symbols.getWarningSymbol();
	}

}
