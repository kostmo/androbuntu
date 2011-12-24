package com.googlecode.androbuntu;

import com.googlecode.androbuntu.services.ServiceSocketMonitor;

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

	private ServiceSocketMonitor service_binder;
	private MediaController mc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		Intent i = new Intent();
		i.setClass(MediaPanel.this, ServiceSocketMonitor.class);
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
		@Override
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

		@Override
		public int getBufferPercentage() {
			return 75;
		}

		@Override
		public int getCurrentPosition() {
			return 25;
		}

		@Override
		public int getDuration() {
			return 180;
		}

		@Override
		public boolean isPlaying() {
			return true;
		}

		@Override
		public void pause() {
		}

		@Override
		public void seekTo(int pos) {
		}

		@Override
		public void start() {
		}

		@Override
		public boolean canPause() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean canSeekBackward() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean canSeekForward() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private ServiceConnection my_relay_service = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {


			service_binder = ((ServiceSocketMonitor.LocalBinder) service).getService();

			Log.d("forker", "Successfully connected to SocketMonitor service.");
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {


			Log.d("forker", "SocketMonitor service disconnected.");
		}
	};





	private View.OnClickListener voldown_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String[] reply = service_binder.send_message("XF86AudioLowerVolume");    	
			Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
		}
	};

	private View.OnClickListener volup_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			String reply[] = service_binder.send_message("XF86AudioRaiseVolume");    	
			Toast.makeText(MediaPanel.this, reply[0], Toast.LENGTH_SHORT).show();
		}
	};

	@Override
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
