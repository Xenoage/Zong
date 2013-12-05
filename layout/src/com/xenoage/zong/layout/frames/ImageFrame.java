package com.xenoage.zong.layout.frames;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * An {@link ImageFrame} is a frame that contains
 * a bitmap image.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper = true) public class ImageFrame
	extends Frame {

	/** The path of the image. */
	private String imagePath;


	@Override public FrameType getType() {
		return FrameType.ImageFrame;
	}

}
