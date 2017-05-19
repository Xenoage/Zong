package com.xenoage.zong.android.activities.score;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.App;
import com.xenoage.zong.android.R;
import com.xenoage.zong.android.view.PdfScoreView;
import com.xenoage.zong.android.view.ScoreDocScoreView;
import com.xenoage.zong.android.view.ScoreView;
import com.xenoage.zong.documents.ScoreDoc;

import java.io.IOException;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;

/**
 * This activity displays a given score, either in MusicXML
 * or in PDF format.
 *
 * @author Andreas Wenger
 */
public class ScoreActivity
	extends FragmentActivity {

	public static ScoreActivity currentScoreActivity = null;

	private ScoreDoc scoreDoc = null;
	public ScoreView scoreView;

	private boolean playing = false;

	private float[] scalings = { 0.1f, 0.2f, 0.4f, 0.7f, 1.1f, 1.5f, 2f, 3f, 5f };
	private int currentScalingIndex = 4;

	private ViewPager pager;
	private ScorePagerAdapter pagerAdapter;


	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentScoreActivity = this;

		//layout and listeners
		setContentView(R.layout.score);
		registerListeners();

		//load score
		try {
			String filename = getIntent().getStringExtra("filename");
			if (filename.toLowerCase().endsWith(".pdf")) {
				//PDF score
				scoreView = new PdfScoreView(filename, getScreenSize(), scalings[currentScalingIndex]);
				//hide Playback button
				findViewById(R.id.score_playback).setVisibility(View.INVISIBLE);
			}
			else {
				//MusicXML score
				scoreDoc = App.load(filename);
				scoreView = new ScoreDocScoreView(scoreDoc, getScreenSize(), scalings[currentScalingIndex]);
				App.getMidiPlayer().open(scoreDoc.getScore(), ScoreActivity.this);
			}
		} catch (OutOfMemoryError ex) {
			showError("Not enough memory to load this score.");
		} catch (IOException ex) {
			showError("Could not load score.");
		} catch (Throwable t) {
			showError(platformUtils().getStackTraceString(t));
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

		//create and link pager components
		pager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new ScorePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
	}

	private void registerListeners() {
		//zoom buttons
		final ZoomControls zoomControls = (ZoomControls) findViewById(R.id.score_zoom);
		zoomControls.setOnZoomInClickListener(view -> {
			if (currentScalingIndex + 1 < scalings.length) {
				currentScalingIndex++;
				updateScoreView();
				//enable/disable buttons
				zoomControls.setIsZoomInEnabled(currentScalingIndex + 1 < scalings.length);
				zoomControls.setIsZoomOutEnabled(true);
			}
		});
		zoomControls.setOnZoomOutClickListener(view -> {
			if (currentScalingIndex > 0) {
				currentScalingIndex--;
				updateScoreView();
				//enable/disable buttons
				zoomControls.setIsZoomInEnabled(true);
				zoomControls.setIsZoomOutEnabled(currentScalingIndex > 0);
			}
		});
		//play button
		final Button playbackButton = (Button) findViewById(R.id.score_playback);
		playbackButton.setOnClickListener(view -> {
			playback();
			playbackButton.setText(playing ? "Stop" : "Play");
		});
	}

	private void updateScoreView() {
		scoreView.updateScreen(getScreenSize(), scalings[currentScalingIndex]);
		pagerAdapter.notifyDataSetChanged();
	}

	private void playback() {
		if (!playing)
			App.getMidiPlayer().start();
		else
			App.getMidiPlayer().pause();
		playing = !playing;
	}

	private void showError(String text) {
		//show error in log
		log(error(text));
		//show error in text view
		final TextView tv = new TextView(ScoreActivity.this);
		tv.setText(text);
		runOnUiThread(() -> setContentView(tv));
	}

	@Override protected void onStop() {
		App.getMidiPlayer().stop();
		super.onStop();
	}

	private Size2i getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		return new Size2i(display.getWidth(), display.getHeight());
	}


	private class ScorePagerAdapter
		extends FragmentStatePagerAdapter {

		public ScorePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override public int getCount() {
			return scoreView.getPagesCount();
		}

		@Override public Fragment getItem(int position) {
			ScorePageFragment page = new ScorePageFragment();
			Bundle args = new Bundle();
			args.putInt("pageIndex", position);
			page.setArguments(args);
			return page;
		}

		/**
		 * Without this workaround, the pager would not update.
		 * See http://stackoverflow.com/a/7287121/518491
		 */
		@Override public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	};

}
