package com.xenoage.utils.gwt.io;

import java.io.IOException;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.InputStream;

/**
 * {@link InputStream} implementation of GWT.
 * 
 * Currently, this input stream only works with text files.
 * 
 * @author Andreas Wenger
 */
public class GwtInputStream
	implements InputStream {

	private String data;
	private ByteArrayInputStream stream;


	/**
	 * Opens the given file asynchronously.
	 * The given callback methods are used to indicate success or failure.
	 */
	public static void open(String file, final AsyncResult<InputStream> callback) {
		try {
			new RequestBuilder(RequestBuilder.GET, file).sendRequest("", new RequestCallback() {

				@Override public void onResponseReceived(Request req, Response resp) {
					try {
						if (resp.getStatusCode() == Response.SC_OK) {
							//file could be read. success.
							String data = resp.getText();
							ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes("UTF-8"));
							GwtInputStream result = new GwtInputStream(data, stream);
							callback.onSuccess(result);
						}
						else {
							callback.onFailure(new IOException("HTTP status code " + resp.getStatusCode()));
						}
					} catch (Exception ex) {
						callback.onFailure(new IOException(ex));
					}
				}

				@Override public void onError(Request res, Throwable throwable) {
					callback.onFailure(new IOException(throwable));
				}
			});
		} catch (RequestException ex) {
			callback.onFailure(new IOException(ex));
		}
	}

	private GwtInputStream(String data, ByteArrayInputStream stream) {
		this.data = data;
		this.stream = stream;
	}
	
	/**
	 * Gets the whole file data as a string.
	 */
	public String getData() {
		return data;
	}

	@Override public int read()
		throws IOException {
		return stream.read();
	}

	@Override public int read(byte... b)
		throws IOException {
		return stream.read(b, 0, b.length);
	}

	@Override public int read(byte[] b, int off, int len)
		throws IOException {
		return stream.read(b, off, len);
	}

	@Override public void close() {
		stream = null;
	}

}
