package com.xenoage.zong.webapp.symbols;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.zong.webapp.symbols.path.PathElement;

/**
 * Container for path data.
 * 
 * We need this class, since HTML5 does not provide a path class,
 * but instead provides low level drawing methods directly on the canvas.
 * 
 * @author Andreas Wenger
 */
public class GwtPathSymbol {
	
	/** The list of path drawing commands. */
	private List<PathElement> path = alist();
	
	public void add(PathElement pathElement) {
		path.add(pathElement);
	}
	
	public void draw(Context2d context) {
		for (PathElement pathElement : path)
			pathElement.draw(context);
	}

}
