package com.xenoage.utils.jse.lang;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.io.FileFilters.orFilter;
import static com.xenoage.utils.io.FileFilters.poFilter;
import static com.xenoage.utils.io.FileFilters.xmlFilter;
import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.log.Report.warning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.xml.XMLReader;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.lang.Language;

/**
 * This class reads a {@link Language} from a given directory.
 * The directory may contain .xml files or GNU gettext .po files, which are all read.
 * 
 * The format of the XML files is simple:
 * 
 * <pre>{@code <vocabulary>
 *   <voc key="About" value="Über"/>
 *   ...
 * </vocabulary> }</pre>
 * 
 * The format of the PO files is like in the
 * <a href="http://www.gnu.org/software/gettext/manual/html_node/PO-Files.html">documentation</a>,
 * but requires the usage of <code>msgctxt</code> for each entry, which is used as the
 * vocabulary key. The <code>msgid</code> is ignored and is only useful for human translators.
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class LanguageReader {

	/**
	 * Creates a {@link Language} from all .xml and .po files in the folder <code>basePath/id</code>.
	 * If the language pack can not be loaded, an {@link IOException} is thrown.
	 * @param path  path to the language pack directory (without trailing slash)
	 * @param id    id of the language pack
	 */
	public static Language read(String basePath, String id)
		throws IOException {
		log(remark("Loading language pack \"" + id + "\" from folder \"" + basePath + "\"..."));

		//check if language exists
		String dir = basePath + "/" + id;
		if (false == io().existsFile(dir + "/id.xml"))
			throw new FileNotFoundException("Language " + id + " does not exist");
		
		//locate vocabulary files
		List<String> langFiles = io().listFiles(dir, orFilter(xmlFilter, poFilter));
		langFiles.remove("id.xml");

		//load entries
		HashMap<String, String> entries = map();
		int entriesCount = 0;
		int entriesOverwrittenCount = 0;
		
		for (String langFileName : langFiles) {
			JseInputStream langStream = io().openFile(dir + "/" + langFileName);
			//read XML or PO file
			HashMap<String, String> fileEntries = null;
			if (langFileName.endsWith(".po")) {
				log(remark("Reading PO language file \"" + langFileName + "\""));
				fileEntries = readPO(langStream);
			}
			else {
				log(remark("Reading XML language file \"" + langFileName + "\""));
				fileEntries = readXML(langStream);
			}

			//insert vocabulary data
			for (Entry<String, String> fileEntry : fileEntries.entrySet()) {
				String oldValue = entries.put(fileEntry.getKey(), fileEntry.getValue());
				if (oldValue == null)
					entriesCount++;
				else {
					log(warning("Overwritten entry: " + fileEntry.getKey()));
					entriesOverwrittenCount++;
				}
			}
		}
		log(remark("Language pack loaded. Entries: " + entriesCount + ". Overwritten entries: " +
			entriesOverwrittenCount));

		//replace all tokens
		for (String key : entries.keySet()) {
			String value = entries.get(key);
			if (value.contains("{")) {
				entries.put(key, replaceTokens(value, entries));
			}
		}

		return new Language(id, entries);
	}

	/**
	 * Returns all key-value pairs from the given XML language file.
	 */
	private static HashMap<String, String> readXML(JseInputStream inputStream)
		throws IOException {
		Document doc = null;
		doc = XMLReader.readFile(inputStream);

		//check root element
		Element root = XMLReader.root(doc);
		if (!root.getNodeName().equals("vocabulary")) {
			//no vocabulary file.
			log(warning("No vocabulary file."));
			return null;
		}

		//read vocabulary data
		HashMap<String, String> entries = map();
		List<Element> eEntries = XMLReader.elements(root, "voc");
		for (Element e : eEntries) {
			String eID = XMLReader.attributeNotNull(e, "key");
			String eValue = XMLReader.attributeNotNull(e, "value");
			eValue = eValue.replaceAll("\\\\n", "\n");
			entries.put(eID, eValue);
		}

		return entries;
	}

	/**
	 * Returns all key-value pairs from the given PO language file.
	 */
	private static HashMap<String, String> readPO(JseInputStream inputStream)
		throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

		//read vocabulary data
		HashMap<String, String> entries = map();
		String line, key = null, original = null, value = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#") || line.length() == 0) {
				//finish entry, if one is open
				if (key != null && value != null)
					entries.put(key, value);
				key = null;
				original = null;
				value = null;
			}
			else {
				//begin or extend current entry
				if (line.startsWith("msgctxt "))
					key = val(line.substring("msgctxt ".length()));
				else if (line.startsWith("msgid "))
					original = val(line.substring("msgid ".length()));
				else if (line.startsWith("msgstr "))
					value = val(line.substring("msgstr ".length()));
				else if (line.startsWith("\"")) {
					String l = val(line); //another text line which belongs to the previous one
					if (value != null)
						value += l;
					else if (original != null)
						original += l;
					else if (key != null)
						key += l;
				}
			}
		}
		br.close();
		//finish entry, if one is open
		if (key != null && value != null)
			entries.put(key, value);

		return entries;
	}

	/**
	 * Removes quotes at the beginning and at the end, if there,
	 * and unescapes special characters.
	 */
	private static String val(String s) {
		s = s.trim();
		if (s.startsWith("\"") && s.endsWith("\""))
			s = s.substring(1, s.length() - 1);
		s = s.replaceAll("\\\"", "\"");
		s = s.replaceAll("\\\\n", "\n");
		return s;
	}

	/**
	 * Replaces the following tokens in the given String and returns the result:
	 * <ul>
	 * 	<li>All registered tokens from the {@link Lang} class</li>
	 * 	<li>{voc:xyz}: Inserts vocabulary with ID "xyz"</li>
	 * </ul>
	 */
	private static String replaceTokens(String s, Map<String, String> entries) {
		String ret = s;
		for (Tuple2<String, String> t : Lang.getTokens()) {
			ret = ret.replace(t.get1(), t.get2());
		}
		int pos;
		while ((pos = ret.indexOf("{voc:")) > -1) {
			int pos2 = ret.indexOf("}", pos + 1);
			if (pos2 > -1) {
				String id = ret.substring(pos + 5, pos2);
				ret = ret.replaceFirst("\\{voc:" + id + "\\}", notNull(entries.get(id), "?"));
			}
			else {
				ret = ret.replaceFirst("\\{voc:", "");
			}
		}
		return ret;
	}

}
