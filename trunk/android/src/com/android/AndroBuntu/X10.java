package com.android.AndroBuntu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class X10 extends Activity implements View.OnClickListener {

		private SocketMonitor service_binder;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(X10.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

		  
		   
		   
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

	   
	   
	   
	   
	   
	   
	   private ServiceConnection my_relay_service = new ServiceConnection() {
	       public void onServiceConnected(ComponentName className, IBinder service) {


	    	   service_binder = ((SocketMonitor.LocalBinder) service).getService();
	    	   
	    	   Log.d("forker", "Successfully connected to SocketMonitor service.");
	       }

	       public void onServiceDisconnected(ComponentName componentName) {


	    	   Log.d("forker", "SocketMonitor service disconnected.");
	       }
	   };

	   
	   
	   
	   
	   
	   private View.OnClickListener lightsoff_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		 	   String[] reply = service_binder.send_message("lights_off");
			   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
		
	   private View.OnClickListener lightson_listener = new View.OnClickListener() {
		    public void onClick(View v) {
			 	   String reply[] = service_binder.send_message("lights_on");
				   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
	       
	   public void onClick(View v) {
		   
		   
	   }

	   
	   

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	    	unbindService(my_relay_service);
	    }
}
