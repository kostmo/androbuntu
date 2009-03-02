package com.android.AndroBuntu;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ServerPanel extends PreferenceActivity {

		private EditText et;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);

		   
	       /*
	       TextView tv = new TextView(this);
	       et = new EditText(this);
	       tv.setText( R.string.goodbye );
	       et.setText( "192.168.0.9" );
	       */
//	       String hostname = et.getText().toString();

	       
	       addPreferencesFromResource(R.xml.preferences );

	       
/*
	       LinearLayout myfoo = new LinearLayout(this);
	       myfoo.setOrientation(LinearLayout.VERTICAL);
	       
	       
	        myfoo.addView(tv);
	        myfoo.addView(et);
	       
	       
	       setContentView(myfoo);
	       
	       
	       */
	   }
}
