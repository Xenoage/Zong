package com.xenoage.zong.desktop.io.symbols;

import static com.xenoage.utils.jse.io.DesktopIO.desktopIO;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import java.awt.geom.GeneralPath;
import java.io.File;

import org.w3c.dom.Document;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.xml.XMLReader;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;

/**
 * A {@link SvgSymbolReader} creates {@link Symbol}s
 * from SVG files.
 *
 * @author Andreas Wenger
 */
public class SvgSymbolReader {

	/**
	 * Creates a {@link Symbol} from the given SVG file.
	 * If an error occurs, an {@link IllegalStateException} is thrown.
	 */
	public PathSymbol<GeneralPath> loadSymbol(String svgFilepath) {
		File svgFile = new File(svgFilepath);
		String id = JseFileUtils.getNameWithoutExt(svgFile);
		log(remark("Loading symbol \"" + id + "\", file: \"" + svgFilepath + "\" ..."));

		//open the file
		Document doc;
		try {
			doc = XMLReader.readFile(desktopIO().findFile(svgFilepath).getAbsolutePath());
		} catch (Exception ex) {
			throw new IllegalStateException("Could not read XML file \"" + svgFilepath + "\"");
		}

		//read id element. it has the format "type:id", e.g.
		//"path:clef-g", or "styled:warning". If there is no ":",
		//the type "path" is used.
		//styles: path, styled, rect
		PathSymbol<GeneralPath> ret = null;
		String elementID = XMLReader.attribute(XMLReader.root(doc), "id");
		if (elementID == null || elementID.indexOf(':') == -1) {
			//no format information. use path.
			ret = SvgPathSymbolReader.read(id, doc);
		}
		else {
			String format = elementID.split(":")[0];
			if (format.equals("path")) {
				ret = SvgPathSymbolReader.read(id, doc);
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

		return ret;
	}

}
