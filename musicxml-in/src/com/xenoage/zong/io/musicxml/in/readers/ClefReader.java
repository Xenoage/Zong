package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.musicxml.types.MxlClef;

/**
 * This class reads a {@link ClefType} from a {@link MxlClef}.
 * 
 * @author Andreas Wenger
 */
public class ClefReader {
	
	/**
	 * Reads the given clef, or returns null if the clef is unsupported.
	 */
	public static ClefType readClef(MxlClef clef) {
		//read symbol
		ClefSymbol symbol = null;
		switch (clef.getSign()) {
			case G: symbol = ClefSymbol.G; break;
			case F: symbol = ClefSymbol.F; break;
			case C: symbol = ClefSymbol.C; break;
			case Percussion: symbol = ClefSymbol.PercTwoRects; break;
			case TAB: symbol = ClefSymbol.Tab; break;
			case None: symbol = ClefSymbol.None; break;
			default: return null; //unsupported
		}
		//for some clefs, we provide octave shifting. If unsupported, we ignore it
		int octaveChange = clef.getClefOctaveChange();
		if (octaveChange != 0) {
			if (symbol == ClefSymbol.G) {
				switch (octaveChange) {
					case -2: symbol = ClefSymbol.G15vb; break;
					case -1: symbol = ClefSymbol.G8vb; break;
					case 1: symbol = ClefSymbol.G8va; break;
					case 2: symbol = ClefSymbol.G15va; break;
				}
			}
			else if (symbol == ClefSymbol.F) {
				switch (octaveChange) {
					case -2: symbol = ClefSymbol.F15vb; break;
					case -1: symbol = ClefSymbol.F8vb; break;
					case 1: symbol = ClefSymbol.F8va; break;
					case 2: symbol = ClefSymbol.F15va; break;
				}
			}
		}
		//read line or use default position
		Integer line = clef.getLine();
		if (line == null) {
			return ClefType.commonClefs.get(symbol);
		}
		else {
			//convert MusicXML line number (1 based) to LP (0 based)
			int lp = (line - 1) * 2;
			//special case: in MusicXML, the TAB symbol is based on the top line, but
			//in Zong!, it is centered, so move it 2 interline spaces down (= 4 LPs)
			if (symbol == ClefSymbol.Tab || symbol == ClefSymbol.TabSmall)
				lp -= 4;
			return new ClefType(symbol, lp);
		}
	}

}
