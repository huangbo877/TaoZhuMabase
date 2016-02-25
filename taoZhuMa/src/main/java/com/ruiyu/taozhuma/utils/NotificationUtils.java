package com.ruiyu.taozhuma.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtils {

	public static Notification getNotification(Context context, int id,
			String tickerText, String title, String content, int drawable,
			Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		Notification notification = new Notification(drawable, tickerText,
				System.currentTimeMillis());
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.when = System.currentTimeMillis();
		notification.defaults |= Notification.DEFAULT_SOUND;
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, content, pendingIntent);
		return notification;
	}

	public static void showNotification(Context context, Intent intent,
			String tickerText, String title, String content, int drawable) {
		int id = Long.bitCount(System.currentTimeMillis());
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = getNotification(context,id, tickerText, title,
				content, drawable, intent);
		manager.notify(id, notification);
	}
}
