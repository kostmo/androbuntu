package com.android.AndroBuntu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


public class X10 extends Activity implements View.OnClickListener {

		private SocketMonitor service_binder;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(X10.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

		  
		   
			
			setContentView(R.layout.lights);
			    
			Button lightson_button = (Button) findViewById(R.id.lightson_button);
			lightson_button.setOnClickListener(lightson_listener);
			

			Button lightsoff_button = (Button) findViewById(R.id.lightsoff_button);
			lightsoff_button.setOnClickListener(lightsoff_listener);
			
			String[] housecodes = new String[16];
			String[] appliancecodes = new String[16];
			for (int j=0; j<housecodes.length; j++) {
				housecodes[j] = Character.toString( (char) (j + 'A') );
				appliancecodes[j] = Integer.toString(j + 1);
			}

			Spinner housecode_selector = (Spinner) findViewById(R.id.housecode_selector);
			SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, housecodes);
			housecode_selector.setAdapter(adapter);


			Spinner appliance_selector = (Spinner) findViewById(R.id.appliance_selector);
			SpinnerAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, appliancecodes);
			appliance_selector.setAdapter(adapter2);

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
