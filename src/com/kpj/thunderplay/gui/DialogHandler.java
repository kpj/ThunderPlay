package com.kpj.thunderplay.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.fs.FileHandler;

public class DialogHandler {
	public static void saveQueue(final Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		builder.setMessage("Enter name").setTitle("Save Queue");
		builder.setView(inflater.inflate(R.layout.dialog_save_queue, (ViewGroup) ((Activity) ctx).findViewById(R.layout.activity_main)))
		.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String name = ((EditText) ((AlertDialog) dialog).findViewById(R.id.playlist_name)).getText().toString();
				if(name.length() > 0) {
					FileHandler.savePlaylist(ctx, name, ContentHandler.queue);
					
					// reload playlists
					ContentHandler.playlistFragment.reload();
				}
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});      

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
