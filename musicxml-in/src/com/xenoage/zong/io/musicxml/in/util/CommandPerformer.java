package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.exceptions.CancelledException;
import com.xenoage.utils.document.exceptions.PropertyAlreadySetException;
import com.xenoage.utils.document.exceptions.UselessException;

/**
 * Runs {@link Command}s.
 * 
 * @author Andreas Wenger
 */
public class CommandPerformer {
	
	/**
	 * Executes the given {@link Command} and ignores
	 * {@link CancelledException}, {@link PropertyAlreadySetException} and {@link UselessException}.
	 */
	public static void execute(Command command) {
		try {
			command.execute();
		}
		catch (CancelledException ex) {
		}
		catch (PropertyAlreadySetException ex) {
		}
		catch (UselessException ex) {
		}
	}

}
