package com.android.AndroBuntu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.AndroBuntu.R;

public class AndroBuntu extends Activity {

	public TextView tv;
	public EditText et;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       TextView tv = new TextView(this);
       EditText et = new EditText(this);
       
       tv.setText( R.string.goodbye );
       
       et.setText( "192.168.0.9" );
       
       Button button = new Button(this);
       button.setOnClickListener(mCorkyListener);
       button.setText("booya!");


      
        
        LinearLayout myfoo = new LinearLayout(this);
        myfoo.setOrientation(LinearLayout.VERTICAL);
        
        myfoo.addView(tv);
        myfoo.addView(et);
        
        myfoo.addView(button);
       
       
       setContentView(myfoo);
   }
   
   private String send_message() {
	   
	   	String message = "";
	    try {
	
	    	Log.d("fark", "initiating");
	    	
//		  	tv.setText( R.string.hello );	// FIXME: This crashes!
	    	

	    	InetAddress addr = InetAddress.getByName("192.168.0.9");
//	    	InetAddress addr = InetAddress.getByName( et.getText().toString() );	// FIXME: This ALSO crashes!
	    	
	    	Log.d("fark", "got address");
	    	Socket MyClient = new Socket(addr, 46645);
	    	Log.d("fark", "connected to socket");
	
	        try {
		    	Log.d("fark", "About to instantiate buffered writer...");
	        	BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(MyClient.getOutputStream()));
		    	Log.d("fark", "instantiated buffered writer");
	        	
		    	
		    	Log.d("fark", "About to instantiate buffered reader...");
		        BufferedReader rd = new BufferedReader(new InputStreamReader(MyClient.getInputStream()));
		    	Log.d("fark", "instantiated buffered reader");

		    	
	            wr.write("XF86AudioMute");
		    	Log.d("fark", "wrote string");
	            wr.flush();
		    	Log.d("fark", "flushed string");
		    	
		    	
		    	
		        String str;
		        while ((str = rd.readLine()) != null) {
			    	Log.d("fark", "read some from the buffered reader");
		        	message += str;
		        }

		        rd.close();
		    	Log.d("fark", "cosed buffered reader");	

		    	
		    	
				wr.close();
		    	Log.d("fark", "cosed buffered writer");
	        } catch (IOException e) {
		    	Log.d("fark", "The buffered reader failed somehow...");
	        }
	
	
			MyClient.close();
	    	Log.d("fark", "cosed the socket entirely");
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
	    return message;
   }
   
   private View.OnClickListener mCorkyListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	

	    	String message = send_message();
	    	Toast.makeText(AndroBuntu.this, message, Toast.LENGTH_SHORT).show();
	    }
	};

}