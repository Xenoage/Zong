package com.xenoage.zong.io.musicxml.in.readers;

import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.io.musicxml.Equivalents;
import com.xenoage.zong.musicxml.types.MxlClef;

/**
 * This class reads a {@link ClefType} from a {@link MxlClef}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class ClefReader {
	
	private final MxlClef mxlClef;
	
	private ClefSymbol symbol;
	private ClefType clefType;
	
	
	public Clef read() {
		if (mxlClef == null)
			return null;
		ClefType clefType = readType();
		Clef clef = (clefType != null ? new Clef(clefType) : null);
		return clef;
	}
	
	public int readStaff() {
		//staff (called "number" in MusicXML), first staff is default
		return mxlClef.getNumber() - 1;
	}
	
	private ClefType readType() {
		symbol = Equivalents.clefSymbols.get1(mxlClef.getSign());
		if (symbol == null)
			return null;
		readOctaveShift();
		readLine();
		return clefType;
	}
	
	private void readOctaveShift() {
		//for some clefs, we provide octave shifting. If unsupported, we ignore it
		int octaveChange = mxlClef.getClefOctaveChange();
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
	}
	
	private void readLine() {
		//read line or use default position
		Integer line = mxlClef.getLine();
		if (line == null) {
			clefType = ClefType.commonClefs.get(symbol);
		}
		else {
			//convert MusicXML line number (1 based) to LP (0 based)
			int lp = (line - 1) * 2;
			//special case: in MusicXML, the TAB symbol is based on the top line, but
			//in Zong!, it is centered, so move it 2 interline spaces down (= 4 LPs)
			if (symbol == ClefSymbol.Tab || symbol == ClefSymbol.TabSmall)
				lp -= 4;
			clefType = new ClefType(symbol, lp);
		}
	}

}
