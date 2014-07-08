package com.xenoage.zong.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.xenoage.utils.base.settings.Settings;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.io.filefilter.SoundbankFileFilter;
import com.xenoage.zong.io.midi.out.SynthManager;


/**
 * Configuration dialog for audio settings.
 * Based on ConfigDialog.java, written by Karl Helgason
 * for the Gervill 0.8 demos, which license is:
 * 
 * <i>
 * Copyright (c) 2007 by Karl Helgason
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * </i>
 * 
 * @author Andreas Wenger
 */
public class AudioSettingsDialog
	extends JDialog
{
	
	private boolean isOK = false;


	public boolean isOK()
	{
		return isOK;
	}


	public AudioSettingsDialog(Frame parent)
	{
		super(parent);
		setSize(550, 400);
		setModal(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(Lang.get(Voc.AudioSettings));

		ArrayList<String> dev_list = new ArrayList<String>();
		dev_list.add(Lang.get(Voc.Default));
		for (Mixer.Info info : AudioSystem.getMixerInfo())
		{
			Mixer mixer = AudioSystem.getMixer(info);
			boolean hassrcline = false;
			for (Line.Info linfo : mixer.getSourceLineInfo())
				if (linfo instanceof javax.sound.sampled.DataLine.Info)
					hassrcline = true;
			if (hassrcline)
			{
				dev_list.add(info.getName());
			}
		}

		String[] devlist = new String[dev_list.size()];
		dev_list.toArray(devlist);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		setContentPane(panel);

		JPanel optpanel = new JPanel();
		optpanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		panel.add(optpanel);
		optpanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.anchor = GridBagConstraints.WEST;

		final JComboBox<String> co_devname = new JComboBox<String>(devlist);
		final JComboBox<String> co_samplerate = new JComboBox<String>(
			new String[] { "44100", "22050", "11025" });
		co_samplerate.setSelectedIndex(0);
		final JComboBox<String> co_channels = new JComboBox<String>(new String[] { "1", "2" });
		co_channels.setSelectedIndex(1);
		final JComboBox<String> co_bits = new JComboBox<String>(new String[] { "8", "16" });
		co_bits.setSelectedIndex(1);
		final JComboBox<String> co_latency = new JComboBox<String>(
			new String[] { "100", "200", "400", "800" });
		co_latency.setSelectedIndex(2);
		final JComboBox<String> co_polyphony = new JComboBox<String>(
			new String[] { "32", "64", "96", "128", "256" });
		co_polyphony.setSelectedIndex(1);
		final JComboBox<String> co_interp = new JComboBox<String>(new String[] {
			Lang.get(Voc.Linear), Lang.get(Voc.Cubic),
			Lang.get(Voc.Sinc), Lang.get(Voc.Point)});
		co_interp.setSelectedIndex(0);
		final JLabel lblSoundbankHidden = new JLabel(""); //full path to soundbank (hidden)
		final JLabel lblSoundbank = new JLabel(Lang.get(Voc.Default)); //file name of soundbank
		final JButton btnSoundbankDefault = new JButton(Lang.get(Voc.UseDefault));
		btnSoundbankDefault.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				lblSoundbankHidden.setText("");
				lblSoundbank.setText(Lang.get(Voc.Default));
				AudioSettingsDialog.this.pack();
			}
		});
		final JButton btnSoundbankSelect = new JButton(Lang.getWithEllipsis(Voc.Select));
		btnSoundbankSelect.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser(lblSoundbankHidden.getText());
		    fc.addChoosableFileFilter(new SoundbankFileFilter());
		    int ret = fc.showOpenDialog(AudioSettingsDialog.this);
		    if (ret == JFileChooser.APPROVE_OPTION)
		    {
		      File file = fc.getSelectedFile();
		      lblSoundbankHidden.setText(file.getAbsolutePath());
					lblSoundbank.setText(file.getName());
					AudioSettingsDialog.this.pack();
		    }
			}
		});
		final JPanel panelSoundbank = new JPanel();
		panelSoundbank.add(lblSoundbank);
		panelSoundbank.add(btnSoundbankDefault);
		panelSoundbank.add(btnSoundbankSelect);

		c.gridy = 0;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.DeviceName)), c);
		c.gridy = 0;
		c.gridx = 1;
		optpanel.add(co_devname, c);
		c.gridy = 1;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.SampleRateHz)), c);
		c.gridy = 1;
		c.gridx = 1;
		optpanel.add(co_samplerate, c);
		c.gridy = 2;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.Channels)), c);
		c.gridy = 2;
		c.gridx = 1;
		optpanel.add(co_channels, c);
		c.gridy = 3;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.Bits)), c);
		c.gridy = 3;
		c.gridx = 1;
		optpanel.add(co_bits, c);
		c.gridy = 4;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.LatencyMs)), c);
		c.gridy = 4;
		c.gridx = 1;
		optpanel.add(co_latency, c);
		c.gridy = 5;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.MaxPolyphony)), c);
		c.gridy = 5;
		c.gridx = 1;
		optpanel.add(co_polyphony, c);
		c.gridy = 6;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.get(Voc.InterpolationMode)), c);
		c.gridy = 6;
		c.gridx = 1;
		optpanel.add(co_interp, c);
		c.gridy = 7;
		c.gridx = 0;
		optpanel.add(new JLabel(Lang.getLabel(Voc.Soundbank)), c);
		c.gridy = 7;
		c.gridx = 1;
		optpanel.add(panelSoundbank, c);

		JButton okbutton = new JButton(Lang.get(Voc.OK));
		okbutton.addActionListener(new ActionListener()
		{

			@Override public void actionPerformed(ActionEvent e)
			{
				
				Settings s = Settings.getInstance();
				String file = SynthManager.CONFIG_FILE;
				s.setSetting("devicename", file,
					co_devname.getSelectedIndex() == 0 ? "" : co_devname.getSelectedItem().toString());
				s.setSetting("samplerate", file, co_samplerate.getSelectedItem());
				s.setSetting("channels", file, co_channels.getSelectedItem());
				s.setSetting("bits", file, co_bits.getSelectedItem());
				s.setSetting("latency", file, co_latency.getSelectedItem());
				s.setSetting("polyphony", file, co_polyphony.getSelectedItem());
				s.setSetting("interpolation", file, co_interp.getSelectedItem());
				s.setSetting("soundbank", file, lblSoundbankHidden.getText());
				s.save(file);
				
				isOK = true;
				AudioSettingsDialog.this.dispose();
			}
		});
		okbutton.setDefaultCapable(true);
		JButton cancelbutton = new JButton(Lang.get(Voc.Cancel));
		cancelbutton.addActionListener(new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent e)
			{
				AudioSettingsDialog.this.dispose();
			}
		});

		JPanel buttonpanel = new JPanel();
		panel.add(buttonpanel, BorderLayout.SOUTH);
		buttonpanel.add(okbutton);
		buttonpanel.add(cancelbutton);

		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		panel.registerKeyboardAction(new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		Settings s = Settings.getInstance();
		String file = SynthManager.CONFIG_FILE;
		selectValueInList(co_devname, s.getSetting("devicename", file));
		selectValueInList(co_samplerate, s.getSetting("samplerate", file));
		selectValueInList(co_channels, s.getSetting("channels", file));
		selectValueInList(co_bits, s.getSetting("bits", file));
		selectValueInList(co_latency, s.getSetting("latency", file));
		selectValueInList(co_polyphony, s.getSetting("polyphony", file));
		selectValueInList(co_interp, s.getSetting("interpolation", file));
		String soundbank = s.getSetting("soundbank", file);
		if (soundbank != null && soundbank.length() > 0)
		{
			try
			{
				File soundbankFile = new File(soundbank);
				if (soundbankFile.exists())
				{
					lblSoundbankHidden.setText(soundbankFile.getAbsolutePath());
					lblSoundbank.setText(soundbankFile.getName());
				}
			}
			catch (Exception ex)
			{
			}
		}
		pack();
	}
	
	
	private void selectValueInList(JComboBox<String> box, String value)
	{
		if (value == null)
			return;
		for (int i = 0; i < box.getItemCount(); i++)
		{
			if (box.getItemAt(i).equals(value))
			{
				box.setSelectedIndex(i);
				return;
			}
		}
	}



}
