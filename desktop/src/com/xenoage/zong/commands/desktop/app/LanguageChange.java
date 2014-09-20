package com.xenoage.zong.commands.desktop.app;

import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.utils.jse.lang.LanguageListener;

/**
 * Changes the current language of the app.
 * All registered {@link LanguageListener}s will be notified.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class LanguageChange
	extends TransparentCommand {

	/** The ID of the new language. */
	private String id;
	
	@Override public void execute() {
		LangManager.loadLanguage(id);
	}

}
