package com.xenoage.utils.jse;

/**
 * Command line arguments.
 * 
 * @author Andreas Wenger
 */
public class CommandLine {

	private static String[] args = null;


	public static String[] getArgs() {
		return args;
	}

	public static void setArgs(String... args) {
		CommandLine.args = args;
	}
	
	public static boolean containsArg(String arg) {
		for (String s : args)
			if (s.equals(arg))
				return true;
		return false;
	}

}
