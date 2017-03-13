package com.xenoage.zong.android;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.Zong;
import com.xenoage.utils.kernel.functional.Supplier;

import static com.xenoage.utils.math.Fraction.fr;


public class InfoActivity
	extends Activity
{


	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		((TextView) findViewById(R.id.info_menu_version)).setText("Prototype " + Zong.projectVersion + "." +
			Zong.projectIteration);
		//informationgit
		((TextView) findViewById(R.id.info_title)).setText(Zong.getName(App.projectFirstName));
		((TextView) findViewById(R.id.info_version)).setText(Zong.projectVersion + "." +
			Zong.projectIteration);
	}
	
}


