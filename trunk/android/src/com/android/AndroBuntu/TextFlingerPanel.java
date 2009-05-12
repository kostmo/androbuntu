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
import android.widget.Spinner;
import android.widget.Toast;


public class TextFlingerPanel extends Activity implements View.OnClickListener {

	private EditText et;
//	private Button button;
		private SocketMonitor service_binder;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(TextFlingerPanel.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

	       

	       

	       setContentView(R.layout.flinger);
	        
	       
	       
	       
	       Spinner s = (Spinner) findViewById(R.id.flinger_destination_selector);
	       ArrayAdapter adapter = ArrayAdapter.createFromResource(
	               this, R.array.flinger_destinations, android.R.layout.simple_spinner_item);
	       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	       s.setAdapter(adapter);

	       
	       
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
		   
           
//		   String reply = service_binder.send_message("fling_text:" + flingtext );
		   String reply[] = service_binder.send_message( "fling_text:", flingtext );
		   Toast.makeText(TextFlingerPanel.this, reply[0], Toast.LENGTH_SHORT).show();
	   }
	   
	   
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	    	unbindService(my_relay_service);
	    }
}
