package com.xenoage.zong.webapp;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.gwt.error.GwtErrorProcessing;
import com.xenoage.utils.gwt.log.GwtLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.Zong;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileReader;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.Target;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.renderer.gwtcanvas.GwtCanvasLayoutRenderer;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.webapp.utils.GwtZongPlatformUtils;

import java.io.IOException;

import static com.google.gwt.dom.client.Style.Display.BLOCK;
import static com.google.gwt.dom.client.Style.Display.NONE;
import static com.google.gwt.dom.client.Style.Unit.PX;
import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.gwt.GwtPlatformUtils.consoleLog;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.utils.math.MathUtils.clamp;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;


/**
 * HTML5/JS based version of a Zong! score viewer, based on GWT.
 *
 * TODO: ZONG-106: SVG output in webapp
 *
 * @author Andreas Wenger
 */
public class WebApp
	implements EntryPoint {
	
	public static final String appFirstName = "WebApp";

	public static WebApp instance;

	private RootPanel container;
	private Element loadingPanel;
	private Canvas canvas;
	private Context2d context;

	private ScoreDoc scoreDoc;

	private float zoom = 1;
	

	/**
	 * This is the entry point method.
	 */
	@Override public void onModuleLoad() {
		instance = this;

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
		//find the container and loading panel
		container = RootPanel.get("ZongView");
		loadingPanel = DOM.getElementById("ZongLoading");

		//create and add canvas
		canvas = Canvas.createIfSupported();
    if (canvas == null) {
    	container.add(new Label("Error: canvas not supported!"));
      return;
    }
    canvas.setWidth("800px");
    canvas.setHeight("600px");
		Window.addResizeHandler(e -> {
			paintLayout();
		});
		container.add(canvas);

    //load and layout MusicXML file
		openDemo(1);
	}

	private void openFile(String filePath) {
		//show loading, hide score
		if (loadingPanel != null) loadingPanel.getStyle().setDisplay(BLOCK);
		canvas.setVisible(false);
		//load score
		platformUtils().openFileAsync(filePath)
			.thenAsync(scoreStream -> new MusicXmlScoreDocFileReader(scoreStream, null).read())
			.thenAsync(scoreDoc -> {
				WebApp.this.scoreDoc = scoreDoc;
				return platformUtils().openFileAsync("data/layout/default.xml");
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
				paintLayout();
				//show score, hide loading
				canvas.setVisible(true);
				if (loadingPanel != null) loadingPanel.getStyle().setDisplay(NONE);
			})
			.onError(ex -> consoleLog("Error: " + ex.toString()));
	}

	private void paintLayout() {
		if (scoreDoc != null) {
			Size2i size = GwtCanvasLayoutRenderer.paintToCanvas(scoreDoc.getLayout(), 0, zoom, canvas);
			//also resize the loading panel to the size of the score
			if (loadingPanel != null) {
				loadingPanel.getStyle().setWidth(size.width, PX);
				loadingPanel.getStyle().setHeight(size.height, PX);
			}
		}
	}

	public void openDemo(int id) {
		String file = null;
		if (id == 1) file = "data/test/scores/musicxml20/BeetAnGeSample.xml";
		if (id == 2) file = "data/test/scores/musicxml20/BrahWiMeSample.xml";
		if (id == 3) file = "data/test/scores/musicxml20/MahlFaGe4Sample.xml";
		if (file != null)
			openFile(file);
	}

	public void zoomIn() {
		zoom(1.3f);
	}

	public void zoomOut() {
		zoom(1 / 1.3f);
	}

	private void zoom(float factor) {
		zoom = clamp(zoom * factor, 0.2f, 4f);
		paintLayout();
	}

}
