package com.xenoage.zong.android.scoreview;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

import com.xenoage.zong.documents.ScoreDoc;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

/**
 * A {@link ScorePageView} with controls for zooming.
 * 
 * @author Andreas Wenger
 */
public class DecoratedScorePageView
	extends RelativeLayout {

	private float[] scalings = { 0.1f, 0.2f, 0.4f, 0.7f, 1f, 1.5f, 2f, 3f, 5f };
	private int currentScalingIndex = 3;

	private ScorePageView scoreView;


	public DecoratedScorePageView(Context context) {
		super(context);

		//score view
		scoreView = new ScorePageView(context, scalings[currentScalingIndex]);
		addView(scoreView, new LayoutParams(FILL_PARENT, LayoutParams.FILL_PARENT));

		//zooming
		final ZoomControls zoomControls = new ZoomControls(context);
		//zoomControls.setVisibility(View.GONE); //Hide
		zoomControls.setOnZoomInClickListener(view -> {
			if (currentScalingIndex + 1 < scalings.length) {
				currentScalingIndex++;
				scoreView.setScaling(scalings[currentScalingIndex]);
				//enable/disable buttons
				zoomControls.setIsZoomInEnabled(currentScalingIndex + 1 < scalings.length);
				zoomControls.setIsZoomOutEnabled(true);
			}
		});
		zoomControls.setOnZoomOutClickListener(view -> {
			if (currentScalingIndex > 0) {
				currentScalingIndex--;
				scoreView.setScaling(scalings[currentScalingIndex]);
				//enable/disable buttons
				zoomControls.setIsZoomInEnabled(true);
				zoomControls.setIsZoomOutEnabled(currentScalingIndex > 0);
			}
		});
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(zoomControls, lp);
	}

	public void setScoreDoc(ScoreDoc doc) {
		scoreView.setScoreDoc(doc);
	}

}
