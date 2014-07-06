package com.xenoage.zong.commands.player.playback;

import static com.xenoage.zong.player.PlayerApplication.pApp;

import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;


/**
 * This command pauses the MIDI playback.
 * 
 * @author Andreas Wenger
 */
public class PauseMidiPlaybackCommand
	extends Command
{

	@Override public void execute(CommandPerformer performer)
	{
		pApp().getPlayer().pause();
	}

}
