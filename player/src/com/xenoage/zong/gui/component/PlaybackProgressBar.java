package com.xenoage.zong.gui.component;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.xenoage.zong.core.position.BMP;
import com.xenoage.zong.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.io.midi.out.PlaybackListener;


/**
 * Progress bar for playback.
 * 
 * @author Andreas Wenger
 */
public class PlaybackProgressBar
	extends JPanel
	implements PlaybackListener
{
	
	private JProgressBar bar;
	private MidiScorePlayer player;
	
	
	/**
	 * Creates a new {@link PlaybackProgressBar}, which listens to the given
	 * {@link MidiScorePlayer}.
	 */
	public PlaybackProgressBar(MidiScorePlayer player)
	{
		this.player = player;
		setLayout(new BorderLayout());
		bar = new JProgressBar();
		bar.setMaximum(10000);
		bar.setStringPainted(true);
		bar.setString("");
		bar.addMouseListener(new MouseAdapter()
		{
			@Override public void mousePressed(MouseEvent e)
			{
				MidiScorePlayer player = PlaybackProgressBar.this.player;
				if (player.getSequence() != null) {
					float pos = (1f * e.getX()) / bar.getWidth();
					long mis = (long) (pos * player.getMicrosecondLength());
					player.setMicrosecondPosition(mis);
					playbackAtMs(mis / 1000);
				}
			}
		});
		add(bar, BorderLayout.CENTER);
	}


	@Override public void playbackAtMP(BMP mp, long ms)
	{
	}


	@Override public void playbackAtMs(long ms)
	{
		long lengthMs = player.getMicrosecondLength() / 1000;
		if (lengthMs > 0) {
			bar.setValue((int) (1.0 * ms / lengthMs * bar.getMaximum()));
			bar.setString(formatTime(ms) + " / " + formatTime(lengthMs));
		} else {
			bar.setValue(0);
			bar.setString("");
		}
	}


	@Override public void playbackStarted()
	{
	}


	@Override public void playbackPaused()
	{
	}


	@Override public void playbackStopped()
	{
		playbackAtMs(0);
	}


	@Override public void playbackAtEnd()
	{
		playbackAtMs(0);
	}


	//TIDY: same code somewhere in Viewer
	private String formatTime(long timeMs)
	{
		long timeS = timeMs / 1000;
		String mins = String.valueOf(timeS / 60);
		String secs = String.valueOf(timeS % 60);
		if (secs.length() < 2) {
			secs = "0" + secs;
		}
		return mins + ":" + secs;
	}

}
