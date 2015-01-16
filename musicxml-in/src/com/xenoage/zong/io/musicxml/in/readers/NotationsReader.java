package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.zong.commands.core.music.direction.DirectionAdd;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlArticulations;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlFermata;
import com.xenoage.zong.musicxml.types.MxlNotations;
import com.xenoage.zong.musicxml.types.MxlOrnaments;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent.MxlNotationsContentType;

/**
 * Reads the slurs, ties and dynamics from the {@link MxlNotations} of a chord.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class NotationsReader {
	
	private final List<MxlNotations> mxlNotations;
	

	public void readToNote(Chord chord, int noteIndex, int staffIndexInPart, Context context) {
		StaffDetails staffDetails = StaffDetails.fromContext(context, staffIndexInPart);
		ArrayList<Annotation> annotations = alist(0);
		for (MxlNotations mxlNotationsElement : mxlNotations) {
			for (MxlNotationsContent mxlNC : mxlNotationsElement.getElements()) {
				MxlNotationsContentType mxlNCType = mxlNC.getNotationsContentType();
				switch (mxlNCType) {
					
					case SlurOrTied: {
						SlurReader.readToContext(chord, noteIndex, staffIndexInPart, context, (MxlSlurOrTied) mxlNC);
						break;
					}
	
					case Dynamics: {
						Dynamics dynamics = DynamicsReader.read((MxlDynamics) mxlNC, staffDetails);
						new DirectionAdd(dynamics, chord).execute();
						break;
					}
	
					case Articulations: {
						MxlArticulations mxlArticulations = (MxlArticulations) mxlNC;
						annotations.addAll(ArticulationReader.read(mxlArticulations));
						break;
					}
					
					case Fermata: {
						Fermata fermata = FermataReader.read((MxlFermata) mxlNC, staffDetails);
						annotations.add(fermata);
						break;
					}
					
					case Ornaments: {
						MxlOrnaments mxlOrnaments = (MxlOrnaments) mxlNC;
						annotations.addAll(OrnamentReader.read(mxlOrnaments));
						break;
					}
					
					default:
				}
			}
		}
		
		if (annotations.size() > 0)
			chord.setAnnotations(annotations);
	}

}
