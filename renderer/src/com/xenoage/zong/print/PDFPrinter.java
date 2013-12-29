package com.xenoage.zong.print;

import static com.xenoage.utils.base.iterators.It.it;
import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.log.Report.warning;

import java.awt.Graphics2D;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.xenoage.utils.base.iterators.It;
import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.renderer.canvas.AWTCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.renderer.AWTPageLayoutRenderer;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;


/**
 * This class allows the user to print out
 * the current score into a PDF file.
 * 
 * The printing functions are the same as for printing out with
 * Java2D, but the target is iText instead of the printer driver.
 *
 * @author Andreas Wenger
 */
public final class PDFPrinter
{


	/**
	 * Prints the given {@link Layout} into the given PDF output stream.
	 */
	public static void print(Layout layout, OutputStream out)
	{

		Document document = new Document();
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (Exception e) {
			handle(warning(Voc.ErrorWhilePrinting));
		}

		document.open();
		PdfContentByte cb = writer.getDirectContent();
		AWTPageLayoutRenderer renderer = AWTPageLayoutRenderer.getInstance();

		It<Page> pages = it(layout.pages);
		for (Page page : pages) {
			//create PDF page
			Size2f pageSize = page.format.size;
			float width = Units.mmToPx(pageSize.width, 1);
			float height = Units.mmToPx(pageSize.height, 1);
			document.newPage();
			PdfTemplate tp = cb.createTemplate(width, height);
			//fill PDF page
			Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
			log(remark("Printing page " + pages.getIndex() + "..."));
			renderer.paint(layout, pages.getIndex(), new AWTCanvas(g2d, pageSize, CanvasFormat.Vector,
				CanvasDecoration.None, CanvasIntegrity.Perfect));
			//finish page
			g2d.dispose();
			cb.addTemplate(tp, 0, 0);
		}

		document.close();
	}


}
