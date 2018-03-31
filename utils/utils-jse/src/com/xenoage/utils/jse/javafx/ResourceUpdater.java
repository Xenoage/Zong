package com.xenoage.utils.jse.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;

import javafx.scene.Scene;

/**
 * This class reloads the text properties of internationalized strings
 * on a FXML file for a given {@link Scene}.
 * Only nodes whose "fx:id" is set and who are named in the are supported.
 * 
 * @author Andreas Wenger
 */
public class ResourceUpdater {
	
	public static void updateScene(Dialog controller, InputStream fxmlStream, ResourceBundle resources)
		throws IOException {
		//parse FXML file. collect elements which have an id and a text starting with "%".
		Document fxmlDoc = XMLReader.readFile(fxmlStream);
		HashMap<String, String> nodes = new HashMap<>();
		collectTexts(XMLReader.root(fxmlDoc), nodes);
		//apply new texts
		for (String nodeId : nodes.keySet()) {
			String textId = nodes.get(nodeId);
			String text = null;
			if (resources.containsKey(textId))
				text = resources.getString(textId);
			if (text != null) {
				try {
					Field field = controller.getClass().getDeclaredField(nodeId);
					field.setAccessible(true); //make accessible, if needed
					Object node = field.get(controller);
					node.getClass().getMethod("setText", String.class).invoke(node, text);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	private static void collectTexts(Element element, HashMap<String, String> acc) {
		//read id and text
		if (element.hasAttribute("fx:id") && element.hasAttribute("text")) {
			String text = element.getAttribute("text");
			if (text.startsWith("%"))
				acc.put(element.getAttribute("fx:id"), text.substring(1));
		}
		//read children
		for (Element e : XMLReader.elements(element)) {
			collectTexts(e, acc);
		}
	}

}
