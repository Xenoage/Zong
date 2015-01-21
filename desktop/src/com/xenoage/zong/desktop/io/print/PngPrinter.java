package com.xenoage.zong.desktop.io.print;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.renderer.awt.AwtLayoutRenderer;

/**
 * This class allows to print out the current score into a PNG file.
 *
 * @author Andreas Wenger
 */
public final class PngPrinter {

	/**
	 * Prints the given page of the given {@link Layout} into the given PNG {@link File}.
	 */
	public static void print(Layout layout, int pageIndex, OutputStream out) {
		//create image
		BufferedImage img = AwtLayoutRenderer.paintToImage(layout, pageIndex, 1f);

		//save file
		try {
			ImageIO.write(img, "png", out);
		} catch (IOException ex) {
			handle(warning("could not print to PNG file"));
		}
	}

}
