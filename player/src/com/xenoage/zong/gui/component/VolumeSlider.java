package com.xenoage.zong.gui.component;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xenoage.zong.gui.IconReader;
import com.xenoage.zong.io.midi.out.MidiScorePlayer;


/**
 * Slider for controlling the playback volume.
 * There is also an icon showing the volume (high, medium, low, or muted).
 * 
 * @author Andreas Wenger
 */
public class VolumeSlider
	extends JPanel
{
	
	private static final int height = 22;
	private static final int sliderWidth = 200;
	
	private MidiScorePlayer player;
	private Map<String, Icon> volumeIcons;
	
	
	/**
	 * Creates a new {@link VolumeSlider} for the given {@link MidiScorePlayer}.
	 */
	public VolumeSlider(MidiScorePlayer player)
	{
		this.player = player;
		IconReader ir = new IconReader("gui/22/");
		//add volume icon
		volumeIcons = new HashMap<String, Icon>();
		volumeIcons.put("high", ir.read("audio-volume-high.png"));
		volumeIcons.put("medium", ir.read("audio-volume-medium.png"));
		volumeIcons.put("low", ir.read("audio-volume-low.png"));
		volumeIcons.put("muted", ir.read("audio-volume-muted.png"));
		final JLabel lblVolume = new JLabel(volumeIcons.get("medium"));
		lblVolume.setPreferredSize(new Dimension(height, height));
		add(lblVolume);

		//add volume slider
		final JSlider sldVolume = new JSlider(0, 100);
		sldVolume.setPaintLabels(false);
		sldVolume.setMajorTickSpacing(1);
		sldVolume.setMinorTickSpacing(1);
		sldVolume.setPaintTicks(false);
		sldVolume.setMinimumSize(new Dimension(sliderWidth, height));
		sldVolume.addChangeListener(new ChangeListener()
		{
			@Override public void stateChanged(ChangeEvent e)
			{
				int val = sldVolume.getValue();
				VolumeSlider.this.player.setVolume(0.01f * val);
				String sVolume = "muted";
				if (val > 80)
					sVolume = "high";
				else if (val > 40)
					sVolume = "medium";
				else if (val > 0)
					sVolume = "low";
				lblVolume.setIcon(volumeIcons.get(sVolume));
			}
		});
		add(sldVolume);
	}

}
