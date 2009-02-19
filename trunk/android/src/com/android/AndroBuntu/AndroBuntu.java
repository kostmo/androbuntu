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
import android.text.Editable;
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
	    }
	    return message;
   }
   
   
   public void onClick(View v) {
	
	   String reply = send_message();
	   Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
   }

}