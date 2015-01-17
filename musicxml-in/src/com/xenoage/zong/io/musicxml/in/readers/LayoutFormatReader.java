package com.xenoage.zong.io.musicxml.in.readers;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.PageMargins;
import com.xenoage.zong.musicxml.types.MxlPageLayout;
import com.xenoage.zong.musicxml.types.MxlPageMargins;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;

/**
 * Reads a {@link LayoutFormat} from a {@link MxlLayout}.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class LayoutFormatReader {

	@MaybeNull private final MxlLayout mxlLayout;
	private final float tenthsMm;
	
	private LayoutFormat layoutFormat;


	@NonNull public LayoutFormat read() {
		layoutFormat = LayoutFormat.defaultValue;
		if (mxlLayout != null)
			readPageLayout();
		return layoutFormat;
	}

	private void readPageLayout() {
		MxlPageLayout mxlPageLayout = mxlLayout.getPageLayout();
		if (mxlPageLayout == null)
			return;
		
		Size2f size = PageFormat.defaultValue.getSize();

		//page-width and page-height
		Size2f mxlPageSize = mxlPageLayout.getPageSize();
		if (mxlPageSize != null)
			size = new Size2f(tenthsMm * mxlPageSize.width, tenthsMm * mxlPageSize.height);

		//page-margins
		PageMargins pageMarginsLeft = PageMargins.defaultValue;
		PageMargins pageMarginsRight = PageMargins.defaultValue;
		for (MxlPageMargins mxlMargins : mxlPageLayout.getPageMargins()) {
			PageMargins pageMargins = new PageMargins(
				tenthsMm * mxlMargins.getLeftMargin(),
				tenthsMm * mxlMargins.getRightMargin(),
				tenthsMm * mxlMargins.getTopMargin(),
				tenthsMm * mxlMargins.getBottomMargin());
			//left, right page or both? default: both
			switch (mxlMargins.getType()) {
				case Both:
					pageMarginsLeft = pageMargins;
					pageMarginsRight = pageMargins;
					break;
				case Odd:
					pageMarginsRight = pageMargins;
					break;
				case Even:
					pageMarginsRight = pageMargins;
					break;
			}
		}

		layoutFormat = new LayoutFormat(
			new PageFormat(size, pageMarginsLeft),
			new PageFormat(size, pageMarginsRight));
	}

}
