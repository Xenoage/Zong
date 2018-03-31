package com.xenoage.utils.jse.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Combobox which lists all installed fonts, sorted alphabetically. In the
 * dropdown, each font name is shown in the default font together with some
 * characters in its own font, which can be customized calling the
 * <code>setPreviewString</code> method.
 * 
 * In the main text field, the default font is used to display the font name. It
 * is editable and supports auto completion.
 * 
 * The last <code>n</code> selected fonts can be shown on the top by calling
 * <code>setRecentFontsCount(n)</code>.
 * 
 * This file is public domain. However, if you improve it, please share your
 * work with andi@xenoage.com. Thanks!
 * 
 * @author Andreas Wenger
 */
public class FontStyleChooserComboBox
	extends JComboBox<FontStyleChooserComboBox.Item> {

	private int previewFontSize;
	private String previewString = "AaBbCc";
	private int recentFontsCount = 5;

	private List<String> fontNames;
	private HashMap<String, Item> itemsCache = new HashMap<>();


	/**
	 * Creates a new {@link FontStyleChooserComboBox}.
	 */
	public FontStyleChooserComboBox() {
		// load available font names
		String[] fontNames = new String[] { "Plain", "Bold", "Italic", "Bold Italic" };
		Arrays.sort(fontNames);
		this.fontNames = Arrays.asList(fontNames);

		// fill combo box
		JLabel label = new JLabel();
		this.previewFontSize = label.getFont().getSize();
		updateList(null);

		// set editor and item components
		this.setEditable(true);
		this.setEditor(new FontChooserComboBoxEditor());
		this.setRenderer(new FontChooserComboBoxRenderer());

	}

	/**
	 * Gets the font size of the preview characters.
	 */
	public int getPreviewFontSize() {
		return previewFontSize;
	}

	/**
	 * Sets the font size of the preview characters.
	 */
	public void setPreviewFontSize(int previewFontSize) {
		this.previewFontSize = previewFontSize;
		updateList(getSelectedFontName());
	}

	/**
	 * Gets the preview characters, or null.
	 */
	public String getPreviewString() {
		return previewString;
	}

	/**
	 * Sets the preview characters, or the empty string or null to display no
	 * preview but only the font names.
	 */
	public void setPreviewString(String previewString) {
		this.previewString = (previewString != null && previewString.length() > 0 ? previewString
			: null);
		updateList(getSelectedFontName());
	}

	/**
	 * Gets the number of recently selected fonts, or 0.
	 */
	public int getRecentFontsCount() {
		return recentFontsCount;
	}

	private void updateList(String selectedFontName) {
		// list items
		removeAllItems();
		itemsCache.clear();

		// regular items
		for (String fontName : fontNames) {
			Item item = new Item(fontName);
			addItem(item);
			itemsCache.put(fontName, item);
		}
		// reselect item
		if (selectedFontName != null)
			setSelectedItem(selectedFontName);
	}

	/**
	 * Gets the selected font name, or null.
	 */
	public String getSelectedFontName() {
		if (this.getSelectedItem() != null)
			return ((Item) this.getSelectedItem()).font.getFontName();
		else
			return null;
	}

	@Override public Dimension getPreferredSize() {
		// default height: like a normal combo box
		return new Dimension(super.getPreferredSize().width,
			new JComboBox<Item>().getPreferredSize().height);
	}

	/**
	 * Sets the selected font by the given name. If it does not exist, nothing
	 * happens.
	 */
	public void setSelectedItem(String fontName) {

		Item item = itemsCache.get(fontName); // then in regular items
		if (item != null)
			setSelectedItem(item);
	}


	/**
	 * The editor component of the list. This is an editable text area which
	 * supports auto completion.
	 * 
	 * @author Andreas Wenger
	 */
	class FontChooserComboBoxEditor
		extends BasicComboBoxEditor {

		/**
		 * Plain text document for the text area. Needed for text selection.
		 * 
		 * Inspired by http://www.java2s.com/Code/Java/Swing-Components/
		 * AutocompleteComboBox.htm
		 * 
		 * @author Andreas Wenger
		 */
		class AutoCompletionDocument
			extends PlainDocument {

			private JTextField textField = FontChooserComboBoxEditor.this.editor;


			@Override public void replace(int i, int j, String s, AttributeSet attributeset)
				throws BadLocationException {
				super.remove(i, j);
				insertString(i, s, attributeset);
			}

			@Override public void insertString(int i, String s, AttributeSet attributeset)
				throws BadLocationException {
				if (s != null && !"".equals(s)) {
					String s1 = getText(0, i);
					String s2 = getMatch(s1 + s);
					int j = (i + s.length()) - 1;
					if (s2 == null) {
						s2 = getMatch(s1);
						j--;
					}
					if (s2 != null)
						FontStyleChooserComboBox.this.setSelectedItem(s2);
					super.remove(0, getLength());
					super.insertString(0, s2, attributeset);
					textField.setSelectionStart(j + 1);
					textField.setSelectionEnd(getLength());
				}
			}

			@Override public void remove(int i, int j)
				throws BadLocationException {
				int k = textField.getSelectionStart();
				if (k > 0)
					k--;
				String s = getMatch(getText(0, k));

				super.remove(0, getLength());
				super.insertString(0, s, null);

				if (s != null)
					FontStyleChooserComboBox.this.setSelectedItem(s);
				try {
					textField.setSelectionStart(k);
					textField.setSelectionEnd(getLength());
				} catch (Exception exception) {
				}
			}

		}


		private FontChooserComboBoxEditor() {
			editor.setDocument(new AutoCompletionDocument());
			if (fontNames.size() > 0)
				editor.setText(fontNames.get(0).toString());
		}

		private String getMatch(String input) {
			for (String fontName : fontNames) {
				if (fontName.toLowerCase().startsWith(input.toLowerCase()))
					return fontName;
			}
			return null;
		}

		public void replaceSelection(String s) {
			AutoCompletionDocument doc = (AutoCompletionDocument) editor.getDocument();
			try {
				Caret caret = editor.getCaret();
				int i = min(caret.getDot(), caret.getMark());
				int j = max(caret.getDot(), caret.getMark());
				doc.replace(i, j - i, s, null);
			} catch (BadLocationException ex) {
			}
		}
	}

	/**
	 * The renderer for a list item.
	 * 
	 * @author Andreas Wenger
	 */
	class FontChooserComboBoxRenderer
		implements ListCellRenderer<Item> {

		@Override public Component getListCellRendererComponent(JList<? extends Item> list, Item item,
			int index, boolean isSelected, boolean cellHasFocus) {
			// extract the component from the item's value
			boolean s = (isSelected && !item.isSeparator);
			item.setBackground(s ? list.getSelectionBackground() : list.getBackground());
			item.setForeground(s ? list.getSelectionForeground() : list.getForeground());
			return item;
		}
	}


	public Font changeStyle(String fontName) {
		Font font = null;
		if (fontName.toLowerCase().equals("plain")) {
			font = new JLabel().getFont().deriveFont(Font.PLAIN);
		}
		else if (fontName.toLowerCase().equals("bold")) {
			font = new JLabel().getFont().deriveFont(Font.BOLD);
		}
		else if (fontName.toLowerCase().equals("italic")) {
			font = new JLabel().getFont().deriveFont(Font.ITALIC);
		}
		else if (fontName.toLowerCase().equals("bold italic")) {
			font = new JLabel().getFont().deriveFont(Font.BOLD | Font.ITALIC);
		}
		return font;
	}


	/**
	 * The component for a list item.
	 * 
	 * @author Andreas Wenger
	 */
	class Item
		extends JPanel {

		private Font font;
		private final boolean isSeparator;


		private Item(String fontName) {
			if (fontName != null) {
				font = changeStyle(fontName);
				this.isSeparator = false;
			}
			else {
				this.font = null;
				this.isSeparator = true;
			}

			this.setOpaque(true);

			if (!isSeparator) {
				this.setLayout(new FlowLayout(FlowLayout.LEFT));

				JLabel labelFont = new JLabel(fontName);
				labelFont.setFont(font);
				this.add(labelFont);
			}
			else {
				// separator
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
}
