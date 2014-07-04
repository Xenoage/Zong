package com.xenoage.zong.player;

import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.zong.io.midi.out.MidiScorePlayer.midiScorePlayer;

import javax.swing.JOptionPane;

import com.xenoage.utils.base.CommandLine;
import com.xenoage.utils.base.exceptions.ThrowableUtils;
import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.io.ApplicationIO;
import com.xenoage.utils.io.IO;
import com.xenoage.utils.log.AppLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.SwingApp;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.Document;
import com.xenoage.zong.gui.GUIErrorProcessing;
import com.xenoage.zong.gui.frame.PlayerFrame;
import com.xenoage.zong.io.PlayerSupportedFormats;
import com.xenoage.zong.io.midi.out.MidiScorePlayer;


/**
 * Main class for the Zong! Player desktop application.
 * 
 * @author Andreas Wenger
 */
public class PlayerApplication
	extends SwingApp<Score>
{
	
	private static final String appFirstName = "Player";
	
	private MidiScorePlayer player;
	
	
	public static void main(String[] args)
	{
		CommandLine.setArgs(args);
		
		//schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		//TIDY: unifiy error handling with viewer/editor
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override public void run()
			{
				try {
					new PlayerApplication();
				} catch (Throwable error) {
					//here all uncaught exceptions and errors are caught.
					try {
						new BasicErrorProcessing().report(fatal("Unknown program error.", error));
					} catch (Throwable error2) {
						//ignore (we can not do something)
					}
					if (JOptionPane.showConfirmDialog(null, "Unknown program error!\nShow stack trace?",
						Zong.PROJECT_FAMILY_NAME, JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
						String stackTrace = ThrowableUtils.getStackTrace(error);
						if (stackTrace.length() > 2000)
							stackTrace = stackTrace.substring(0, 2000) + "...";
						JOptionPane.showMessageDialog(null, stackTrace);
					}
				}
			}
		});
	}
	
	
	/**
	 * Creates a new {@link PlayerApplication}.
	 */
	public PlayerApplication()
	{
		super(SwingType.DesktopApp, DocumentInterface.SDI);
		
		this.supportedFormats = PlayerSupportedFormats.getInstance();
		
		//try to open file (first parameter)
		if (CommandLine.getArgs().length > 0) {
			try {
				openDocument(CommandLine.getArgs()[0]);
			} catch (Exception ex) {
				//could not load file.
			}
		}
		
		mainFrame.setVisible(true);
	}
	
	
	/**
	 * Gets the only instance of the {@link PlayerApplication} class. If this
	 * program is not running as this type, null is returned.
	 */
	public static PlayerApplication pApp()
	{
		if (app() instanceof PlayerApplication)
			return (PlayerApplication) app();
		else
			return null;
	}
	
	
	/**
   * Initializes the IO interface, using {@link ApplicationIO}.
   */
  @Override protected void initIO()
  {
  	IO.initApplication(getAppPath());
  }
  
  
  /**
   * Initializes the logging interface, using {@link AppLogProcessing}.
   */
  @Override protected void initLog()
  {
  	Log.init(new AppLogProcessing(getNameAndVersion()));
  }
  
  
  /**
   * Initializes the error handling interface, using {@link GUIErrorProcessing}.
   */
  @Override protected void initErr()
  {
  	Err.init(new GUIErrorProcessing());
  }
  
  
  @Override protected void initAudio()
	{
  	super.initAudio();
		this.player = midiScorePlayer();
	}
	
	
	@Override protected void initGUI()
	{
		this.mainFrame = new PlayerFrame(player);
	}


	@Override public String getAppFirstName()
	{
		return appFirstName;
	}
	
	
	/**
	 * Gets the currently loaded {@link Score}.
	 */
	public Score getScore()
	{
		Document<Score> doc = getActiveDocument();
		if (doc == null)
			return null;
		else
			return doc.getData();
	}
	
	
	/**
	 * Gets the {@link MidiScorePlayer} for MIDI playback.
	 * @return
	 */
	public MidiScorePlayer getPlayer()
	{
		return player;
	}
  
 
 	@Override public Score openDocumentData(String filePath)
	{
		return loadMxlScore(filePath);
	}
 	
 	
 	@Override public void addDocument(Document<Score> doc)
  {
  	super.addDocument(doc);
  	((PlayerFrame) mainFrame).setScore(doc.getData());
    player.openScore(doc.getData());
    player.setMetronomeEnabled(false);
  	player.start();
	}


}
