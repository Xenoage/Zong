package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.zong.io.musicxml.opus.Opus;
import com.xenoage.zong.io.musicxml.opus.OpusItem;
import com.xenoage.zong.io.musicxml.opus.OpusLink;

/**
 * Resolves all {@link OpusLink} items within the given {@link Opus} to
 * a new {@link Opus} and returns the result.
 * For a compressed MusicXML file, a {@link ZipReader} has to be given, otherwise
 * the given base path is used.
 * 
 * Reading is asynchronous, since multiple files may have to be opened
 * and this is only supported by non-blocking IO on all platforms.
 * 
 * @author Andreas Wenger
 */
public class OpusLinkResolver
	implements AsyncProducer<Opus> {

	//input
	private Opus opus;
	@MaybeNull private ZipReader zip;
	@MaybeNull private String basePath;

	//working data
	private List<OpusItem> input;
	private List<OpusItem> acc;
	private AsyncResult<Opus> callback;


	public OpusLinkResolver(Opus opus, ZipReader zip, String basePath) {
		this.opus = opus;
		this.zip = zip;
		this.basePath = basePath;
	}

	@Override public void produce(AsyncResult<Opus> callback) {
		this.input = alist(opus.getItems());
		this.acc = alist();
		this.callback = callback;
		processNextItem();
	}

	/**
	 * Processes the next opus item in the input queue, or finishes the processing
	 * if the queue is empty.
	 */
	private void processNextItem() {
		if (input.size() > 0) {
			//another item to resolve
			OpusItem inputItem = input.remove(0);
			if (inputItem instanceof OpusLink) {
				//opus link; must be resolved
				String filePath = ((OpusLink) inputItem).getLink().getHref();
				if (zip != null) {
					//read zipped opus file
					InputStream opusStream;
					try {
						opusStream = zip.openFile(filePath);
						resolveItem(opusStream);
					} catch (com.xenoage.utils.io.FileNotFoundException ex) {
						callback.onFailure(ex);
					}
				}
				else if (basePath != null) {
					//read plain opus file
					platformUtils().openFileAsync(basePath + "/" + filePath,
						new AsyncResult<InputStream>() {

							@Override public void onSuccess(InputStream opusStream) {
								resolveItem(opusStream);
							}

							@Override public void onFailure(Exception ex) {
								callback.onFailure(ex);
							}
						});
				}
				else {
					callback.onFailure(new IOException("neither zip nor basePath is given"));
				}
			}
			else if (inputItem instanceof Opus) {
				//opus; can contain links which must be resolved
				Opus childOpus = (Opus) inputItem;
				new OpusLinkResolver(childOpus, zip, basePath).produce(new AsyncResult<Opus>() {

					@Override public void onSuccess(Opus opus) {
						acc.add(opus);
						//item finished, next one
						processNextItem();
					}

					@Override public void onFailure(Exception ex) {
						callback.onFailure(ex);
					}
				});
			}
			else {
				//simple case. item needs not to be resolved, just add it
				acc.add(inputItem);
				processNextItem();
			}
		}
		else {
			//all items resolved
			callback.onSuccess(new Opus(opus.getTitle(), acc));
		}
	}

	private void resolveItem(InputStream stream) {
		//read opus
		Opus newOpus = null;
		try {
			newOpus = new OpusFileInput().readOpusFile(stream);
		} catch (Exception ex) {
			callback.onFailure(ex);
			return;
		}
		//this opus can again have links. resolve them recursively.
		new OpusLinkResolver(newOpus, zip, basePath).produce(new AsyncResult<Opus>() {

			@Override public void onSuccess(Opus opus) {
				acc.add(opus);
				//item finished, next one
				processNextItem();
			}

			@Override public void onFailure(Exception ex) {
				callback.onFailure(ex);
			}
		});
	}

}
