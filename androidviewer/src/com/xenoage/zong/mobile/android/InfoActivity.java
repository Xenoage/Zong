package com.xenoage.zong.mobile.android;
import com.xenoage.zong.Zong;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class InfoActivity
	extends Activity
{


	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		((TextView) findViewById(R.id.info_menu_version)).setText("Prototype " + Zong.PROJECT_VERSION + "." +
			Zong.PROJECT_ITERATION);
		//information
		((TextView) findViewById(R.id.info_title)).setText(Zong.getName(App.PROJECT_FIRST_NAME));
		((TextView) findViewById(R.id.info_version)).setText(Zong.PROJECT_VERSION + "." +
			Zong.PROJECT_ITERATION);
	}
	
}


