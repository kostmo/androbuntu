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
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;


public class MediaPanel extends Activity implements View.OnClickListener {

		private SocketMonitor service_binder;
		private MediaController mc;
		
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);

   
	       Intent i = new Intent();
	       i.setClass(MediaPanel.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       

	       

	       setContentView(R.layout.media);

	       ImageButton mute_button = (ImageButton) findViewById(R.id.mute_button);
	       mute_button.setOnClickListener(this);
//	       button.setText("Mute");
	       
	       ImageButton voldown_button = (ImageButton) findViewById(R.id.voldown_button);
	        voldown_button.setOnClickListener(voldown_listener);
//	        voldown_button.setText("VolDown");

		    ImageButton volup_button = (ImageButton) findViewById(R.id.volup_button);
	        volup_button.setOnClickListener(volup_listener);
//	        volup_button.setText("VolUp");


	        View v = (View) findViewById(R.layout.media);
//	        View v = (View) findViewById(R.id.media_player_button);
	        
	        mc = new MediaController(this);
	        mc.setMediaPlayer(player_interface);
	        mc.setEnabled(true);
	        mc.setAnchorView(v);
	        
	        
	        
	        

	        Button media_player_button = (Button) findViewById(R.id.media_player_button);
	        media_player_button.setOnClickListener(media_button_listener);
	   }

	   
	   
	   private View.OnClickListener media_button_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	
		    	Log.d("forker", "Can haz media player?");

		    	
		    	mc.requestFocus();
		    	mc.show(5);
		    	
		    	Log.d("forker", "Getting percentage...");
    	
		    	
		    	String[] reply = service_binder.send_message("exaile_query");    	
				Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
		    }
		};
	   


	   private MediaController.MediaPlayerControl player_interface = new MediaController.MediaPlayerControl() {

		public int getBufferPercentage() {
			return 75;
		}

		public int getCurrentPosition() {
			return 25;
		}

		public int getDuration() {
			return 180;
		}

		public boolean isPlaying() {
			return true;
		}

		public void pause() {
		}

		public void seekTo(int pos) {
		}

		public void start() {
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
