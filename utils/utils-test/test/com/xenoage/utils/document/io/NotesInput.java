package com.xenoage.utils.document.io;

import com.xenoage.utils.document.Notes;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.io.JseInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class read {@link Notes} from a text file.
 * 
 * @author Andreas Wenger
 */
public class NotesInput
	implements FileInput<Notes> {

	@Override public Notes read(InputStream stream, String filePath)
		throws InvalidFormatException, IOException {
		List<String> lines = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(new JseInputStream(stream)));
		String line = null;
		while ((line = in.readLine()) != null)
			lines.add(line);
		in.close();
		Notes ret = new Notes();
		ret.getLines().addAll(lines);
		return ret;
	}

}
