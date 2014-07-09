package com.xenoage.zong.desktop.io.midi.out;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import com.sun.media.sound.AudioSynthesizer;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.jse.settings.Settings;

/**
 * This class shares a {@link Synthesizer}, {@link Sequencer}
 * and other objects needed for MIDI and audio playback.
 * 
 * It should be initialized at the start of the program by
 * calling the <code>init()</code>method and
 * when closing the program, the <code>close()</code> method
 * should be called.
 * 
 * Some code is based on the SimpleMidiPlayer demo
 * application from Gervill.
 * 
 * @author Andreas Wenger
 */
public class SynthManager {

	public static final String configFile = "synth";
	private static SynthManager instance = null;

	private Synthesizer synthesizer = null;
	private Sequencer sequencer = null;
	private Soundbank soundbank = null;

	private Mixer mixer = null;
	private SourceDataLine line = null;
	private AudioFormat format = null;

	private Set<ControllerEventListener> controllerEventListeners = new HashSet<ControllerEventListener>();


	/**
	 * Initializes the manager. Must be called at the beginning one time
	 * and each time when the audio settings should be reloaded.
	 * @param readSettings  true, to load the settings from the configuration file.
	 *                      false, to use default settings (e.g. for an applet)
	 */
	public static void init(boolean readSettings)
		throws MidiUnavailableException {
		//TIDY method (especially default settings)
		if (instance == null) {
			instance = new SynthManager();
		}
		if (readSettings) {
			//load settings (or use default ones)
			Settings s = Settings.getInstance();
			String file = configFile;
			String deviceName = s.getSetting("devicename", file);
			float sampleRate = s.getSetting("samplerate", file, 44100);
			int sampleSizeInBits = s.getSetting("bits", file, 16);
			int channels = s.getSetting("channels", file, 2);
			int latency = s.getSetting("latency", file, 100);
			int polyphony = s.getSetting("polyphony", file, 64);
			String interpolation = s.getSetting("interpolation", file, "linear");
			String soundbank = s.getSetting("soundbank", file, null);
			//init midi and soundbank
			instance.initMidi(sampleRate, sampleSizeInBits, channels, latency, polyphony, deviceName,
				interpolation);
			if (soundbank != null && soundbank.length() > 0) {
				try {
					instance.loadSoundbank(new File(soundbank));
				} catch (InvalidFormatException ex) {
					//TODO
				}
			}
		}
		else {
			instance.initMidi(44100, 16, 2, 100, 64, null, "linear");
		}
	}

	/**
	 * Gets the single instance of this manager.
	 * Throws an IllegalStateEx when init was not called before
	 * (this additional method was created so that there is no
	 * need for throwing a MidiUnavail here).
	 */
	private static SynthManager getInstance() {
		if (instance == null)
			throw new IllegalStateException("init() must be called first");
		return instance;
	}

	/**
	 * For the parameters, see {@link Sequencer#addControllerEventListener}.
	 * Call this method instead, because this class will remember the registered
	 * listeners so you can call removeAllControllerEventListeners.
	 */
	public static void addControllerEventListener(ControllerEventListener listener, int[] controllers) {
		SynthManager instance = getInstance();
		instance.controllerEventListeners.add(listener);
		instance.sequencer.addControllerEventListener(listener, controllers);
	}

	/**
	 * Removes all addeed ControllerEventListeners.
	 */
	public static void removeAllControllerEventListeners() {
		SynthManager instance = getInstance();
		for (ControllerEventListener listener : instance.controllerEventListeners)
			instance.sequencer.removeControllerEventListener(listener, null);
		instance.controllerEventListeners.clear();
	}

	/**
	 * Closes all MIDI objects and frees the resources.
	 */
	public static void close() {
		SynthManager t = instance;
		if (t.sequencer != null) {
			t.sequencer.stop();
			t.sequencer.close();
		}
		if (t.synthesizer != null) {
			t.synthesizer.close();
		}
		if (t.mixer != null) {
			t.mixer.close();
		}
		instance = null;
	}

	/**
	 * Gets the synthesizer.
	 */
	public static Synthesizer getSynthesizer() {
		return getInstance().synthesizer;
	}

	/**
	 * Gets the sequencer.
	 */
	public static Sequencer getSequencer() {
		return getInstance().sequencer;
	}

	public static Soundbank getSoundbank() {
		return getInstance().soundbank;
	}

