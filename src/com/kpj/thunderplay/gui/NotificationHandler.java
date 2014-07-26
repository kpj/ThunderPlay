package com.kpj.thunderplay.gui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import com.kpj.thunderplay.MainActivity;
import com.kpj.thunderplay.R;

public class NotificationHandler {
	private static final int NOTIFY_ID = 1;
	
	public static void showSongNotifier(Service serv, String currentSongTitle) {
		Intent notIntent = new Intent(serv, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(serv, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification.Builder builder = new Notification.Builder(serv);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.status_bar_icon)
		.setTicker(currentSongTitle)
		.setOngoing(true)
		.setContentTitle("Playing")
		.setContentText(currentSongTitle);
		Notification not = builder.build();

		serv.startForeground(NOTIFY_ID, not);
	}
}
