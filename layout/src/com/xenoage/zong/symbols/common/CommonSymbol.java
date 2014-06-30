package com.xenoage.zong.symbols.common;

import java.util.ArrayList;

import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Enumeration of commonly used symbols, that
 * is used to get symbols in a very fast way
 * by calling {@link SymbolPool#getSymbol(CommonSymbol)}.
 * 
 * @author Andreas Wenger
 */
public enum CommonSymbol {
	AccidentalDoubleflat("accidental-doubleflat"),
	AccidentalDoublesharp("accidental-doublesharp"),
	AccidentalFlat("accidental-flat"),
	AccidentalNatural("accidental-natural"),
	AccidentalSharp("accidental-sharp"),
	ArticulationAccent("articulation-accent"),
	ArticulationStaccato("articulation-staccato"),
	ArticulationStaccatissimo("articulation-staccatissimo"),
	ArticulationStrongAccent("articulation-strongaccent"), //TODO: rename to "Marcato"
	ArticulationTenuto("articulation-tenuto"),
	BracketBrace("bracket-brace"),
	BracketBracketLine("bracket-bracketline"),
	BracketBracketEnd("bracket-bracketend"),
	ClefG("clef-g"),
	ClefF("clef-f"),
	Digit0("digit-0"),
	Digit1("digit-1"),
	Digit2("digit-2"),
	Digit3("digit-3"),
	Digit4("digit-4"),
	Digit5("digit-5"),
	Digit6("digit-6"),
	Digit7("digit-7"),
	Digit8("digit-8"),
	Digit9("digit-9"),
	DynamicsF("dynamics-f"),
	DynamicsM("dynamics-m"),
	DynamicsP("dynamics-p"),
	DynamicsS("dynamics-s"),
	DynamicsZ("dynamics-z"),
	NoteDot("note-dot"),
	NoteFlag("note-flag"),
	NoteHalf("note-half"),
	NoteQuarter("note-quarter"),
	NoteWhole("note-whole"),
	PedalSustainDown1("pedal-sustain-down-1"),
	PedalSustainUp("pedal-sustain-up"),
	Rest16th("rest-16th"),
	Rest32th("rest-32th"),
	Rest64th("rest-64th"),
	Rest128th("rest-128th"),
	Rest256th("rest-256th"),
	RestEighth("rest-eighth"),
	RestHalf("rest-half"),
	RestQuarter("rest-quarter"),
	RestWhole("rest-whole"),
	TextNoteHalf("text-note-half"),
	TextNoteQuarter("text-note-quarter"),
	TimeCommon("time-common");

	private String id;


	/**
	 * Creates a common symbol with the given id.
	 */
	private CommonSymbol(String id) {
		this.id = id;
	}

	/**
	 * Gets the ID of this symbol.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the {@link CommonSymbol} for the given digit
	 * between 0 and 9.
	 */
	public static CommonSymbol getDigit(int digit) {
		switch (digit) {
			case 0:
				return Digit0;
			case 1:
				return Digit1;
			case 2:
				return Digit2;
			case 3:
				return Digit3;
			case 4:
				return Digit4;
			case 5:
				return Digit5;
			case 6:
				return Digit6;
			case 7:
				return Digit7;
			case 8:
				return Digit8;
			case 9:
				return Digit9;
			default:
				throw new IllegalArgumentException("digit must be a number between 0 and 9.");
		}
	}

	/**
	 * Gets the common symbol for the given articulation type.
	 */
	public static CommonSymbol getArticulation(ArticulationType articulation) {
		switch (articulation) {
			case Accent:
				return ArticulationAccent;
			case Staccato:
				return ArticulationStaccato;
			case Staccatissimo:
				return ArticulationStaccatissimo;
			case Marcato:
				return ArticulationStrongAccent;
			case Tenuto:
				return ArticulationTenuto;
			default:
				throw new IllegalArgumentException("unsupported articulation");
		}
	}

	/**
	 * Gets the common symbol for the given {@link Accidental} type.
	 */
	public static CommonSymbol getAccidental(Accidental accidental) {
		switch (accidental) {
			case DoubleFlat:
				return CommonSymbol.AccidentalDoubleflat;
			case Flat:
				return CommonSymbol.AccidentalFlat;
			case Sharp:
				return CommonSymbol.AccidentalSharp;
			case DoubleSharp:
				return CommonSymbol.AccidentalDoublesharp;
			default:
				return CommonSymbol.AccidentalNatural;
		}
	}

	/**
	 * Gets the common symbol for the given {@link ClefSymbol}.
	 */
	public static CommonSymbol getClef(ClefSymbol clefSymbol) {
		switch (clefSymbol) {
			case F:
				return CommonSymbol.ClefF;
			case G:
				return CommonSymbol.ClefG;
			default:
				return CommonSymbol.ClefG; //TODO: add more clefs
		}
	}

	/**
	 * Gets the symbols for the given {@link DynamicsType}.
	 * TODO: memoize (one-time-computation)
	 */
	public static ArrayList<CommonSymbol> getDynamics(DynamicsType dynamicsType) {
		//collect symbols from the name, e.g. "p" is DynamicsP.
		String name = dynamicsType.name();
		ArrayList<CommonSymbol> ret = new ArrayList<CommonSymbol>(name.length());
		for (int i = 0; i < name.length(); i++) {
			//TODO: symbol for "r"
			switch (name.charAt(i)) {
				case 'f':
					ret.add(CommonSymbol.DynamicsF);
					break;
				case 'm':
					ret.add(CommonSymbol.DynamicsM);
					break;
				case 'p':
					ret.add(CommonSymbol.DynamicsP);
					break;
				case 's':
					ret.add(CommonSymbol.DynamicsS);
					break;
				case 'z':
					ret.add(CommonSymbol.DynamicsZ);
					break;
			}
		}
		return ret;
	}

	/**
	 * Gets the symbol for the given {@link Pedal} type.
	 */
	public static CommonSymbol getPedal(Pedal.Type pedalType) {
		switch (pedalType) {
			case Start:
				return CommonSymbol.PedalSustainDown1;
			case Stop:
				return CommonSymbol.PedalSustainUp;
			default:
				return null;
		}
	}

}
