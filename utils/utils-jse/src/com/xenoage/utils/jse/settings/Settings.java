package com.xenoage.utils.jse.settings;

import com.xenoage.utils.Parser;
import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.jse.io.DesktopIO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.utils.log.Report.warning;

/**
 * This class manages simple configuration data, which is String values
 * identified by String keys.
 * 
 * The values are stored in .settings files, which are XML files in the format
 * described at http://java.sun.com/dtd/properties.dtd
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class Settings {

	private static Settings instance = null;
	private static BasicErrorProcessing err = null;

	private String directory;
	private Hashtable<String, Properties> files;


	/**
	 * Gets the only instance of the {@link Settings} class.
	 */
	public static Settings getInstance() {
		return settings();
	}
	
	/**
	 * Gets the only instance of the {@link Settings} class.
	 */
	public static Settings settings() {
		if (instance == null) {
			instance = new Settings("data/config");
		}
		return instance;
	}

	/**
	 * Sets the error processing class. If unset,
	 * a {@link RuntimeException} will be thrown in any method where an error occurs.
	 */
	public static void setErrorProcessing(BasicErrorProcessing err) {
		Settings.err = err;
	}

	/**
	 * Creates a new Settings instance using the given directory to locate the
	 * .settings files.
	 */
	public Settings(String directory) {
		// save directory
		this.directory = directory;
		while (this.directory.endsWith("/")) {
			this.directory = this.directory.substring(0, this.directory.length() - 1);
		}
		// load the settings from the .settings files
		this.files = new Hashtable<>();
		List<String> fileList = null;
		fileList = io().listFiles(directory);
		for (String file : fileList) {
			String name = file;
			if (name.endsWith(".settings")) {
				try {
					reload(FileUtils.getNameWithoutExt(file));
				} catch (Exception ex) {
					if (err != null)
						err.report(error("Could not load settings from " + file, ex));
					else
						throw new RuntimeException("Could not load settings from " + file, ex);
				}
			}
		}
	}

	/**
	 * Gets the value of the setting with the given key. If not found, null is
	 * returned.
	 * @param key the key of the setting
	 * @param file the filename where the setting can be found, without
	 * extension
	 */
	public String getSetting(String key, String file) {
		Properties p = files.get(file);
		if (p != null) {
			return p.getProperty(key);
		}
		return null;
	}

	/**
	 * Sets the value of the setting with the given key. If the key or the file
	 * doesn't exist yet, it is created. The setting is not stored in the file
	 * immediately, but may be saved automatically later. To save it
	 * immediately, <code>save</code> has to be called, or use
	 * <code>saveSetting</code>.
	 * @param key the key of the setting
	 * @param file the filename where the setting will be stored, without extension
	 * @param value the new value of the setting. It is converted to a string internally.
	 */
	public void setSetting(String key, String file, Object value) {
		Properties p = files.computeIfAbsent(file, k -> new Properties());
		p.put(key, value.toString());
	}

	/**
	 * Sets the value of th setting with the given key. If the key or the file
	 * doesn't exist yet, it is created. The setting is immediately stored in
	 * the given file.
	 * @param key the key of the setting
	 * @param file the filename where the setting will be stored, without extension
	 * @param value the new value of the setting. It is converted to a string internally.
	 */
	public void saveSetting(String key, String file, Object value) {
		setSetting(key, file, value.toString());
		save(file);
	}

	/**
	 * Saves the settings of the given file permanently.
	 */
	public void save(String file) {
		Properties p = files.get(file);
		if (p != null) {
			try {
				OutputStream out = new FileOutputStream(
					io().createFile(this.directory + "/" + file + ".settings"));
				p.storeToXML(out, "Changed " + new Date());
				out.close();
			} catch (IOException ex) {
				log(warning("Could not store settings to file \"" + file + "\"", ex));
			}
		}
	}

	/**
	 * Reloads the given settings file.
	 */
	public void reload(String file) {
		try {
			Properties p = new Properties();
			String filePath = directory + "/" + file + ".settings";
			p.loadFromXML(io().openFile(filePath));
			files.put(file, p);
		} catch (Exception ex) {
			log(warning("Could not load settings from file \"" + file + "\"", ex));
		}
	}

	/**
	 * Convenience method: Gets the given setting as a float value, or use the
	 * given default value, if the is setting not available or readable.
	 */
	public float getSetting(String key, String file, float defaultValue) {
		String s = getSetting(key, file);
		if (s != null) {
			Float ret = Parser.parseFloatNull(s);
			if (ret != null) {
				return ret;
			}
		}
		return defaultValue;
	}

	/**
	 * Convenience method: Gets the given setting as a boolean value, or use the
	 * given default value, if the is setting not available or readable.
	 */
	public boolean getSetting(String key, String file, boolean defaultValue) {
		String s = getSetting(key, file);
		if (s != null) {
			Boolean ret = Parser.parseBooleanNull(s);
			if (ret != null) {
				return ret;
			}
		}
		return defaultValue;
	}

	/**
	 * Gets all settings of the given file as a {@link HashMap}. If the file
	 * does not exist, null is returned. If there are no entries, an empty
	 * {@link HashMap} is returned.
	 * @param file the filename where the setting can be found, without
	 * extension
	 */
	public HashMap<String, String> getAllSettings(String file) {
		HashMap<String, String> ret = new HashMap<>();
		Properties p = files.get(file);
		if (p != null) {
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String) (keys.nextElement());
				String value = p.getProperty(key);
				ret.put(key, value);
			}
		}
		return ret;
	}

	/**
	 * Convenience method: Gets the given setting as a int value, or use the
	 * given default value, if the is setting not available or readable.
	 */
	public int getSetting(String key, String file, int defaultValue) {
		String s = getSetting(key, file);
		if (s != null) {
			Integer ret = Parser.parseIntegerNull(s);
			if (ret != null) {
				return ret;
			}
		}
		return defaultValue;
	}

	/**
	 * Convenience method: Gets the given setting as a String value, or use the
	 * given default value, if the is setting not available or readable.
	 */
	public String getSetting(String key, String file, String defaultValue) {
		String s = getSetting(key, file);
		if (s != null) {
			return s;
		}
		return defaultValue;
	}

}
