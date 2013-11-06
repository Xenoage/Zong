package com.xenoage.zong.webapp.client;

import static com.xenoage.utils.math.Fraction._0;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
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
		RootPanel.get("container").add(new Label(findAClef(score)));
	}
	
	private String findAClef(Score score) {
		return "Last clef at or before measure 1 in staff 0: " +
			score.getClef(MP.atBeat(0, 1, 0, _0), Interval.BeforeOrAt).getType().toString() + "<br/>" +
			"Last clef at or before measure 1 in staff 1: " +
			score.getClef(MP.atBeat(1, 1, 0, _0), Interval.BeforeOrAt).getType().toString();
	}
	
}
