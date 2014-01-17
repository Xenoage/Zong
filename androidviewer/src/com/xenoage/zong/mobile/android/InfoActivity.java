package com.xenoage.zong.mobile.android;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xenoage.zong.Zong;


public class InfoActivity
	extends Activity
{


	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		((TextView) findViewById(R.id.info_menu_version)).setText("Prototype " + Zong.projectVersion + "." +
			Zong.projectIteration);
		//information
		((TextView) findViewById(R.id.info_title)).setText(Zong.getName(App.PROJECT_FIRST_NAME));
		((TextView) findViewById(R.id.info_version)).setText(Zong.projectVersion + "." +
			Zong.projectIteration);
	}
	
}


