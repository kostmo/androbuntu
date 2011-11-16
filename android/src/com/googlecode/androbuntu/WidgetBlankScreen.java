package com.googlecode.androbuntu;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.googlecode.androbuntu.services.ServiceBlankScreen;

public class WidgetBlankScreen extends AppWidgetProvider {
	
	static final String TAG = "WidgetSfx";

	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_icon_layout);
		
		PendingIntent pendingIntent = PendingIntent.getService(context,
				0, // no requestCode
				new Intent(context, ServiceBlankScreen.class),
				0 // no flags 
		);
		updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
		
		
		
		ComponentName thisWidget = new ComponentName(context, WidgetBlankScreen.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thisWidget, updateViews);	
	}
	
	
	@Override
	public void onDisabled(Context context) {
		
	}
}
