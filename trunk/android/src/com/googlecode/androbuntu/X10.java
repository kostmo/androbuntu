package com.googlecode.androbuntu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class X10 extends Activity implements View.OnClickListener {

		private SocketMonitor service_binder;
		private Spinner appliance_selector, housecode_selector;
		private SeekBar dimmer_bar;
		
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
			


			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			int saved_housecode_index = settings.getInt("house_code", 0);
			int saved_appliance_index = settings.getInt("appliance_code", 0);
			

			housecode_selector = (Spinner) findViewById(R.id.housecode_selector);
			SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, housecodes);
			housecode_selector.setAdapter(adapter);
			housecode_selector.setSelection(saved_housecode_index);
			

			appliance_selector = (Spinner) findViewById(R.id.appliance_selector);
			SpinnerAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, appliancecodes);
			appliance_selector.setAdapter(adapter2);
			appliance_selector.setSelection(saved_appliance_index);


			Button single_off_button = (Button) findViewById(R.id.slingle_light_off);
			single_off_button.setOnClickListener(single_light_off_listener);
			
			Button single_on_button = (Button) findViewById(R.id.slingle_light_on);
			single_on_button.setOnClickListener(single_light_on_listener);
			
			
	
		
			dimmer_bar = (SeekBar) findViewById(R.id.dimmer_bar);
			dimmer_bar.setOnSeekBarChangeListener(seek_change_listen);

	   }

	   

	   
	   private OnSeekBarChangeListener seek_change_listen = new OnSeekBarChangeListener() {
		    public void  onProgressChanged  (SeekBar seekBar, int new_value, boolean fromUser) {
		    	
		    	if (fromUser) {
		    		
		    		int prev_value = seekBar.getSecondaryProgress();
		    		int diff = new_value - prev_value;

		    		// This is a nifty way to store the previous value for relative adjustment
					seekBar.setSecondaryProgress(new_value);

			    	
			    	String housecode = (String) X10.this.housecode_selector.getSelectedItem();
			    	String appliance = (String) X10.this.appliance_selector.getSelectedItem();

			    	String devicecode = housecode+appliance;    		
		    		

		    		String command_mode, value_string;
		    		if (diff < 0) {
		    			command_mode = "dimmer";
		    			value_string = Integer.toString(-diff);
		    		} else {
		    			command_mode = "brighter";
		    			value_string = Integer.toString(diff);
		    		}
		    		

		    		String payload = devicecode + " " + value_string;
		    		
				 	   String[] reply = service_binder.send_message(command_mode, payload);
					   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    	}
			}
	
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
	
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
	   };
	   
	   
	   private ServiceConnection my_relay_service = new ServiceConnection() {
	       public void onServiceConnected(ComponentName className, IBinder service) {


	    	   service_binder = ((SocketMonitor.LocalBinder) service).getService();
	    	   
	    	   Log.d("forker", "Successfully connected to SocketMonitor service.");
	       }

	       public void onServiceDisconnected(ComponentName componentName) {


	    	   Log.d("forker", "SocketMonitor service disconnected.");
	       }
	   };

	   private View.OnClickListener single_light_on_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	dimmer_bar.setSecondaryProgress( dimmer_bar.getMax() );
		    	dimmer_bar.setProgress( dimmer_bar.getMax() );
		    	
		    	String housecode = (String) X10.this.housecode_selector.getSelectedItem();
		    	String appliance = (String) X10.this.appliance_selector.getSelectedItem();

		    	String payload = housecode+appliance;
		 	   String[] reply = service_binder.send_message("single_light_on", payload);
			   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
	   
	   private View.OnClickListener single_light_off_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	 
		    	dimmer_bar.setSecondaryProgress(0);
		    	dimmer_bar.setProgress(0);
		    	
		    	String housecode = (String) X10.this.housecode_selector.getSelectedItem();
		    	String appliance = (String) X10.this.appliance_selector.getSelectedItem();

		    	String payload = housecode+appliance;
		 	   String[] reply = service_binder.send_message("single_light_off", payload);
			   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
	   
	   
	   private View.OnClickListener lightsoff_listener = new View.OnClickListener() {
		    public void onClick(View v) {

		    	String housecode = (String) X10.this.housecode_selector.getSelectedItem();
		 	   String[] reply = service_binder.send_message("lights_off", housecode);
			   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
		
	   private View.OnClickListener lightson_listener = new View.OnClickListener() {
		    public void onClick(View v) {

		    	String housecode = (String) X10.this.housecode_selector.getSelectedItem();
			 	   String reply[] = service_binder.send_message("lights_on", housecode);
				   Toast.makeText(X10.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};

		
		
		
		
	   public void onClick(View v) {
		   
		   
	   }

	   
	   

	    @Override
	    protected void onDestroy() {
	    	
			int saved_housecode_index = X10.this.housecode_selector.getSelectedItemPosition();
			int saved_appliance_index = X10.this.appliance_selector.getSelectedItemPosition();

			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(X10.this);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("house_code", saved_housecode_index);
			editor.putInt("appliance_code", saved_appliance_index);
            editor.commit();
			
	        super.onDestroy();
	    	unbindService(my_relay_service);
	    }
}
