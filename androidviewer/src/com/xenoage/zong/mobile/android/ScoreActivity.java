package com.xenoage.zong.mobile.android;

import static com.xenoage.utils.kernel.Range.range;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.github.ysamlan.horizontalpager.HorizontalPager;
import com.xenoage.utils.base.exceptions.ThrowableUtils;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.mobile.android.scoreview.ScreenViewBitmaps;


/**
 * This activity displays a given score.
 * 
 * @author Andreas Wenger
 */
public class ScoreActivity
	extends Activity
{
	
	private ScoreDoc scoreDoc = null;
	private ScreenViewBitmaps bitmaps = null;
	
	private boolean playing = false;
	
	private float[] scalings = {0.1f, 0.2f, 0.4f, 0.7f, 1.1f, 1.5f, 2f, 3f, 5f};
	private int currentScalingIndex = 4;
	

	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//layout and listeners
		setContentView(R.layout.score);
		registerListeners();
		
		//load score
		try
		{
			scoreDoc = App.load(getIntent().getStringExtra("filename"));
			App.getMidiPlayer().open(scoreDoc.getScore(), ScoreActivity.this);
			updateBitmaps();
		} catch (OutOfMemoryError ex) {
			showError("Not enough memory to load this score.");
		} catch (IOException ex) {
			showError("Could not load score.");
		} catch (Throwable t) {
			showError(ThrowableUtils.getStackTrace(t));
		}
		
		/*
		//load document in background thread, meanwhile show waiting text
		TextView tv = new TextView(this);
		tv.setText("Score is loaded...");
		setContentView(tv);

		new Thread()
		{

			@Override public void run()
			{
				//load document
				try {
					scoreDoc = App.load(getIntent().getStringExtra("filename"));
					App.getMidiPlayer().open(scoreDoc.getScore(), ScoreActivity.this);
					runOnUiThread(new Runnable()
					{
						@Override public void run()
						{
							View view = View.inflate(ScoreActivity.this, R.layout.score, null);
							//replace placeholder by real score view
							ScoreScreenView scoreView = new ScoreScreenView(ScoreActivity.this);
							scoreView.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
							ViewGroup container = (ViewGroup) view.findViewById(R.id.score_container);
							container.removeAllViews();
							container.addView(scoreView);
							//register listeners
							registerListeners(view, scoreView);
							setContentView(view);
							scoreView.setScoreDoc(scoreDoc);
							scoreView.updateLayout();
						}
					});
				} catch (OutOfMemoryError ex) {
					showError("Not enough memory to load this score.");
				} catch (IOException ex) {
					showError("Could not load score.");
				} catch (Throwable t) {
					showError(ThrowableUtils.getStackTrace(t));
				}
			}
		}.start();
		*/
		
	}
	
	
	private void registerListeners()
	{
		//page switcher
		final HorizontalPager pager = (HorizontalPager) findViewById(R.id.score_pager);
		pager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener()
		{
			@Override public void onScreenSwitched(int screen)
			{
				int activePageIndex = screen;
				bitmaps.setActivePageIndex(activePageIndex);
				updatePager();
			}
		});
		//zoom buttons
		final ZoomControls zoomControls = (ZoomControls) findViewById(R.id.score_zoom);
		zoomControls.setOnZoomInClickListener(new OnClickListener()
		{
			@Override public void onClick(View v)
			{
				if (currentScalingIndex + 1 < scalings.length)
				{
					currentScalingIndex++;
					updateBitmaps();
					//enable/disable buttons
					zoomControls.setIsZoomInEnabled(currentScalingIndex + 1 < scalings.length);
					zoomControls.setIsZoomOutEnabled(true);
				}
			}
		});
		zoomControls.setOnZoomOutClickListener(new OnClickListener()
		{
			@Override public void onClick(View v)
			{
				if (currentScalingIndex > 0)
				{
					currentScalingIndex--;
					updateBitmaps();
					//enable/disable buttons
					zoomControls.setIsZoomInEnabled(true);
					zoomControls.setIsZoomOutEnabled(currentScalingIndex > 0);
				}
			}
		});
		//play button
		final Button playbackButton = (Button) findViewById(R.id.score_playback);
		playbackButton.setOnClickListener(new OnClickListener()
		{
			@Override public void onClick(View v)
			{
				playback();
				playbackButton.setText(playing ? "Stop" : "Play");
			}
		});
	}
	
	
	private void updateBitmaps()
	{
		bitmaps = new ScreenViewBitmaps(scoreDoc, getScreenSize(), scalings[currentScalingIndex], getResources());
		//fill pager
		HorizontalPager pager = (HorizontalPager) findViewById(R.id.score_pager);
		pager.removeAllViews();
		for (int iPage : range(bitmaps.getPagesCount())) {
			ImageView pageView = new ImageView(this);
			pageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			pager.addView(pageView);
		}
		updatePager();
	}
	
	
	private void updatePager()
	{
		final HorizontalPager pager = (HorizontalPager) findViewById(R.id.score_pager);
		if (bitmaps != null) {
			int activePageIndex = pager.getCurrentScreen();
			for (int iPage : range(bitmaps.getPagesCount())) {
				ImageView pageView = (ImageView) pager.getChildAt(iPage);
				if (iPage < activePageIndex - 2 || iPage > activePageIndex + 1) {
					pageView.setImageBitmap(null);
				} else {
					pageView.setImageBitmap(bitmaps.getPageBitmap(iPage));
				}
			}
		}
	}
	
	
	private void playback()
	{
		if (!playing)
			App.getMidiPlayer().start();
		else
			App.getMidiPlayer().pause();
		playing = !playing;
	}


	private void showError(String text)
	{
		final TextView tv = new TextView(ScoreActivity.this);
		tv.setText(text);
		runOnUiThread(new Runnable()
		{
			@Override public void run()
			{
				setContentView(tv);
			}
		});
	}


	@Override protected void onStop()
	{
		App.getMidiPlayer().stop();
		super.onStop();
	}
	
	
	private Size2i getScreenSize()
	{
		Display display = getWindowManager().getDefaultDisplay(); 
		return new Size2i(display.getWidth(), display.getHeight());
	}


}
