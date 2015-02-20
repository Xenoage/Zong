package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.zong.core.music.format.SP.sp;

import java.util.HashMap;

import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.NormalTimeStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffSymbolStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Strategy to create stampings for elements like rests,
 * clefs, keys and time signatures.
 * 
 * @author Andreas Wenger
 */
public class MusicElementStampingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Creates a stamping for the given rest.
	 */
	public StaffSymbolStamping createRestStamping(RestNotation rest, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		DurationInfo.Type duration = DurationInfo.getRestType(rest.element.getDuration());
		Symbol symbol = getRestSymbol(duration, symbolPool);
		SP sp = sp(positionX, getRestLp(staff, duration));
		return new StaffSymbolStamping(rest, staff, symbol, null, sp, 1, false);
	}

	/**
	 * Creates a stamping for the given clef.
	 */
	public StaffSymbolStamping createClefStamping(ClefNotation clef, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		return new StaffSymbolStamping(clef, staff,
			symbolPool.getSymbol(CommonSymbol.getClef(clef.element.getType().getSymbol())), null,
			sp(positionX, clef.element.getType().getLp()), clef.scaling, false);
	}

	/**
	 * Creates a stamping for the given key signature.
	 */
	public KeySignatureStamping createKeyStamping(TraditionalKeyNotation key, float positionX,
		StaffStamping staff, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		boolean useSharps = key.element.getFifths() > 0;
		Symbol symbol = symbolPool.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		float distance = (useSharps ? layoutSettings.spacings.widthSharp
			: layoutSettings.spacings.widthFlat);
		return new KeySignatureStamping(key, positionX, staff, symbol, distance);
	}

	/**
	 * Creates a stamping for the given time signature.
	 */
	public Stamping createTimeStamping(TimeNotation time, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		if (time.element.getType() == TimeType.timeCommon) {
			return new StaffSymbolStamping(time, staff, symbolPool.getSymbol(CommonSymbol.TimeCommon),
				null, sp(positionX, staff.linesCount - 1), 1f, false);
		}
		else {
			return new NormalTimeStamping(time, positionX, time.numeratorOffset,
				time.denominatorOffset, time.digitGap, staff);
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
	
	private Symbol getRestSymbol(DurationInfo.Type duration, SymbolPool symbolPool) {
		CommonSymbol cs = durationSymbolMapping.get(duration);
		if (cs != null)
			return symbolPool.getSymbol(cs);
		else
			return symbolPool.getWarningSymbol();
	}

	/**
	 * The quarter rest is centered around the middle
	 * line of the staff, the half rest sits on the
	 * middle line and the whole rest hangs on the
	 * line over the middle staff.
	 */
	private int getRestLp(StaffStamping staff, DurationInfo.Type duration) {
		if (duration == DurationInfo.Type.Whole) {
			//whole rest hangs on the line above the middle
			return staff.linesCount + 1;
		}
		else if (duration == DurationInfo.Type.Half) {
			//half rest sits on the line under the middle
			return staff.linesCount - 1;
		}
		else {
			//all other rests are centered on the middle line
			return staff.linesCount - 1;
		}
	}

}
