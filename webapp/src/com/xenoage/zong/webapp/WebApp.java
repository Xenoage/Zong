package com.xenoage.zong.webapp;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.gwt.error.GwtErrorProcessing;
import com.xenoage.utils.gwt.io.GwtInputStream;
import com.xenoage.utils.gwt.log.GwtLogProcessing;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.log.Log;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
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
import com.xenoage.zong.utils.demo.ScoreRevolutionary;
import com.xenoage.zong.webapp.renderer.gwt.canvas.GwtCanvas;
import com.xenoage.zong.webapp.utils.GwtZongPlatformUtils;

import java.io.IOException;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.utils.math.Fraction._0;
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
		//test core
		final Score score = ScoreRevolutionary.createScore();
		//String t = score.getClef(MP.atBeat(0, 1, 0, _0), Interval.BeforeOrAt).getType().toString() + " found";
		//Clef clef = new Clef(ClefType.G);
		//String t = clef.getType().toString();
		//String t = new Point2f(5, 10).toString();
		//Range r = Range.range(5);
		//Label hello = new Label("Result: " + t); //"Aha "+r.getCount() + " " + Defaults.defaultFont.getSize() + " " + t);
		RootPanel container = RootPanel.get("container");
		container.add(new Label("If you see some text with musical data, it works:"));
		container.add(new Label(findAClef(score, MP.atBeat(0, 1, 0, _0))));
		container.add(new Label(findAClef(score, MP.atBeat(1, 1, 0, _0))));
		MP mp = MP.atBeat(0, 1, 0, _0);
		container.add(new Label("Voice at " + mp + ": " + score.getVoice(mp)));


		//Test GWT IO
		try {
			container.add(new Label("File content:"));
			final Label lblData = new Label("Loading...");
			container.add(lblData);
			platformUtils().openFileAsync("test.txt", new AsyncResult<InputStream>() {

				@Override public void onSuccess(InputStream data) {
					lblData.setText(((GwtInputStream) data).getData());
				}

				@Override public void onFailure(Exception ex) {
					lblData.setText("File error: " + ex.toString());
				}
			});
		} catch (Exception ex) {
			container.add(new Label("File error: " + ex.toString()));
		}

		//test XML reading
		try {
			container.add(new Label("XML content:"));
			final Label lblData = new Label("Loading...");
			container.add(lblData);
			platformUtils().openFileAsync("test.xml", new AsyncResult<InputStream>() {

				@Override public void onSuccess(InputStream data) {
					try {
						LayoutSettings layoutSettings = LayoutSettingsReader.read(data);
						lblData.setText("grace scaling: " + layoutSettings.scalingGrace);
					} catch (IOException ex) {
						lblData.setText("XML error: " + ex.toString());
					}
				}

				@Override public void onFailure(Exception ex) {
					lblData.setText("XML error: " + ex.toString());
				}
			});
		} catch (Exception ex) {
			container.add(new Label("XML error: " + ex.toString()));
		}

		//test canvas
		canvas = Canvas.createIfSupported();
    if (canvas == null) {
    	container.add(new Label("Error: canvas not supported!"));
      return;
    }
    int width = 800, height = 400;
    canvas.setWidth(width + "px");
    canvas.setHeight(height + "px");
    canvas.setCoordinateSpaceWidth(width * 4);
    canvas.setCoordinateSpaceHeight(height * 4);
    container.add(canvas);
    context = canvas.getContext2d();
    gwtCanvas = new GwtCanvas(canvas, CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect);

    //TEST
   /* context.beginPath();
    context.moveTo(25,0);
    context.lineTo(0,20);
    context.lineTo(25,40);
    context.closePath();
    context.fill();

    //TEST
    context.save();
    context.translate(200, 200);
    context.scale(100, 100);
    PathSymbol symbol = (PathSymbol) zongPlatformUtils().getSymbolPool().getSymbol(CommonSymbol.ClefG);
    GwtPath.drawPath(symbol.getPath(), context);
    context.fill();
    context.restore();

    //TEST
    context.save();
    context.translate(400, 300);
    context.scale(100, 100);
    symbol = (PathSymbol) zongPlatformUtils().getSymbolPool().getSymbol(CommonSymbol.ClefF);
    GwtPath.drawPath(symbol.getPath(), context);
    context.fill();
    context.restore(); */

    //test layout
		container.add(new Label("And here is the layout data:"));
		final SymbolPool symbolPool = zongPlatformUtils().getSymbolPool();
		final Label lblLayout = new Label("Loading...");
		container.add(lblLayout);
		platformUtils().openFileAsync("data/test/scores/musicxml20/BeetAnGeSample.xml", new AsyncResult<InputStream>() {

			@Override public void onSuccess (InputStream scoreData){
				new MusicXmlScoreDocFileReader(scoreData, null).produce(new AsyncResult<ScoreDoc>() {
					@Override public void onSuccess(ScoreDoc scoreDoc) {
						platformUtils().openFileAsync("test.xml",new AsyncResult<InputStream>() {
							@Override public void onSuccess (InputStream data){
								try {
									LayoutSettings layoutSettings = LayoutSettingsReader.read(data);
									Size2f areaSize = new Size2f(150, 10000);
									Context context = new Context(scoreDoc.getScore(), symbolPool, layoutSettings);
									Target target = Target.completeLayoutTarget(new ScoreLayoutArea(areaSize));
									ScoreLayout layout = new ScoreLayouter(context, target).createLayoutWithExceptions();
									lblLayout.setText(layout.toString().substring(0, 1000) + "...");

									//draw in canvas
									gwtCanvas.transformScale(20, 20);
									Iterable<Stamping> stampings = layout.getScoreFrameLayout(0).getMusicalStampings();
									//render them
									RendererArgs args = new RendererArgs(1, 1, new Point2i(0, 0), symbolPool);
									for (Stamping s : stampings) {
										StampingRenderer.drawAny(s, gwtCanvas, args);
									}
								} catch (IOException ex) {
									lblLayout.setText("layout error: " + ex.toString());
								}
							}

							@Override public void onFailure (Exception ex){
								lblLayout.setText("Layout error: " + ex.toString());
							}
						});
					}

					@Override public void onFailure(Exception ex) {
						lblLayout.setText("ScoreDoc reading error: " + ex.toString());
					}
				});
			}

			@Override public void onFailure (Exception ex){
				lblLayout.setText("MusicXML reading error: " + ex.toString());
			}

		});
	}

	private String findAClef(Score score, MP mp) {
		return "Last clef at or before " + mp + ": " +
			score.getClef(mp, Interval.BeforeOrAt).toString();
	}
	
}
