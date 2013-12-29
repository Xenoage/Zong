package com.xenoage.zong.desktop.gui.components.util;

import javax.swing.text.AbstractDocument;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * @author Uli Teschemacher
 */
public class ScaledEditorKit
	extends StyledEditorKit {

	@Override public ViewFactory getViewFactory() {
		return new StyledViewFactory();
	}


	class StyledViewFactory
		implements ViewFactory {

		@Override public View create(Element elem) {
			String kind = elem.getName();
			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new LabelView(elem);
				}
				else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					return new ParagraphView(elem);
				}
				else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new ScaledView(elem, View.Y_AXIS);
				}
				else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				}
				else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			}

			// default to text display
			return new LabelView(elem);
		}

	}
}
