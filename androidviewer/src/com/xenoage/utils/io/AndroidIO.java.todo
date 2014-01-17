package com.xenoage.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.res.Resources;

import com.xenoage.utils.io.IOInterface;


/**
 * Input/output implementation for an Android device.
 *
 * @author Andreas Wenger
 */
public class AndroidIO
	implements IOInterface
{

	private Resources res;


	/**
	 * Creates a new input/output implementation
	 * for an Android device.
	 */
	public AndroidIO(Resources res)
	{
		this.res = res;
	}


	/**
	 * Returns true, when the given data file exists,
	 * otherwise false.
	 */
	@Override public boolean existsDataFile(String filepath)
	{
		try {
			res.getAssets().open(filepath);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}


	/**
	 * Returns true, when the given data directory exists,
	 * otherwise false.
	 */
	@Override public boolean existsDataDirectory(String directory)
	{
		try {
			res.getAssets().list(directory);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}


	/**
	 * Returns null, since an Android IO can not read file dates.
	 */
	@Override public Date getDataFileModificationDate(String filepath)
	{
		return null;
	}


	/**
	 * Opens and returns an input stream for the data file with the given path.
	 */
	@Override public InputStream openInputStream(String filepath)
		throws IOException
	{
		return res.getAssets().open(filepath);
	}


	/**
	 * Opens and returns an input stream for the data file with
	 * the given absolute or relative path. The path is guaranteed to
	 * be untouched (no automatic rerouting to home directory or something
	 * like that).
	 */
	@Override public InputStream openInputStreamPreservePath(String filepath)
		throws IOException
	{
		return new FileInputStream(filepath);
	}


	/**
	 * Throws an exception, since an Android IO may not write files.
	 */
	@Override public OutputStream openOutputStream(String filepath)
		throws IOException
	{
		throw new IOException("Android can not write files");
	}


	/**
	 * Does nothing, since files can not be deleted in the Android IO version.
	 */
	@Override public void deleteDataFile(String filepath, boolean system)
	{
	}


	/**
	 * Finds and returns the data files in the given directory.
	 */
	@Override public Set<String> listDataFiles(String directory)
		throws IOException
	{
		return listDataFiles(directory, null);
	}


	/**
	 * Finds and returns the data files in the given directory
	 * matching the given filename filter.
	 */
	@Override public Set<String> listDataFiles(String directory, FilenameFilter filter)
		throws IOException
	{
		Set<String> ret = new HashSet<String>();
		String[] files = res.getAssets().list(directory);
		for (String file : files) {
			if (filter == null || filter.accept(new File(directory), file))
				ret.add(file);
		}
		return ret;
	}


	/**
	 * Finds and returns the data directories in the given directory.
	 */
	@Override public Set<String> listDataDirectories(String directory)
		throws IOException
	{
		throw new IOException("Android can not list directories");
	}

}
