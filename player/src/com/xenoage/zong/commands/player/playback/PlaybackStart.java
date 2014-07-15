package com.xenoage.zong.commands.player.playback;

import static com.xenoage.zong.player.Player.pApp;

import com.xenoage.utils.document.command.TransparentCommand;

/**
 * This command starts the MIDI playback.
 * 
 * @author Andreas Wenger
 */
public class PlaybackStart
	extends TransparentCommand {

	@Override public void execute() {
		pApp().getPlayer().start();
	}

}
