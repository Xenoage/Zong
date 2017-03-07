package com.xenoage.zong.android;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xenoage.zong.Zong;
import com.xenoage.zong.android.R;
import com.xenoage.zong.android.util.Supplier;


public class InfoActivity
	extends Activity
{


	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		((TextView) findViewById(R.id.info_menu_version)).setText("Prototype " + Zong.projectVersion + "." +
			Zong.projectIteration);
		//test lambdas TEST
		String s = run(() -> { return " with Lambda!"; });
		//information
		((TextView) findViewById(R.id.info_title)).setText(Zong.getName(App.projectFirstName) + s);
		((TextView) findViewById(R.id.info_version)).setText(Zong.projectVersion + "." +
			Zong.projectIteration);
	}

	private String run(Supplier<String> p) {
		return p.run();
	}
	
}


