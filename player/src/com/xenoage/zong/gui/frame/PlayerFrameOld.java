package com.xenoage.zong.gui.frame;

import static com.xenoage.zong.SwingApp.app;

import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.gui.IconReader;
import com.xenoage.zong.gui.menu.PlayerMenuBar;
import com.xenoage.zong.gui.panel.PlayerPanel;
import com.xenoage.zong.io.midi.out.MidiScorePlayer;


/**
 * Main frame of the Player.
 * 
 * @author Andreas Wenger
 */
public class PlayerFrameOld
	extends JFrame
{
	
	private PlayerMenuBar menuBar;
	private PlayerPanel panel;
	
	
	public PlayerFrameOld(MidiScorePlayer player)
	{
		//location and behavior
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		//icons
		loadIcons();
		//content
		setTitle(app().getName());
		menuBar = new PlayerMenuBar();
		setJMenuBar(menuBar.getJMenuBar());
		setLayout(new FlowLayout(FlowLayout.CENTER, PlayerPanel.border, PlayerPanel.border));
		panel = new PlayerPanel(player);
		add(panel);
		pack();
	}
	
	
	public void setScore(Score score)
	{
		panel.setScore(score);
	}
	
	
	private void loadIcons()
	{
		IconReader ir = new IconReader("logo/");
		List<Image> icons = new ArrayList<Image>();
		icons.add(ir.readImage("logo512.png"));
		icons.add(ir.readImage("logo256.png"));
		icons.add(ir.readImage("logo128.png"));
		icons.add(ir.readImage("logo64.png"));
		icons.add(ir.readImage("logo48.png"));
		icons.add(ir.readImage("logo32.png"));
		icons.add(ir.readImage("logo16.png"));
		setIconImages(icons);
	}

}