	/**
	 * Loads the soundbank from the given file.
	 * If it fails, an {@link InvalidFormatException} is thrown.
	 */
	public void loadSoundbank(File file)
		throws InvalidFormatException {
		try {
			FileInputStream fis = new FileInputStream(file);
			Soundbank newSB;
			try {
				newSB = MidiSystem.getSoundbank(new BufferedInputStream(fis));
			} finally {
				fis.close();
			}
			if (soundbank != null)
				synthesizer.unloadAllInstruments(soundbank);
			soundbank = newSB;
			synthesizer.loadAllInstruments(soundbank);
		} catch (Exception ex) {
			throw new InvalidFormatException("Invalid soundbank: " + file, ex);
		}
	}

	/**
	 * (Re)initializes the MIDI objects and (re)configures the audio settings.
	 * If currently playback is running, it is stopped.
	 * TIDY
	 * @param sampleRate		    the number of samples per second, e.g. 44100
	 * @param sampleSizeInBits	the number of bits in each sample, e.g. 16
	 * @param channels			    the number of channels (1 for mono, 2 for stereo, and so on)
	 * @param latency           the latency in ms
	 * @param polyphony         maximum number of concurrent notes
	 * @param deviceName        name of the device, or null for default
	 * @param interpolation     linear, cubic, sinc or point
	 */
	public void initMidi(float sampleRate, int sampleSizeInBits, int channels, int latency,
		int polyphony, String deviceName, String interpolation)
		throws MidiUnavailableException {
		Sequence sequence = null;
		if (sequencer != null) {
			sequencer.stop();
			sequence = sequencer.getSequence();
		}
		if (synthesizer != null) {
			synthesizer.close();
		}
		if (mixer != null) {
			mixer.close();
		}

		format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);

		if (deviceName != null) {
			Mixer.Info selinfo = null;
			for (Mixer.Info info : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(info);
				boolean hassrcline = false;
				for (Line.Info linfo : mixer.getSourceLineInfo()) {
					if (linfo instanceof javax.sound.sampled.DataLine.Info) {
						hassrcline = true;
						break;
					}
				}
				if (hassrcline) {
					if (info.getName().equals(deviceName)) {
						selinfo = info;
						break;
					}
				}
			}
			if (selinfo != null) {
				mixer = AudioSystem.getMixer(selinfo);
				try {
					mixer.open();
					int bufferSize = (int) (format.getFrameSize() * format.getFrameRate() * latency / 1000f);
					if (bufferSize < 500)
						bufferSize = 500;
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format, bufferSize);
					if (mixer.isLineSupported(dataLineInfo)) {
						line = (SourceDataLine) mixer.getLine(dataLineInfo);
						line.open(format, bufferSize);
						line.start();
					}
				} catch (Throwable t) {
					mixer = null;
				}
			}
		}

		Map<String, Object> ainfo = new HashMap<String, Object>();
		ainfo.put("format", format);
		ainfo.put("max polyphony", polyphony);
		ainfo.put("latency", latency * 1000L);

		ainfo.put("interpolation", interpolation);
		ainfo.put("large mode", true);

		AudioSynthesizer synth = findAudioSynthesizer();
		if (synth == null)
			return; //no audio synthesizer

		synth.open(line, ainfo);

		synthesizer = synth;
		if (soundbank == null) {
			soundbank = synth.getDefaultSoundbank();
		}

		if (sequencer == null) {
			try {
				sequencer = MidiSystem.getSequencer(false);
			} catch (MidiUnavailableException ex) {
				//sequencer already open. no problem.
			}
		}
		if (sequencer.isOpen()) {
			sequencer.close();
		}
		sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
		sequencer.open();
		if (sequence != null) {
			try {
				sequencer.setSequence(sequence);
			} catch (InvalidMidiDataException ex) {
			}
		}
	}

	private AudioSynthesizer findAudioSynthesizer()
		throws MidiUnavailableException {
		//first check if default synthesizer is AudioSynthesizer.
		Synthesizer synth = MidiSystem.getSynthesizer();
		if (synth instanceof AudioSynthesizer)
			return (AudioSynthesizer) synth;

		//if default synthesizer is not AudioSynthesizer, check others.
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(infos[i]);
			if (dev instanceof AudioSynthesizer)
				return (AudioSynthesizer) dev;
		}

		//no AudioSynthesizer was found, return null.
		return null;
	}

	/**
	 * Gets a list with the names of the available audio mixers.
	 * The mixers are in the order provided by the platform, so
	 * their indices can be used to identify them.
	 */
	public static List<String> getAudioMixers() {
		ArrayList<String> mixerNames = new ArrayList<String>();
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			Mixer mixer = AudioSystem.getMixer(info);
			boolean hassrcline = false;
			for (Line.Info linfo : mixer.getSourceLineInfo())
				if (linfo instanceof javax.sound.sampled.DataLine.Info)
					hassrcline = true;
			if (hassrcline) {
				mixerNames.add(info.getName());
			}
		}
		return mixerNames;
	}
	
}
