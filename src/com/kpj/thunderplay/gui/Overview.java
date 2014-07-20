package com.kpj.thunderplay.gui;

import java.util.ArrayList;
import java.util.Comparator;

import android.view.View;
import android.view.View.OnClickListener;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.data.Song;


public class Overview extends Songs {	
	public Overview(ArrayList<Song> es) {
		super(es);
		
		// handle click on list item
		setListItemOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());
				ContentHandler.queueFragment.addSong(ContentHandler.overviewFragment.getSongAt(ind));
			}
		});
		
		// sort list content
		sort(new Comparator<Song>() {
			public int compare(Song a, Song b) {
				return a.getTitle().compareTo(b.getTitle());
			}
		});
	}
}