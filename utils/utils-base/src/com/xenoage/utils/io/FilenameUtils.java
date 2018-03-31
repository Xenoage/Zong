package com.xenoage.utils.io;

import static com.xenoage.utils.StringUtils.fillIntDigits;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

import java.util.List;

/**
 * Useful methods to work with filenames.
 * 
 * @author Andreas Wenger
 */
public class FilenameUtils {

	/**
	 * Returns numbered filenames for the given original filename.
	 * If there is only one file, the original name is returned.
	 * If there are more, numbers (starting with 1) are inserted before
	 * the extension, if there is one (name-1.xml, name-2.xml, ...),
	 * or at the end if there is no extension (name-1, name-2).
	 * A filename with a single dot at the beginning (like ".htaccess")
	 * is not seen as an extension.
	 * If there are 10 or more files, the number has two digits
	 * (name-01.xml, ..., name-99.xml), if there are hundred or more,
	 * the number has three digits (name-001.xml, ..., name-999.xml)
	 * and so on.
	 * This method is aware of directories: If the original name has
	 * a "/" or "\" in it, only the part behind its last appearance
	 * is seen as a filename. The returned file names contain the
	 * directories again.
	 */
	public static List<String> numberFiles(String originalName, int count) {
		//simple case
		if (count <= 1)
			return alist(originalName);
		//try to split at last "/" and extension
		String dirPart = "", namePart = originalName, extPart = "";
		int dirPos = Math.max(originalName.lastIndexOf('/'), originalName.lastIndexOf('\\'));
		if (dirPos > -1) {
			dirPart = originalName.substring(0, dirPos + 1);
			namePart = originalName.substring(dirPos + 1);
		}
		int lastDotPos = namePart.lastIndexOf('.');
		if (lastDotPos > 0) {
			extPart = namePart.substring(lastDotPos);
			namePart = namePart.substring(0, lastDotPos);
		}
		//number of digits
		int digits = 1, tmp = count;
		while (tmp > 9) {
			tmp /= 10;
			digits++;
		}
		//create results
		List<String> ret = alist(count);
		for (int i : range(count))
			ret.add(dirPart + namePart + "-" + fillIntDigits(i + 1, digits) + extPart);
		return ret;
	}

}
