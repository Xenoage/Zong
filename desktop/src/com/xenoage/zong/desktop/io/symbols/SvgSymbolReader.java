package com.xenoage.zong.desktop.io.symbols;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;

/**
 * A {@link SvgSymbolReader} creates {@link Symbol}s from SVG files.
 *
 * @author Andreas Wenger
 */
public class SvgSymbolReader {

	/**
	 * Creates a {@link Symbol} from the given SVG file.
	 * If an error occurs, an {@link IllegalStateException} is thrown.
	 */
	public PathSymbol loadSymbol(String svgFilepath) {
		String id = FileUtils.getNameWithoutExt(svgFilepath);
		log(remark("Loading symbol \"" + id + "\", file: \"" + svgFilepath + "\" ..."));
		
		//open the file
		XmlReader xmlReader;
		try {
			xmlReader = platformUtils().createXmlReader(platformUtils().openFile(svgFilepath));
		} catch (Exception ex) {
			throw new IllegalStateException("Could not open XML file \"" + svgFilepath + "\"");
		}

		//read id element. it has the format "type:id", e.g.
		//"path:clef-g", or "styled:warning". If there is no ":",
		//the type "path" is used.
		//styles: path, styled, rect
		PathSymbol ret = null;
		if (xmlReader.openNextChildElement()) {
			String elementId = xmlReader.getAttribute("id");
			if (elementId == null || elementId.indexOf(':') == -1) {
				//no format information. use path.
				ret = SvgPathSymbolReader.read(id, xmlReader);
			}
			else {
				String format = elementId.split(":")[0];
				if (format.equals("path")) {
					ret = SvgPathSymbolReader.read(id, xmlReader);
				}
				else if (format.equals("rect")) {
					throw new IllegalStateException("Could not load \"" + svgFilepath + "\": \"" + format +
						"\" (rect is no longer supported. Convert it into a path)");
				}
				else if (format.equals("styled")) {
					throw new IllegalStateException("Could not load \"" + svgFilepath + "\": \"" + format +
						"\" (currently styled symbols are not supported)");
				}
				else {
					throw new IllegalStateException("Unknown symbol format in \"" + svgFilepath + "\": \"" +
						format + "\"");
				}
			}
		}
		xmlReader.close();

		return ret;
	}

}
