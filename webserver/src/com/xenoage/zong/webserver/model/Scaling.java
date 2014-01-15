package com.xenoage.zong.webserver.model;

import static com.xenoage.utils.jse.xml.XMLReader.attribute;
import static com.xenoage.zong.webserver.util.Parser.getInt;
import static com.xenoage.zong.webserver.util.Parser.getIntAttr;

import java.util.Map;

import javax.servlet.ServletException;

import org.w3c.dom.Element;

import com.xenoage.utils.math.geom.Size2f;

/**
 * A scaling factor. There are 4 different types of scalings:
 * 
 * <ul>
 * 	<li>{@code <scaling dpi="72"/>} renders this scaling with 72 dpi (was scaling=10000 before)</li>
 * 	<li>{@code <scaling widthpx="500"/>} finds a scaling so that the first page has a width of 500 px</li>
 * 	<li>{@code <scaling heightpx="800"/>} finds a scaling so that the first page has a height of 800 px</li>
 * 	<li>{@code <scaling widthpx="500" heightpx="800"/>} finds a scaling so that the first page fits
 * 		into 500 px width AND 800 px height (the smaller scaling factor is chosen to guarantee that the whole page fits into the given area)</li>
 * </ul>
 * 
 * @author Andreas Wenger
 */
public class Scaling {

	private final Integer dpi, widthpx, heightpx;

	private static final int maxScaling10000 = 100000; //1000% zoom is maximum


	public Scaling(Integer dpi, Integer widthpx, Integer heightpx)
		throws ServletException {
		this.dpi = dpi;
		this.widthpx = widthpx;
		this.heightpx = heightpx;
		//check combinations
		if (!((dpi != null && widthpx == null && heightpx == null) || (dpi == null && (widthpx != null || heightpx != null))))
			throw new ServletException("Invalid combination of scaling types");
	}

	public static Scaling fromXML(Element e)
		throws ServletException {
		Integer dpi = (attribute(e, "dpi") != null ? getIntAttr(e, "dpi") : null);
		Integer widthpx = (attribute(e, "widthpx") != null ? getIntAttr(e, "widthpx") : null);
		Integer heightpx = (attribute(e, "heightpx") != null ? getIntAttr(e, "heightpx") : null);
		return new Scaling(dpi, widthpx, heightpx);
	}

	public static Scaling fromParams(Map<String, String[]> params)
		throws ServletException {
		Integer dpi = (params.get("scaling_dpi") != null ? getInt(params, "scaling_dpi") : null);
		Integer widthpx = (params.get("scaling_widthpx") != null ? getInt(params, "scaling_widthpx")
			: null);
		Integer heightpx = (params.get("scaling_heightpx") != null ? getInt(params, "scaling_heightpx")
			: null);
		return new Scaling(dpi, widthpx, heightpx);
	}

	/**
	 * Converts this scaling into return*72dpi/10000 space
	 * (e.g. return=5000 means 36dpi).
	 */
	public int convertTo10000(Size2f firstPageSize) {
		//easy case: dpi
		if (dpi != null)
			return dpi * 10000 / 72;
		//width and height
		int widthScaling = maxScaling10000;
		int heightScaling = maxScaling10000;
		if (widthpx != null)
			widthScaling = (int) (10000 * widthpx / firstPageSize.width * (25.4f / 72));
		if (heightpx != null)
			heightScaling = (int) (10000 * widthpx / firstPageSize.width * (25.4f / 72));
		return Math.min(widthScaling, heightScaling);
	}

}
