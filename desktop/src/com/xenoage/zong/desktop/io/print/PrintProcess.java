package com.xenoage.zong.desktop.io.print;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.utils.log.Report.remark;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Voc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.renderer.LayoutRenderer;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

/**
 * This class allows the user to print out the current score.
 * 
 * The whole process (from the printer dialog to the actual printing)
 * is implemented in this class.
 *
 * @author Andreas Wenger
 */
public final class PrintProcess
	implements Pageable, Printable {

	private Layout layout;


	/**
	 * Starts a print process.
	 * 
	 * First a print dialog is shown, where the user
	 * can select a printer, the range of pages and
	 * the number of copies.
	 * The user can cancel the process here.
	 * 
	 * If he continues, the selected pages are printed.
	 * Thus this method needs a PageLayout instance.
	 */
	public void requestPrint(Layout layout) {
		this.layout = layout;

		//show print dialog
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setJobName("Score Document");
		pj.setPrintable(this);
		pj.setPageable(this);
		try {
			log(remark("Opening print dialog..."));
			pj.setCopies(1);
			if (pj.printDialog()) {
				log(remark("Starting print..."));
				pj.print();
			}
		} catch (PrinterException ex) {
			handle(error(Voc.ErrorWhilePrinting, ex));
		}
	}

	@Override public int getNumberOfPages() {
		return layout.getPages().size();
	}

	@Override public PageFormat getPageFormat(int pageIndex)
		throws IndexOutOfBoundsException {
		com.xenoage.zong.core.format.PageFormat pageFormat = layout.getPages().get(pageIndex).getFormat();
		PageFormat ret = new PageFormat();
		ret.setOrientation(PageFormat.PORTRAIT);
		//set paper size
		Paper paper = new Paper();
		paper.setSize(Units.mmToPx(pageFormat.getSize().width, 1), Units.mmToPx(pageFormat.getSize().height, 1));
		//no margins TEST: 10 mm
		paper.setImageableArea(10, 10, Units.mmToPx(pageFormat.getSize().width, 1) - 20,
			Units.mmToPx(pageFormat.getSize().height, 1) - 20);
		ret.setPaper(paper);
		return ret;
	}

	@Override public Printable getPrintable(int pageIndex)
		throws IndexOutOfBoundsException {
		return this;
	}

	@Override public int print(Graphics g, PageFormat pageFormat, int pageIndex)
		throws PrinterException {
		//valid page number?
		if (pageIndex < 0 || pageIndex >= layout.getPages().size()) {
			return Printable.NO_SUCH_PAGE;
		}

		//print page
		log(remark("Printing page " + pageIndex + "..."));
		Graphics2D g2d = (Graphics2D) g;
		Size2f pageSize = layout.getPages().get(pageIndex).getFormat().getSize();
		LayoutRenderer.paintToCanvas(layout, pageIndex, new AwtCanvas(g2d, pageSize, CanvasFormat.Vector,
			CanvasDecoration.None, CanvasIntegrity.Perfect));

		//DEMO
		/*
		com.xenoage.zong.data.format.PageFormat pf = layout.getPages().get(pageIndex).getFormat();
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(1));
		float w = Units.mmToPx(pf.getSize().width, 1);
		float h = Units.mmToPx(pf.getSize().height, 1);
		float margin = Units.mmToPx(10, 1);
		float x1 = margin;
		float y1 = margin;
		float x2 = w - margin;
		float y2 = h - margin;
		g2d.draw(new Rectangle2D.Float(x1, y1, x2 - margin, y2 - margin));
		g2d.draw(new Line2D.Float(x1, y1, x2, y2));
		g2d.draw(new Line2D.Float(x2, y1, x1, y2));
		*/

		return Printable.PAGE_EXISTS;
	}

}
