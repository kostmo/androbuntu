package com.googlecode.androbuntu.task;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androbuntu.PreferencesServer;

public class WakeUpComputerTask extends AsyncTask<Void, Void, String> {

	private static String TAG = "WakeUpComputerTask";
	
	
	Context context;
	
	int followup_delay_millis = 0;
	Runnable followup_runnable = null;

	public WakeUpComputerTask(
			Context context,
			Runnable followup_runnable) {
		
		this.context = context;
		this.followup_runnable = followup_runnable;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String mac_address_string = settings.getString(PreferencesServer.PREFKEY_WOL_MAC_ADDRESS, PreferencesServer.DEFAULT_WOL_MAC_ADDRESS);
		String host_string = settings.getString(PreferencesServer.PREFKEY_HOSTNAME_OR_IP, PreferencesServer.DEFAULT_HOST_IP_ADDRESS);
		
		float wol_delay_seconds = settings.getFloat(PreferencesServer.PREFKEY_WOL_DELAY_SECONDS, PreferencesServer.DEFAULT_WOL_DELAY_SECONDS);

		this.followup_delay_millis = (int) (wol_delay_seconds*1000);
		
		Log.d(TAG, "Sending wake packet...");
		String response = WakeUpPC(host_string, mac_address_string);
		
		Log.d(TAG, "Wake packet sent!");
		
		return response;
	}

	@Override
	protected void onPostExecute (String response) {
		if (response != null) {
			Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
		} else {
			Handler handler = new Handler(); 
		    handler.postDelayed(this.followup_runnable, this.followup_delay_millis); 
		}
	}


	public static String WakeUpPC(String ipStr, String macStr) {

		String error = null;
		
		try {
			byte[] macBytes = getMacBytes(macStr);
			byte[] bytes = new byte[6 + 16 * macBytes.length];
			for (int i = 0; i < 6; i++)
				bytes[i] = (byte) 0xff;

			for (int i = 6; i < bytes.length; i += macBytes.length)
				System.arraycopy(macBytes, 0, bytes, i, macBytes.length);

			InetAddress address = InetAddress.getByName(ipStr);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PreferencesServer.WOL_PORT);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();

		} catch (Exception e) {
            //System.out.println("Failed to send Wake-on-LAN packet: + e");
        	error = e.getMessage();
        }
        
        return error;
    }
    
    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6)
            throw new IllegalArgumentException("Invalid MAC address."); 

        try {
            for (int i = 0; i < 6; i++)
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address."); 
        }
        return bytes;
    }
}
