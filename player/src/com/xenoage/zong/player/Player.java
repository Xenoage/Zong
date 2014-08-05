package com.xenoage.zong.player;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer.midiScorePlayer;
import javafx.application.Application;

import javax.swing.JOptionPane;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.CommandLine;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.App;
import com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.desktop.utils.error.GuiErrorProcessing;
import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.io.PlayerSupportedFormats;

/**
 * Main class for the Zong! Player desktop application.
 * 
 * @author Andreas Wenger
 */
public class Player
	extends App<Score> {

	public static final String appFirstName = "Player";
	
	/** The {@link MidiScorePlayer} for MIDI playback. */
	@Getter private MidiScorePlayer player;
	
	/** The controller of the main frame. */
	@Getter @Setter private PlayerFrame playerController = null;


	public static void main(String[] args) {
		CommandLine.setArgs(args);
		
		try {
			new Player();	
		} catch (Throwable error) {
			//here all uncaught exceptions and errors are caught. - TODO: really? not tested for JavaFX
			try {
				new BasicErrorProcessing().report(fatal("Unknown program error.", error));
			} catch (Throwable error2) {
				//ignore (we can not do something)
			}
			if (JOptionPane.showConfirmDialog(null, "Unknown program error!\nShow stack trace?",
				Zong.projectFamilyName, JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
				String stackTrace = platformUtils().getStackTraceString(error);
				if (stackTrace.length() > 2000)
					stackTrace = stackTrace.substring(0, 2000) + "...";
				JOptionPane.showMessageDialog(null, stackTrace);
			}
		}
	}

	/**
	 * Creates a new {@link Player}.
	 */
	public Player()
		throws Exception {
		super(appFirstName, AppType.DesktopApp, DocumentInterface.SDI);

		this.supportedFormats = PlayerSupportedFormats.getInstance();

		//try to open file (first parameter)
		if (CommandLine.getArgs().length > 0) {
			try {
				openDocument(CommandLine.getArgs()[0]);
			} catch (Exception ex) {
				//could not load file.
			}
		}

		//start JavaFX app
		Application.launch(PlayerFXApp.class, CommandLine.getArgs());	
	}

	/**
	 * Gets the only instance of the {@link Player} class. If this
	 * program is not running as this type, null is returned.
	 */
	public static Player pApp() {
		if (app() instanceof Player)
			return (Player) app();
		else
			return null;
	}

	/**
	 * Initializes the logging interface, using {@link DesktopLogProcessing}.
	 */
	@Override protected void initLog() {
		Log.init(new DesktopLogProcessing(getNameAndVersion()));
	}

	/**
	 * Initializes the error handling interface, using {@link GUIErrorProcessing}.
	 */
	@Override protected void initErr() {
		Err.init(new GuiErrorProcessing());
	}

	@Override protected void initAudio() {
		super.initAudio();
		this.player = midiScorePlayer();
	}

	/**
	 * Gets the currently loaded {@link Score}.
	 */
	public Score getScore() {
		return getActiveDocument();
	}

	@Override public Score openDocumentData(String filePath) {
		return loadMxlScore(filePath);
	}

	@Override public void addDocument(Score doc) {
		super.addDocument(doc);
		PlayerFXApp.getInstance().getFrame().setScore(doc);
		player.openScore(doc);
		player.setMetronomeEnabled(false);
		player.start();
	}

	@Override public void close() {
		player.stop();
		super.close();
	}
	
	

}
