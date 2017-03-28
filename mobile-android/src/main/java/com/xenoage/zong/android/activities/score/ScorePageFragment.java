package com.xenoage.zong.android.activities.score;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xenoage.zong.android.scoreview.ScreenViewBitmaps;

import static com.xenoage.utils.android.LayoutUtils.setMatchParent;
import static com.xenoage.zong.android.activities.score.ScoreActivity.currentScoreActivity;

/**
 * Page fragment in a score view.
 *
 * @author Andreas Wenger
 */
public class ScorePageFragment
	extends Fragment {

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ImageView pageView = new ImageView(container.getContext());
		setMatchParent(pageView);

		int pageIndex = getArguments().getInt("pageIndex");
		pageView.setImageBitmap(currentScoreActivity.scoreView.getPage(pageIndex).pageImage);

		return pageView;
	}
}
