package com.xenoage.zong.webapp;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.gwt.error.GwtErrorProcessing;
import com.xenoage.utils.gwt.log.GwtLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Zong;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileReader;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.layouter.Target;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.stamping.StampingRenderer;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.webapp.renderer.gwt.canvas.GwtCanvas;
import com.xenoage.zong.webapp.utils.GwtZongPlatformUtils;

import java.io.IOException;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp
	implements EntryPoint {
	
	public static final String appFirstName = "WebApp";

	private Canvas canvas;
	private Context2d context;
	private GwtCanvas gwtCanvas;

	private ScoreDoc scoreDoc;
	private ScoreLayout scoreLayout;
	

	/**
	 * This is the entry point method.
	 */
	@Override public void onModuleLoad() {
		//init logging
		Log.init(new GwtLogProcessing(Zong.getNameAndVersion(appFirstName)));
		//init error handler
		Err.init(new GwtErrorProcessing());

		//init utils
		GwtZongPlatformUtils.init(new AsyncCallback() {

			@Override public void onSuccess() {
				step1_setup();
			}

			@Override public void onFailure(Exception ex) {
				log(fatal("Could not init platform utils", ex));
			}
		});
	}

	private void step1_setup() {
		RootPanel container = RootPanel.get("container");

		//add canvas
		canvas = Canvas.createIfSupported();
    if (canvas == null) {
    	container.add(new Label("Error: canvas not supported!"));
      return;
    }
    canvas.setWidth("100%");
    canvas.setHeight("100%");
		Window.addResizeHandler(e -> {
			paintLayout();
		});
    container.add(canvas);

    //load and layout MusicXML file
		platformUtils().openFileAsync("data/test/scores/musicxml20/BeetAnGeSample.xml")
			.thenAsync(scoreStream -> new MusicXmlScoreDocFileReader(scoreStream, null).read())
			.thenAsync(scoreDoc -> {
				WebApp.this.scoreDoc = scoreDoc;
				return platformUtils().openFileAsync("test.xml");
			})
			.thenDo(testXmlStream -> {
				LayoutSettings layoutSettings;
				try {
					layoutSettings = LayoutSettingsReader.read(testXmlStream);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				Size2f areaSize = new Size2f(150, 10000);
				SymbolPool symbolPool = zongPlatformUtils().getSymbolPool();
				Context context = new Context(scoreDoc.getScore(), symbolPool, layoutSettings);
				Target target = Target.completeLayoutTarget(new ScoreLayoutArea(areaSize));
				scoreLayout = new ScoreLayouter(context, target).createLayoutWithExceptions();
				paintLayout();
			})
			.onError(ex -> consoleLog("Error: " + ex.toString()));
	}

	private void paintLayout() {
		//update canvas coordinate space
		canvas.setCoordinateSpaceWidth(canvas.getElement().getClientWidth() * 2); //double resolution: smoother
		canvas.setCoordinateSpaceHeight(canvas.getElement().getClientHeight() * 2); //double resolution: smoother
		//paint score layout
		if (scoreLayout != null) {
			//draw in canvas
			context = canvas.getContext2d();
			context.scale(10, 10);
			setImageSmoothingEnabled(context);
			gwtCanvas = new GwtCanvas(canvas, CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect);
			Iterable<Stamping> stampings = scoreLayout.getScoreFrameLayout(0).getMusicalStampings();
			//render them
			SymbolPool symbolPool = zongPlatformUtils().getSymbolPool();
			RendererArgs args = new RendererArgs(1, 1, new Point2i(0, 0), symbolPool);
			for (Stamping s : stampings) {
				StampingRenderer.drawAny(s, gwtCanvas, args);
			}
		}
	}

	public static native void consoleLog(Object message) /*-{ //TIDY: move into GwtPlatformUtils
    console.log(message);
  }-*/;

	native void setImageSmoothingEnabled(Context2d context) /*-{
    context.imageSmoothingEnabled = true;
  }-*/;

}
