package com.xenoage.zong.commands.player.dialog;

import static com.xenoage.zong.SwingApp.app;
import static com.xenoage.zong.player.PlayerApp.pApp;

import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.CommandPerformer;
import com.xenoage.zong.gui.dialog.PlayerInfoDialog;


/**
 * This command shows the {@link PlayerInfoDialog} of the Player.
 * 
 * @author Andreas Wenger
 */
public class InfoDialogCommand
	extends Command
{

	@Override public void execute(CommandPerformer performer)
	{
    PlayerInfoDialog frmInfo = new PlayerInfoDialog(pApp().getMainFrame(),
    	app().getName(), true);
    frmInfo.setInformation(pApp().getScore());
    frmInfo.setActivePage(0);
    frmInfo.setVisible(true);
	}

}
