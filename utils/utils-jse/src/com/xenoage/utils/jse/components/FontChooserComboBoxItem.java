package com.xenoage.utils.jse.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * The component for a list item.
 * 
 * @author Andreas Wenger
 */
public class FontChooserComboBoxItem
	extends JPanel {

	public final Font font;
	public final boolean isSeparator;


	public FontChooserComboBoxItem(String fontName, int previewFontSize, String previewString) {
		if (fontName != null) {
			this.font = new Font(fontName, Font.PLAIN, previewFontSize);
			this.isSeparator = false;
		}
		else {
			this.font = null;
			this.isSeparator = true;
		}

		this.setOpaque(true);

		if (!isSeparator) {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));

			//font name in default font
			JLabel labelHelp = new JLabel(font.getName());
			this.add(labelHelp);

			//preview string in this font
			if (previewString != null) {
				//show only supported characters
				StringBuilder thisPreview = new StringBuilder();
				for (int i = 0; i < previewString.length(); i++) {
					char c = previewString.charAt(i);
					if (font.canDisplay(c))
						thisPreview.append(c);
				}
				JLabel labelFont = new JLabel(thisPreview.toString());
				labelFont.setFont(font);
				this.add(labelFont);
			}
		}
		else {
			//separator
			this.setLayout(new BorderLayout());
			this.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);
		}
	}

	@Override public String toString() {
		if (font != null)
			return font.getFamily();
		else
			return "";
	}

}
