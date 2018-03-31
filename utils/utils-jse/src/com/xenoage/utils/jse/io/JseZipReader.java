package com.xenoage.utils.jse.io;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.io.FileNotFoundException;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.utils.jse.zip.ZipUtils;

/**
 * Java SE implementation of a {@link ZipReader}.
 * 
 * Currently, the data is stored in the main memory.
 * For bigger files this may be undesirable, so an extraction to
 * a temporary directory may be better. See {@link ZipUtils} in this case.
 * 
 * @author Andreas Wenger
 */
public class JseZipReader
	implements ZipReader {

	private final IList<String> files;
	private final Map<String, byte[]> filesData;


	/**
	 * Opens the ZIP file in the given stream.
	 */
	public JseZipReader(InputStream inputStream)
		throws IOException {
		byte[] buf = new byte[4096];
		ZipInputStream zipinputstream = new ZipInputStream(new JseInputStream(inputStream));
		ZipEntry zipentry;
		//extract all files
		CList<String> files = clist();
		Map<String, byte[]> filesData = map();
		while ((zipentry = zipinputstream.getNextEntry()) != null) {
			String entryName = zipentry.getName();
			if (false == zipentry.isDirectory()) {
				files.add(entryName);
				ByteArrayOutputStream fileData = new ByteArrayOutputStream();
				int n;
				while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
					fileData.write(buf, 0, n);
				fileData.close();
				zipinputstream.closeEntry();
				filesData.put(entryName, fileData.toByteArray());
			}
		}
		zipinputstream.close();
		this.files = files.close();
		this.filesData = filesData;
	}

	@Override public List<String> getFiles() {
		return files;
	}

	@Override public InputStream openFile(String file)
		throws FileNotFoundException {
		byte[] fileData = filesData.get(file);
		if (fileData == null)
			throw new FileNotFoundException(file);
		return new JseInputStream(new ByteArrayInputStream(fileData));
	}

	@Override public void close() {
		//nothing to do.
		//memory is cleaned up at finalization.
	}

}
