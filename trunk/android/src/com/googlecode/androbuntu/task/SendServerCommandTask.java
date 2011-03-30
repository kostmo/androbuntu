package com.googlecode.androbuntu.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androbuntu.PreferencesServer;
import com.googlecode.androbuntu.services.ServiceSocketMonitor;

public class SendServerCommandTask extends AsyncTask<Void, Void, String> {

	private static String TAG = "SendServerCommand";
	
	
	Context context;
	private ServiceSocketMonitor service_binder = null;
	List<String> messages;
	
	public SendServerCommandTask(Context context, ServiceSocketMonitor service_binder, List<String> messages) {
		this.context = context;
		this.service_binder = service_binder;
		this.messages = messages;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		return send_lights_command( this.context, this.messages, this.service_binder );
	}

	@Override
	protected void onPostExecute (String response) {
		Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
	}


	public static String send_lights_command(Context context, List<String> messages, ServiceSocketMonitor service_binder) {
		if (service_binder == null) {
			Log.e(TAG, "I can't do this, because the service isn't bound.");
			return "Service not bound.";
		}
		
		Log.d(TAG, "Sending lights commands...");
		
		List<String> replies = new ArrayList<String>();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		int saved_housecode_index = settings.getInt(PreferencesServer.PREFKEY_HOUSE_CODE_INDEX, PreferencesServer.DEFAULT_HOUSE_CODE_INDEX);
		
		for (String message : messages) {
			
			String[] reply = service_binder.send_message(message, Character.toString( (char) (saved_housecode_index + 'A') ) );
			if (reply.length > 0)
				replies.add(reply[0]);
		}
		
		return TextUtils.join("; ", replies);
	}
}
