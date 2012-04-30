package com.googlecode.androbuntu.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androbuntu.services.ServiceSocketMonitor;

public class SendGenericCommandTask extends AsyncTask<Void, Void, String> {

	private static String TAG = "SendServerCommand";
	
	
	Context context;
	private ServiceSocketMonitor service_binder = null;
	String single_command_string;
	
	public SendGenericCommandTask(Context context, ServiceSocketMonitor service_binder, String single_command_string) {
		this.context = context;
		this.service_binder = service_binder;
		this.single_command_string = single_command_string;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		return send_generic_command( this.context, single_command_string, this.service_binder );
	}

	@Override
	protected void onPostExecute (String response) {
		Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
	}


	public static String send_generic_command(Context context, String single_command_string, ServiceSocketMonitor service_binder) {
		if (service_binder == null) {
			Log.e(TAG, "I can't do this, because the service isn't bound.");
			return "Service not bound.";
		}
		
		Log.d(TAG, "Sending generic commands...");
		
		
		String[] reply = service_binder.send_message(single_command_string);

		
		return TextUtils.join("; ", reply);
	}
}
