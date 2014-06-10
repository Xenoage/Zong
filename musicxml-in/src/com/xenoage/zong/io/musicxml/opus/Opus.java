package com.xenoage.zong.io.musicxml.opus;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.zong.io.musicxml.in.OpusLinkResolver;

/**
 * MusicXML opus, which can contain multiple scores and
 * also other opera.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class Opus
	implements OpusItem {

	private String title;
	private List<OpusItem> items;

	/**
	 * Gets a (flattened) list of all filenames in this opus. If this file
	 * contains no opus but a single score, the filename of the single score
	 * is returned.
	 * This links within this opus have to be resolved before to find
	 * all scores. This can be done by the {@link OpusLinkResolver}.
	 */
	public List<String> getScoreFilenames()
		throws IOException {
		ArrayList<String> ret = alist(items.size());
		getScoreFilenames(this, ret);
		return ret;
	}

	private void getScoreFilenames(Opus resolvedOpus, List<String> acc) {
		for (OpusItem item : resolvedOpus.getItems()) {
			if (item instanceof Score)
				acc.add(((Score) item).getLink().getHref());
			else if (item instanceof Opus)
				getScoreFilenames((Opus) item, acc);
		}
	}

}
