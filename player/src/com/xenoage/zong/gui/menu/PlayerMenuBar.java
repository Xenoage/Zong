package com.xenoage.zong.gui.menu;

import static com.xenoage.zong.Voc.Convert;
import static com.xenoage.zong.Voc.DirToMidi;
import static com.xenoage.zong.Voc.FileToMidi;
import static com.xenoage.zong.Voc.ShowInfo;
import static com.xenoage.zong.gui.menu.MenuFactory.jMenu;
import static com.xenoage.zong.gui.menu.MenuFactory.jMenuItem;
import static com.xenoage.zong.gui.menu.MenuFactory.jMenuItemsLanguages;
import static com.xenoage.zong.gui.menu.MenuFactory.jMenuItemsRecentFiles;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.xenoage.utils.base.files.RecentFiles;
import com.xenoage.utils.base.files.RecentFilesListener;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.lang.LanguageComponent;
import com.xenoage.zong.Voc;
import com.xenoage.zong.commands.app.ExitCommand;
import com.xenoage.zong.commands.beta.ErrorReportDialogCommand;
import com.xenoage.zong.commands.dialogs.AboutDialogCommand;
import com.xenoage.zong.commands.dialogs.AudioSettingsDialogCommand;
import com.xenoage.zong.commands.dialogs.OpenDocumentDialogCommand;
import com.xenoage.zong.commands.dialogs.SaveDocumentDialogCommand;
import com.xenoage.zong.commands.help.BlogCommand;
import com.xenoage.zong.commands.help.ReadmeCommand;
import com.xenoage.zong.commands.help.WebsiteCommand;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.commands.player.dialog.InfoDialogCommand;


/**
 * Controller for the menu bar of the main frame of the Player.
 * 
 * @author Andreas Wenger
 */
public class PlayerMenuBar
	implements LanguageComponent, RecentFilesListener
{
  
  JMenuBar menuSwing;
  
  
  /**
   * Creates a controller for the menu bar of
   * the main frame.
   */
  public PlayerMenuBar()
  {
    menuSwing = new JMenuBar();
    updateJMenuBar();
    //listen for language changes and recent files
    Lang.registerComponent(this);
    RecentFiles.addListener(this);
  }
  
  
  public void updateJMenuBar()
  {
  	menuSwing.removeAll();
    
  	//document menu
  	JMenu mnuDocument = jMenu(Voc.Document);
  	mnuDocument.add(jMenuItem(Lang.getWithEllipsis(Voc.Open), "document-open.png", new OpenDocumentDialogCommand()));
    mnuDocument.add(jMenuItem(Lang.getWithEllipsis(Voc.SaveAs), "document-save-as.png", new SaveDocumentDialogShow()));
    mnuDocument.addSeparator();
    mnuDocument.add(jMenuItem(Lang.getWithEllipsis(ShowInfo), "documentinfo.png", new InfoDialogCommand()));
    mnuDocument.addSeparator();
    List<JMenuItem> mnusRecentFiles = jMenuItemsRecentFiles();
    if (mnusRecentFiles.size() > 0) {
    	for (JMenuItem mnu : mnusRecentFiles)
    		mnuDocument.add(mnu);
    	mnuDocument.addSeparator();
    }
    mnuDocument.add(jMenuItem(Voc.Exit, null, new ExitCommand()));
    menuSwing.add(mnuDocument);
    
    //convert menu
		JMenu mnuConvert = jMenu(Convert);
		mnuConvert.add(jMenuItem(Lang.getWithEllipsis(FileToMidi), null, new FileToMidiConvert()));
		mnuConvert.add(jMenuItem(Lang.getWithEllipsis(DirToMidi), null, new DirToMidiConvert()));
		menuSwing.add(mnuConvert);
    
    //settings menu
    JMenu mnuSettings = jMenu(Voc.Settings);
    JMenu mnuLanguage = jMenu(Voc.Language);
    for (JMenuItem mnu : jMenuItemsLanguages())
    	mnuLanguage.add(mnu);
    mnuSettings.add(mnuLanguage);
    mnuSettings.add(jMenuItem(Lang.getWithEllipsis(Voc.Audio), "audio-x-generic.png", new AudioSettingsDialogCommand()));
  	menuSwing.add(mnuSettings);
  	
    //help menu
  	JMenu mnuHelp = jMenu(Voc.Help);
  	mnuHelp.add(jMenuItem(Voc.Readme, "help-browser.png", new ReadmeCommand()));
  	mnuHelp.add(jMenuItem(Voc.Website, "internet-web-browser.png", new WebsiteCommand()));
    mnuHelp.add(jMenuItem(Voc.Blog, "office-calendar.png", new BlogCommand()));
    mnuHelp.addSeparator();
    mnuHelp.add(jMenuItem(Voc.About, null, new AboutDialogCommand()));
    menuSwing.add(mnuHelp);
    
    //beta menu
    JMenu mnuBeta = jMenu(Voc.Beta);
    mnuBeta.add(jMenuItem(Lang.getWithEllipsis(Voc.ErrorReportOrFeatureRequest), null, new ErrorReportDialogCommand()));
    menuSwing.add(mnuBeta);
    
    //complete repaint
    menuSwing.repaint();
  }
  
  
  public JMenuBar getJMenuBar()
  {
    return menuSwing;
  }
  
  
  @Override public void languageChanged()
	{
		updateJMenuBar();
	}


	@Override public void recentFilesChanged()
	{
		updateJMenuBar();
	}
  
}
