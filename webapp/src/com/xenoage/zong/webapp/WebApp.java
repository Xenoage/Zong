package com.xenoage.zong.webapp;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.xenoage.utils.math.MathUtils;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class WebApp implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button button = new Button("Click me");
		final Label label = new Label();

		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				label.setText(MathUtils.max(200, 100)+"");
			}
		});

		// Assume that the host HTML has elements defined whose
		// IDs are "slot1", "slot2".  In a real app, you probably would not want
		// to hard-code IDs.  Instead, you could, for example, search for all
		// elements with a particular CSS class and replace them with widgets.
		//
		RootPanel.get("slot1").add(button);
		RootPanel.get("slot2").add(label);
	}

}
