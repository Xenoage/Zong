package com.xenoage.zong.musicxml.types;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML credit-words content for the credit element.
 * 
 * Here, the sequence of several credit-words elements
 * is accumulated to a single class instance, with multiple
 * {@link MxlFormattedText} instances instead.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "link,bookmark", partly = "credit-words")
@AllArgsConstructor @Getter @Setter
public final class MxlCreditWords
	implements MxlCreditContent {
	
	public static final String elemName = "credit-words";

	@NonEmpty private final List<MxlFormattedText> items;


	@Override public MxlCreditContentType getCreditContentType() {
		return MxlCreditContentType.CreditWords;
	}

	/**
	 * Reads the current element, and all of its following siblings.
	 */
	@NonNull public static MxlCreditWords read(XmlReader reader) {
		List<MxlFormattedText> items = CollectionUtils.alist();
		do {
			String n = reader.getElementName();
			if (n.equals(elemName))
				items.add(MxlFormattedText.read(reader));
			reader.closeElement();
		} while (reader.openNextChildElement());
		if (items.size() < 1)
			throw reader.dataException("no " + elemName + " found");
		return new MxlCreditWords(items);
	}

	@Override public void write(XmlWriter writer) {
		for (MxlFormattedText item : items) {
			writer.writeElementStart(elemName);
			item.write(writer);
			writer.writeElementEnd();
		}
	}

}
