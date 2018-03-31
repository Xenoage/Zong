package com.xenoage.utils.io.index;

import com.xenoage.utils.io.File;

/**
 * A {@link File} in the {@link FilesystemIndex}.
 * 
 * @author Andreas Wenger
 */
public class IndexedFile
	implements File {
	
	private String name;
	private Long sizeInBytes;


	public IndexedFile(String name, Long sizeInBytes) {
		this.name = name;
		this.sizeInBytes = sizeInBytes;
	}

	@Override public String getName() {
		return name;
	}

	@Override public Long getSizeInBytes() {
		return sizeInBytes;
	}
	
	

}
