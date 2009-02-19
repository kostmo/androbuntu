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

public class AndroBuntu extends Activity implements View.OnClickListener {

	private EditText et;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       TextView tv = new TextView(this);
       et = new EditText(this);
       
       tv.setText( R.string.goodbye );
       
       et.setText( "192.168.0.9" );
       
       Button button = new Button(this);
       button.setOnClickListener(this);
       button.setText("booya!");


       Button x10_button = new Button(this);
       x10_button.setOnClickListener(x10_button_listener);
       x10_button.setText("x10 Controls");
      
       Button scripts_button = new Button(this);
       scripts_button.setOnClickListener(scripts_button_listener);
       scripts_button.setText("Scripts");
        
        LinearLayout myfoo = new LinearLayout(this);
        myfoo.setOrientation(LinearLayout.VERTICAL);
        
        
        myfoo.addView(x10_button);
        myfoo.addView(scripts_button);
        
        myfoo.addView(tv);
        myfoo.addView(et);
        myfoo.addView(button);
       
       
       setContentView(myfoo);
   }
   
   
   private View.OnClickListener x10_button_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	      // do something when the button is clicked
	    }
	};

   private View.OnClickListener scripts_button_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	      // do something when the button is clicked
	    }
	};
	
   
   private String send_message() {
	   
	   	String message = "";
	    try {

	    	String farkwad = et.getText().toString();
	    	InetAddress addr = InetAddress.getByName( farkwad );
	    	Socket MyClient = new Socket(addr, 46645);
	    	
	        try {
	        	BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(MyClient.getOutputStream()));   
		        BufferedReader rd = new BufferedReader(new InputStreamReader(MyClient.getInputStream()));
		    	
	            wr.write("XF86AudioMute");
	            wr.flush();
	    	
		    	
		        String str;
		        while ((str = rd.readLine()) != null)
		        	message += str;


		        rd.close();	
				wr.close();
				
	        } catch (IOException e) {
		    	Log.d("fark", "The buffered reader/writer failed somehow...");
	        }
	
			MyClient.close();
			
	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
	        message = "Failed.";
	    }
	    return message;
   }
   
   
   public void onClick(View v) {
	
	   String reply = send_message();
	   Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
   }

}