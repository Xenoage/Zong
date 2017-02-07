package com.xenoage.zong.webapp;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * This class is exposed to JavaScript and can be used
 * to control the Zong! WebApp, e.g. by selecting a score,
 * selecting a page, zooming, and so on.
 *
 * @author Andreas Wenger
 */
@JsType(namespace = JsPackage.GLOBAL)
public class ZongControl {

	public static void zoomIn() {
		WebApp.instance.zoomIn();
	}

	public static void zoomOut() {
		WebApp.instance.zoomOut();
	}

	public static void openDemo(int id) {
		WebApp.instance.openDemo(id);
	}

}
