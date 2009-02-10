package com.android.AndroBuntu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.AndroBuntu.R;

public class AndroBuntu extends Activity {

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       TextView tv = new TextView(this);
       tv.setText( R.string.hello );
       setContentView(tv);
   }

}