package com.xenoage.zong.commands.core.music;

import static com.xenoage.zong.core.music.Voice.voice;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.document.exceptions.UselessException;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;


/**
 * Creates a {@link Voice} in a {@link Measure}.
 * 
 * For each voice x in a measure, there is also a voice x-1 (for x>0).
 * So maybe more than one voice is created.
 * If already existing, a {@link UselessException} is thrown.
 * 
 * @author Andreas Wenger
 */
public class VoiceAdd
	implements Command {

	//data
	private Measure measure;
	private int voiceIndex;
	//backup
	private int lastExistingVoiceIndex;
	
	
	public VoiceAdd(Measure measure, int voiceIndex) {
		this.measure = measure;
		this.voiceIndex = voiceIndex;
	}
	
	
	@Override public void execute() {
		if (measure.getVoices().size() > voiceIndex)
			throw new UselessException();
		lastExistingVoiceIndex = measure.getVoices().size() - 1;
		for (int i = lastExistingVoiceIndex + 1; i <= voiceIndex; i++) {
			Voice voice = voice();
			voice.setParent(measure);
			measure.getVoices().add(voice);
		}
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		for (int i = voiceIndex; i > lastExistingVoiceIndex; i++)
			measure.getVoices().remove(i);
	}
	

}
