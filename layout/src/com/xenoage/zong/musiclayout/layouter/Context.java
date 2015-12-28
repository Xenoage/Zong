package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.zong.core.position.MP.mp0;

import java.util.Stack;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Context within the layouting process.
 * 
 * @author Andreas Wenger
 */
public class Context {
	
	/** The score which is layouted. */
	public Score score;
	/** The used symbols. */
	public SymbolPool symbols;
	/** Layout settings. */
	public LayoutSettings settings;
	
	/** The current musical position. Some values may be unknown, dependent
	 * on the state of the layouter. This value can be saved and popped on
	 * a stack, using {@link #saveMp()} and {@link #restoreMp()}. When a method
	 * changes this value, it must restore it when the method is finished. */
	public MP mp = mp0;
	
	private Stack<MP> mpStack = new Stack<MP>();
	
	
	public Context(Score score, SymbolPool symbols, LayoutSettings settings) {
		this.score = score;
		this.symbols = symbols;
		this.settings = settings;
	}

	/**
	 * Saves the current {@link MP} on the stack.
	 */
	public void saveMp() {
		mpStack.push(mp);
	}
	
	/**
	 * Restores the last {@link MP} from the stack.
	 */
	public void restoreMp() {
		mp = mpStack.pop();
	}
	
	/**
	 * Gets the {@link MusicContext} at the current position.
	 */
	public MusicContext getMusicContext(Interval clefAndKeyInterval, Interval accidentalsInterval) {
		return score.getMusicContext(mp, clefAndKeyInterval, accidentalsInterval);
	}
	
}
