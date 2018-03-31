package com.xenoage.utils.jse.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for a Java SE {@link InputStream}.
 * 
 * It can be used in two ways: By wrapping a Java stream to a
 * Xenoage stream and vice versa.
 * 
 * @author Andreas Wenger
 */
public class JseInputStream
	extends InputStream
	implements com.xenoage.utils.io.InputStream {

	private InputStream jseInputStream = null;
	private com.xenoage.utils.io.InputStream genInputStream = null;


	public JseInputStream(InputStream jseInputStream) {
		this.jseInputStream = jseInputStream;
	}

	public JseInputStream(com.xenoage.utils.io.InputStream genInputStream) {
		this.genInputStream = genInputStream;
	}
	
	public JseInputStream(File file)
		throws FileNotFoundException {
		this.jseInputStream = new FileInputStream(file);
	}

	@Override public int read()
		throws IOException {
		if (jseInputStream != null)
			return jseInputStream.read();
		else
			return genInputStream.read();
	}
	
	@Override public int read(byte[] b)
		throws IOException {
		if (jseInputStream != null)
			return jseInputStream.read(b);
		else
			return genInputStream.read(b);
	}

	@Override public int read(byte[] b, int off, int len)
		throws IOException {
		if (jseInputStream != null)
			return jseInputStream.read(b, off, len);
		else
			return genInputStream.read(b, off, len);
	}

	@Override public void close() {
		if (jseInputStream != null) {
			try {
				jseInputStream.close();
			} catch (IOException e) {
				//ignore
			}
		}
		else {
			genInputStream.close();
		}
	}

}
