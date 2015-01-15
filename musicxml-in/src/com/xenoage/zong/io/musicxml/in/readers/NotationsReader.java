package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readBezierPoint;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPlacement;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readVSide;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.commands.core.music.direction.DirectionAdd;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.annotation.Ornament;
import com.xenoage.zong.core.music.annotation.OrnamentType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlArticulations;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlFermata;
import com.xenoage.zong.musicxml.types.MxlNotations;
import com.xenoage.zong.musicxml.types.MxlOrnaments;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied.MxlElementType;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent.MxlArticulationsContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent.MxlNotationsContentType;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent.MxlOrnamentsContentType;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopContinue;
import com.xenoage.zong.musicxml.types.enums.MxlUprightInverted;

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
