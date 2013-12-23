package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.core.position.IMP.imp0;
import static com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer.midiScorePlayer;

import javax.sound.midi.MidiUnavailableException;

import com.xenoage.utils.io.IO;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.InstrumentBase;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Articulation;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.NormalTime;
import com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.io.score.ScoreController;
import com.xenoage.zong.io.selection.Cursor;



/**
 * @author Uli Teschemacher
 */
public class MidiVelocityConverterTry
{
	
	
	public static void main(String... args)
	{
		new MidiVelocityConverterTry().midiConverterTest();
	}
	
	
	public void midiConverterTest()
	{
		IO.initTest();
		
		Score score = createTestScore();
		try
		{
			SynthManager.init(false);
			MidiScorePlayer pl = midiScorePlayer();
			pl.addPlaybackListener(new MidiScorePlayerTry());	
			pl.openScore(score);
			pl.start();
			try { Thread.sleep(8000); } catch (InterruptedException ex) { }
			SynthManager.close();
		}
		catch (MidiUnavailableException e)
		{
		}
		
		/*
		
		byte[] message = sequence.getTracks()[0].get(0).getMessage().getMessage();
		//Assert.assertEquals(58, message[2]);
		message = sequence.getTracks()[0].get(1).getMessage().getMessage();
		Assert.assertEquals(116, message[2]);
		//Message 2 is note-off event
		message = sequence.getTracks()[0].get(3).getMessage().getMessage();
		Assert.assertEquals(110, message[2]);
		//Message 4 and 5 are note-off events
		message = sequence.getTracks()[0].get(6).getMessage().getMessage();
		Assert.assertEquals(110, message[2]);
		//Message 7 is note-off event
		message = sequence.getTracks()[0].get(8).getMessage().getMessage();
		Assert.assertEquals(110, message[2]);
		message = sequence.getTracks()[0].get(9).getMessage().getMessage();
		Assert.assertEquals(116, message[2]);
		//Message 10 is note-off event
		message = sequence.getTracks()[0].get(11).getMessage().getMessage();
		Assert.assertEquals(58, message[2]);
		//Message 12 and 13 are note-off events
		message = sequence.getTracks()[0].get(14).getMessage().getMessage();
		Assert.assertEquals(58, message[2]);
		//Message15 is note-off event
		message = sequence.getTracks()[0].get(16).getMessage().getMessage();
		Assert.assertEquals(102, message[2]);
		message = sequence.getTracks()[0].get(17).getMessage().getMessage();
		Assert.assertEquals(58, message[2]);
		//Message 18 is note-off event
		message = sequence.getTracks()[0].get(19).getMessage().getMessage();
		Assert.assertEquals(58, message[2]);
		//Message 20 is note-off event
		message = sequence.getTracks()[0].get(21).getMessage().getMessage();
		Assert.assertEquals(58, message[2]);
		//Message 22, 23 and 24 are note-off events
		*/
	}
	
	

	public static Score createTestScore()
	{
		Score ret = Score.empty;
		Instrument instr = new PitchedInstrument(
			new InstrumentBase("piano", "Piano", "Pno.", null, null, null),
			0, Transpose.none(), null, null, 0);
		Part pianoPart = new Part("Test","T",1, pvec(instr));
		
		ret = ret.plusPart(pianoPart);

		//measure.plusColumnElement(new NormalTime(3, 4), fr(0, 4));
		//ret.get
		
		Cursor cursor = new Cursor(ret, imp0, true);
		
		cursor = cursor.write((ColumnElement)new TraditionalKey(-3));
		cursor = cursor.write((ColumnElement) new NormalTime(3,4));
		
		cursor = cursor.write(new Clef(ClefType.G));

		Fraction f4 = fr(1, 4);

		
		Chord attachC;
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.G,4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.A, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.ff)));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.G, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.sfp)));

		/*
		Chord chord;
		Voice voice = measure.getVoices().get(0);
		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.pp));
		
		

		chord = voice.addNote(pi'A', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.ff));

		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.sfp));
	*/
		
		cursor = cursor.withIMP(imp0.withVoice(1));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.C, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.fff)));
		
		/*
		voice = measure.addVoice();
		
		chord = voice.addNote(pi'C', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.fff));
		*/

		cursor = cursor.withIMP(imp0.withMeasure(1));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.G, 4)));
		//cursor.withScore(ScoreController.attachElement(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.A, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		cursor = cursor.write(attachC = chord(f4,Pitch.pi(Pitch.G, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		
		cursor = cursor.withIMP(cursor.getIMP().withElement(0).withVoice(1));
		
		cursor = cursor.write(attachC = chord(f4, Pitch.pi(Pitch.C, 5)));
		/*		
		measure = staff.getMeasures().get(1);

		voice = measure.getVoices().get(0);
		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		//chord.addDirection(new Dynamics(DynamicsType.pp));

		chord = voice.addNote(pi'A', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.pp));

		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.pp));

		voice = measure.addVoice();
		chord = voice.addNote(pi'C', 0, 5), fr(1, 4));
*/
		
		cursor = cursor.withIMP(imp0.withMeasure(2));
		
		cursor = cursor.write(chord(f4,Pitch.pi(Pitch.G, 4)));
		cursor = cursor.withScore(ScoreController.plusAttachment(cursor.getScore(), attachC, new Dynamics(DynamicsType.sfz)));
		
		cursor = cursor.write(chord(f4,Pitch.pi(Pitch.A, 4)));
		//cursor = cursor.withScore(ScoreController.attachElement(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		cursor = cursor.write(chord(f4,Pitch.pi(Pitch.G, 4)));
		//cursor = cursor.withScore(ScoreController.attachElement(cursor.getScore(), attachC, new Dynamics(DynamicsType.pp)));
		
		
		cursor = cursor.withIMP(imp0.withMeasure(2).withVoice(2));
		
		cursor = cursor.write(chord(f4,Pitch.pi(Pitch.C, 5)));
		/*
		measure = staff.getMeasures().get(2);

		
		voice = measure.getVoices().get(0);
		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		chord.addDirection(new Dynamics(DynamicsType.sfz));

		chord = voice.addNote(pi'A', 0, 4), fr(1, 4));
		//chord.addDirection(new Dynamics(DynamicsType.pp));

		chord = voice.addNote(pi'G', 0, 4), fr(1, 4));
		//chord.addDirection(new Dynamics(DynamicsType.pp));
		
		voice = measure.addVoice();
		voice = measure.addVoice();
		chord = voice.addNote(pi'C',0,5), fr(1,2));

*/
		return cursor.getScore();
	}

	
	private static Chord chord(Fraction fraction, Pitch... pitches)
	{
		return chord(fraction, null, pitches);
	}


	private static Chord chord(Fraction fraction, Articulation[] articulations,
		Pitch... pitches)
	{
		return new Chord(Note.createNotes(pitches), fraction, null,
			articulations != null ? pvec(articulations) : null);
	}
	

	
}
