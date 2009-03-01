package com.android.AndroBuntu;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SocketMonitor extends Service {
	
	private Socket MyClient;
	
	private BufferedWriter wr;
	private BufferedReader rd;
	
    public class LocalBinder extends Binder {
    	SocketMonitor getService() {
            return SocketMonitor.this;
        }
    }
    
	
	@Override
	public void onCreate() {
		
		super.onCreate();
	    
	    
	}
	
	
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	

		Log.d("fork", "server onDestroy called.");
	}
	
	
	
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
	
	

	public String send_message(String command) {
	   
		
//    	String farkwad = et.getText().toString();
    	String farkwad = "192.168.0.9";
    	
    	int androbuntu_socket = 46645;
    	
	    try {
	    	InetAddress addr = InetAddress.getByName( farkwad );

	    	MyClient = new Socket(addr, androbuntu_socket);
	    	

        	wr = new BufferedWriter(new OutputStreamWriter(MyClient.getOutputStream()), 256);   
	        rd = new BufferedReader(new InputStreamReader(MyClient.getInputStream()), 256);
	    	
	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
//	        message = "Connection failed.";
	    }
	    
		
		
		
		Log.d("fork", "about to send a message");
	   	String message = "";
    	
        try {

            wr.write( command );
            wr.flush();
    	
	    	Log.d("fark", "Test1");
	    	

	        while (true) {
	        	
		    	Log.d("fark", "Top of while loop");
	        	
		        String str = null;
	        
	        	str = rd.readLine();

	        	
		    	Log.d("fark", "Read the string.");

	        	if (str == null) {
			    	Log.d("fark", "String is null.  Breaking.");
	        		break;
	        	}
		    	Log.d("fark", "Read the string; it is: "+str);
	        	
	        	
	        	message += str;
	        	
		    	Log.d("fark", "Aggregate message is: " + str);
	        }
	        
	        
	    	Log.d("fark", "Test2");

			
        } catch (IOException e) {
	    	Log.d("fark", "The buffered reader/writer failed somehow...");
        }


		Log.d("fork", "The message reply is: "+message);
		
		
		
		
		
	    try {
	    	

	        rd.close();	
			wr.close();
			
			
			MyClient.close();

	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
//       	message = "Closing connection failed.";
	    }
		
		
		
	    return message;
	}
}
