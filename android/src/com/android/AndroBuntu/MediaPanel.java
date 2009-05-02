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


public class MediaPanel extends Activity implements View.OnClickListener {

		private SocketMonitor service_binder;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(MediaPanel.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

	       
	       Button button = new Button(this);
	       button.setOnClickListener(this);
	       button.setText("Mute");
		   
	
		   
	       LinearLayout myfoo = new LinearLayout(this);
	       myfoo.setOrientation(LinearLayout.VERTICAL);
   
	       
	       
	       
			
			LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(-1, -1);
			l.weight = 1f;
			
		       // --------------------------
	        LinearLayout myfoo2 = new LinearLayout(this);
	        myfoo2.setOrientation(LinearLayout.HORIZONTAL);
	        
	        Button voldown_button = new Button(this);
	        voldown_button.setOnClickListener(voldown_listener);
	        voldown_button.setText("VolDown");
			myfoo2.addView(voldown_button, l );
	        
	        Button volup_button = new Button(this);
	        volup_button.setOnClickListener(volup_listener);
	        volup_button.setText("VolUp");
	        myfoo2.addView(volup_button, l );
	        // --------------------------


	        myfoo.addView(myfoo2);
	        myfoo.addView(button);	// Mute
			   
	        
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

	   
	   
	   
		
	   private View.OnClickListener voldown_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	String[] reply = service_binder.send_message("XF86AudioLowerVolume");    	
			   Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};

	   private View.OnClickListener volup_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	String reply[] = service_binder.send_message("XF86AudioRaiseVolume");    	
			   Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
	       
	   public void onClick(View v) {

		   String reply[] = service_binder.send_message("XF86AudioMute");
		   Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
	   }
	   
	   
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	    	unbindService(my_relay_service);
	    }
}
