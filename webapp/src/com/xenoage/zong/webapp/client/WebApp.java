package com.xenoage.zong.webapp.client;

import static com.xenoage.utils.math.Fraction._0;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.util.demo.ScoreRevolutionary;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp
	implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	@Override public void onModuleLoad() {
		Score score = ScoreRevolutionary.createScore();
		//String t = score.getClef(MP.atBeat(0, 1, 0, _0), Interval.BeforeOrAt).getType().toString() + " found";
		//Clef clef = new Clef(ClefType.G);
		//String t = clef.getType().toString();
		//String t = new Point2f(5, 10).toString();
		//Range r = Range.range(5);
		//Label hello = new Label("Result: " + t); //"Aha "+r.getCount() + " " + Defaults.defaultFont.getSize() + " " + t);
		RootPanel container = RootPanel.get("container");
		container.add(new Label(findAClef(score, MP.atBeat(0, 1, 0, _0))));
		container.add(new Label(findAClef(score, MP.atBeat(1, 1, 0, _0))));
		MP mp = MP.atBeat(0, 1, 0, _0);
		container.add(new Label("Voice at " + mp + ": " + score.getVoice(mp)));
	}
	
	private String findAClef(Score score, MP mp) {
		return "Last clef at or before " + mp + ": " +
			score.getClef(mp, Interval.BeforeOrAt).getType().toString();
	}
	
}
