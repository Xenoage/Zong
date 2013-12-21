package com.xenoage.zong.io.midi.out;

import static com.xenoage.zong.io.midi.out.MidiScorePlayer.midiScorePlayer;

import javax.sound.midi.MidiUnavailableException;

import com.xenoage.utils.io.IO;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.BMP;
import com.xenoage.zong.util.demo.ScoreRevolutionary;


/**
 * Test class for {@link MidiScorePlayer}
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiScorePlayerTry
	implements PlaybackListener
{
	
	public static void main(String args[])
		throws MidiUnavailableException
	{
		int timeFactor = 1000;
		IO.initTest();
		Score score = ScoreRevolutionary.createScore();
		SynthManager.init(false);
		MidiScorePlayer player = midiScorePlayer();
		player.addPlaybackListener(new MidiScorePlayerTry());
		player.openScore(score);
		player.start();
		try { Thread.sleep(2 * timeFactor); } catch (InterruptedException ex) { }
		player.pause();
		System.out.println("Pause");
		try { Thread.sleep(2 * timeFactor); } catch (InterruptedException ex) { }
		player.start();
		try { Thread.sleep(3 * timeFactor); } catch (InterruptedException ex) { }
		player.stop();
		System.out.println("Stop");
		try { Thread.sleep(2 * timeFactor); } catch (InterruptedException ex) { }
		player.start();
		try { Thread.sleep(3 * timeFactor); } catch (InterruptedException ex) { }
		player.stop();
		SynthManager.close();
	}

	
	@Override public void playbackAtEnd()
	{
	}

	@Override public void playbackAtMP(BMP mp, long ms)
	{
		System.out.println(mp.toString());
	}
	
	
	@Override public void playbackAtMs(long ms)
	{
	}
	
	
	@Override public void playbackStarted()
	{
	}
	
	
	@Override public void playbackPaused()
	{
	}

	
	@Override public void playbackStopped()
	{
	}
	
}
