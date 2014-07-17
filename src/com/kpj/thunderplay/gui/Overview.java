package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.data.Song;


public class Overview extends Songs {	
	public Overview(ArrayList<Song> es) {
		super(es);
		
		setListItemOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());
				ContentHandler.queueFragment.addSong(ContentHandler.overviewFragment.getSongAt(ind));
			}
		});
	}
}