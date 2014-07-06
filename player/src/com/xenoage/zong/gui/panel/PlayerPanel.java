package com.xenoage.zong.gui.panel;

import static com.xenoage.utils.base.NullUtils.notNull;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.xenoage.zong.commands.Command;
import com.xenoage.zong.commands.dialogs.OpenDocumentDialogCommand;
import com.xenoage.zong.commands.player.dialog.InfoDialogCommand;
import com.xenoage.zong.commands.player.playback.PauseMidiPlaybackCommand;
import com.xenoage.zong.commands.player.playback.StartMidiPlaybackCommand;
import com.xenoage.zong.commands.player.playback.StopMidiPlaybackCommand;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.gui.IconReader;
import com.xenoage.zong.gui.component.PlaybackProgressBar;
import com.xenoage.zong.gui.component.VolumeSlider;
import com.xenoage.zong.gui.event.CommandActionListener;
import com.xenoage.zong.io.midi.out.MidiScorePlayer;


/**
 * The main panel of the Player.
 * 
 * @author Andreas Wenger
 */
public class PlayerPanel
	extends JPanel
{
	
	//player
	private MidiScorePlayer player;
	
	//components
	private JLabel lblTitle;
	
	//layout settings
	public static final int border = 4;
	public static final Dimension buttonSize = new Dimension(32, 32);
	
	
	public PlayerPanel(MidiScorePlayer player)
	{
		this.player = player;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addTitlePanel();
		addProgressPanel();
		addButtons();
	}
	
	
	/**
	 * Adds the title label to the panel.
	 */
	private void addTitlePanel()
	{
		JPanel pnlTitle = new JPanel();
		pnlTitle.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
		pnlTitle.setLayout(new GridLayout(1, 1));
		add(pnlTitle);
		lblTitle = new JLabel(" "); //space character to reserve layout space
		pnlTitle.add(lblTitle);
	}
	
	
	/**
	 * Adds the playback progress bar to the panel.
	 */
	private void addProgressPanel()
	{
		JPanel pnlProgress = new JPanel();
		pnlProgress.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
		pnlProgress.setLayout(new GridLayout(1, 1));
		add(pnlProgress);
		PlaybackProgressBar bar = new PlaybackProgressBar(player);
		player.addPlaybackListener(bar);
		pnlProgress.add(bar);
	}
	
	
	/**
	 * Adds the buttons for playback, volume and info to the panel.
	 */
	private void addButtons()
	{
		IconReader ir = new IconReader("gui/22/");
		//buttons panel
		FlowLayout buttonsLayout = new FlowLayout(FlowLayout.CENTER, border / 2, border);
		JPanel pnlButtons = new JPanel(buttonsLayout);
		add(pnlButtons);
		//add playback buttons
		pnlButtons.add(jButton(ir, "media-eject.png", new OpenDocumentDialogCommand()));
		pnlButtons.add(jButton(ir, "media-playback-start.png", new StartMidiPlaybackCommand()));
		pnlButtons.add(jButton(ir, "media-playback-pause.png", new PauseMidiPlaybackCommand()));
		pnlButtons.add(jButton(ir, "media-playback-stop.png", new StopMidiPlaybackCommand()));
		//add volume control
		pnlButtons.add(new VolumeSlider(player));
		//add document info button
		pnlButtons.add(jButton(ir, "documentinfo.png", new InfoDialogCommand()));
	}
	
	
	private JButton jButton(IconReader ir, String iconID, Command command)
	{
		JButton ret = new JButton(ir.read(iconID));
		ret.setPreferredSize(buttonSize);
		if (command != null)
			ret.addActionListener(new CommandActionListener(command));
		return ret;
	}
	
	
	public void setScore(Score score)
	{
		//set title
		String title = notNull(score.info.getComposer());
		if (title.length() > 0)
			title += " - ";
		title += notNull(score.info.getTitle());
		lblTitle.setText("<html><b>" + title + "</b></html>");
		//update layout
		invalidate();
	}

}
