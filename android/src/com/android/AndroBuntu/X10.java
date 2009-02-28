package com.android.AndroBuntu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class X10 extends Activity implements View.OnClickListener {
	
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


   
		   this.getIntent().getClass();	// FIXME
		   
		   
		   
	       LinearLayout myfoo = new LinearLayout(this);
	       myfoo.setOrientation(LinearLayout.VERTICAL);
   
			   
			LinearLayout myfoo3 = new LinearLayout(this);
			myfoo3.setOrientation(LinearLayout.HORIZONTAL);
			
			
			LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(-1, -1);
			l.weight = 1f;
			
			Button lightson_button = new Button(this);
			lightson_button.setOnClickListener(lightson_listener);
			lightson_button.setText("LightsOn");
			myfoo3.addView(lightson_button, l );
			
			Button lightsoff_button = new Button(this);
			lightsoff_button.setOnClickListener(lightsoff_listener);
			lightsoff_button.setText("LightsOff");
			myfoo3.addView(lightsoff_button, l );
			myfoo.addView(myfoo3);
			   
	        
			setContentView(myfoo);
	   }

	   private View.OnClickListener lightsoff_listener = new View.OnClickListener() {
		    public void onClick(View v) {
//		 	   String reply = send_message("lights_off");
//			   Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
		    }
		};
		
	   private View.OnClickListener lightson_listener = new View.OnClickListener() {
		    public void onClick(View v) {
//		 	   String reply = send_message("lights_on");
//			   Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
		    }
		};
	       
	   public void onClick(View v) {
		   
		   
	   }


}
