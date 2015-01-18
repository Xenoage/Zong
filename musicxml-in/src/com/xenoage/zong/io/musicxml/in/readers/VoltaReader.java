package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.kernel.Range.range;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.musicxml.in.util.ClosedVolta;
import com.xenoage.zong.musicxml.types.MxlEnding;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopDiscontinue;

/**
 * Reads a {@link Volta} from a {@link MxlEnding}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class VoltaReader {

	private final MxlEnding mxlEnding;
	
	
	public void readToContext(Context context) {
		MxlStartStopDiscontinue type = mxlEnding.getType();
		if (type == MxlStartStopDiscontinue.Start) {
			Range range = readEndingRange(mxlEnding.getNumber());
			context.openVolta(range, null);
		}
		else if (type == MxlStartStopDiscontinue.Stop || type == MxlStartStopDiscontinue.Discontinue) {
			boolean rightHook = (type == MxlStartStopDiscontinue.Stop);
			ClosedVolta closedVolta = context.closeVolta(rightHook);
			if (closedVolta != null)
				context.writeColumnElement(closedVolta.volta, closedVolta.measure);
		}
	}
	
	private Range readEndingRange(String number) {
		number = number.trim();
		if (number.length() == 0)
			return null;
		//we allow only consecutive endings, so find minimum and maximum value
		String[] numbers = number.split(",");
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (String n : numbers) {
			int i = Integer.parseInt(n.trim());
			min = Math.min(min, i);
			max = Math.max(max, i);
		}
		return range(min, max);
	}
	
}
