package com.xenoage.utils.gwt.io;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.gwt.GwtPlatformUtils;
import com.xenoage.utils.gwt.xml.GwtXmlReader;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.FilesystemItem;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.index.FilesystemIndex;
import com.xenoage.utils.io.index.FilesystemIndexReader;

/**
 * Some useful input methods for a GWT client application.
 * 
 * Use {@link GwtPlatformUtils#gwtIO()} to get an instance of this class.
 * 
 * Since HTTP provides no method to list files and directories, a
 * {@link FilesystemIndex} is used, stored in the file ".index" at the root.
 *
 * @author Andreas Wenger
 */
public class GwtIO
	implements FilesystemInput {
	
	public static final String indexFile = ".index";
	
	private FilesystemIndex index = null;
	
	
	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> callback) {
		GwtInputStream.open(filePath, callback);
	}

	/**
	 * {@inheritDoc}
	 * A file is only reported to exist, iff it is listed in the {@value #fileIndex} index.
	 */
	@Override public void existsFileAsync(final String filepath, final AsyncResult<Boolean> exists) {
		loadIndex(new AsyncCallback() {
			
			@Override public void onSuccess() {
				exists.onSuccess(index.existsFile(filepath));
			}
			
			@Override public void onFailure(Exception ex) {
				exists.onFailure(ex);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 * A directory is only reported to exist, iff it is listed in the {@value #dirIndex} index.
	 */
	@Override public void existsDirectoryAsync(final String dirpath, final AsyncResult<Boolean> exists) {
		loadIndex(new AsyncCallback() {
			
			@Override public void onSuccess() {
				exists.onSuccess(index.existsDirectory(dirpath));
			}
			
			@Override public void onFailure(Exception ex) {
				exists.onFailure(ex);
			}
		});
	};

	@Override public void listFilesAsync(final String dirpath, final AsyncResult<List<String>> files) {
		loadIndex(new AsyncCallback() {
			
			@Override public void onSuccess() {
				files.onSuccess(getNames(index.listFiles(dirpath)));
			}
			
			@Override public void onFailure(Exception ex) {
				files.onFailure(ex);
			}
		});
	}

	@Override public void listFilesAsync(final String dirpath, final FileFilter filter,
		final AsyncResult<List<String>> files) {
		loadIndex(new AsyncCallback() {
			
			@Override public void onSuccess() {
				files.onSuccess(getNames(index.listFiles(dirpath, filter)));
			}
			
			@Override public void onFailure(Exception ex) {
				files.onFailure(ex);
			}
		});
	}

	@Override public void listDirectoriesAsync(final String dirpath,
		final AsyncResult<List<String>> directories) {
		loadIndex(new AsyncCallback() {
			
			@Override public void onSuccess() {
				directories.onSuccess(getNames(index.listDirectories(dirpath)));
			}
			
			@Override public void onFailure(Exception ex) {
				directories.onFailure(ex);
			}
		});
	}
	
	private void loadIndex(final AsyncCallback initialized) {
		openFileAsync(FilesystemIndex.indexFile, new AsyncResult<InputStream>() {

			@Override public void onSuccess(InputStream indexStream) {
				try {
					GwtInputStream gwtStream = (GwtInputStream) indexStream;
					GwtIO.this.index = FilesystemIndexReader.read(new GwtXmlReader(gwtStream.getData()));
					initialized.onSuccess();
				} catch (IOException ex) {
					initialized.onFailure(ex);
				}
			}

			@Override public void onFailure(Exception ex) {
				initialized.onFailure(ex);
			}
		});
	}
	
	private List<String> getNames(List<? extends FilesystemItem> items) {
		List<String> ret = alist();
		for (FilesystemItem item : items)
			ret.add(item.getName());
		return ret;
	}

}
