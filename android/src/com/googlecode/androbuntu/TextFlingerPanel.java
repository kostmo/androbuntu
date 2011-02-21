package com.android.AndroBuntu;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class TextFlingerPanel extends Activity implements View.OnClickListener {

	private EditText et;
		private SocketMonitor service_binder;
	
		private String[] flinger_options = {"read_text", "store_text", "fling_text"};
		Spinner fling_menu;
		
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(TextFlingerPanel.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

	       

	       

	       setContentView(R.layout.flinger);
	        
	       
	       
	       
	       fling_menu = (Spinner) findViewById(R.id.flinger_destination_selector);
	       ArrayAdapter adapter = ArrayAdapter.createFromResource(
	               this, R.array.flinger_destinations, android.R.layout.simple_spinner_item);
	       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	       fling_menu.setAdapter(adapter);

			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			int saved_fling_mode = settings.getInt("fling_mode", 0);
			fling_menu.setSelection(saved_fling_mode);
			
			
	       
	       Button button = (Button) findViewById(R.id.textflinger_button);
	       button.setOnClickListener(this);
		   
	       
	       et = (EditText) findViewById(R.id.flingertext);
	       

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

	   
	   
	   
	
	   public void onClick(View v) {
		   String flingtext = et.getText().toString();
		   
		   String command = flinger_options[fling_menu.getSelectedItemPosition()];
		   
		   String reply[] = service_binder.send_message( command, flingtext );
		   Toast.makeText(TextFlingerPanel.this, reply[0], Toast.LENGTH_SHORT).show();
	   }
	   
	   
	    @Override
	    protected void onDestroy() {
		
			int saved_fling_mode = fling_menu.getSelectedItemPosition();
				
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("fling_mode", saved_fling_mode);
            editor.commit();
            
            
	        super.onDestroy();
	    	unbindService(my_relay_service);
	    }
}
