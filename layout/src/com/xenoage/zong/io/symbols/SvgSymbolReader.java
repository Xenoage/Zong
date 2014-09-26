package com.xenoage.zong.io.symbols;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;

/**
 * A {@link SvgSymbolReader} creates a {@link Symbol} from a SVG file.
 *
 * @author Andreas Wenger
 */
class SvgSymbolReader
	implements AsyncProducer<PathSymbol> {
	
	//parameters
	private final String id;
	private final String svgFilepath;
	
	//state
	private AsyncResult<PathSymbol> result;
	
	public SvgSymbolReader(String svgFilepath) {
		this.svgFilepath = svgFilepath;
		this.id = FileUtils.getNameWithoutExt(svgFilepath);
	}

	/**
	 * Creates a {@link Symbol} from the given SVG file.
	 * If an error occurs, an {@link IllegalStateException} is thrown.
	 */
	@Override public void produce(AsyncResult<PathSymbol> result) {
		this.result = result;
		step1_openFile();
	}
	
	private void step1_openFile() {
		log(remark("Loading symbol \"" + id + "\", file: \"" + svgFilepath + "\" ..."));
		platformUtils().openFileAsync(svgFilepath, new AsyncResult<InputStream>() {

			@Override public void onSuccess(InputStream stream) {
				step2_readFile(stream);
			}

			@Override public void onFailure(Exception ex) {
				result.onFailure(ex);
			}
		});
	}
	
	private void step2_readFile(InputStream stream) {
		//create xml reader
		XmlReader xmlReader = platformUtils().createXmlReader(stream);

		//read id element. it has the format "type:id", e.g.
		//"path:clef-g", or "styled:warning". If there is no ":",
		//the type "path" is used.
		//styles: path, styled, rect
		PathSymbol ret = null;
		Exception ex = null;
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
					ex = new IllegalStateException("Could not load \"" + svgFilepath + "\": \"" +
						format + "\" (rect is no longer supported. Convert it into a path)");
				}
				else if (format.equals("styled")) {
					ex = new IllegalStateException("Could not load \"" + svgFilepath + "\": \"" +
						format + "\" (currently styled symbols are not supported)");
				}
				else {
					ex = new IllegalStateException("Unknown symbol format in \"" + svgFilepath +
						"\": \"" + format + "\"");
				}
			}
		}
		xmlReader.close();

		if (ex != null)
			result.onFailure(ex);
		else
			result.onSuccess(ret);
	}

}
