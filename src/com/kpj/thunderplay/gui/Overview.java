package com.kpj.thunderplay.gui;

import java.util.ArrayList;
import java.util.Comparator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.gui.bar.OverviewBar;


public class Overview extends Songs {
	private OverviewBar overviewBar;
	
	public Overview() {
		this(new ArrayList<Song>());
		Log.e("FOO", "Weird constructor called");
	}
	
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootView = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);		

		LinearLayout bbar = (LinearLayout) rootView.findViewById(R.id.bottom_bar_preview);
		overviewBar = new OverviewBar();
		overviewBar.initView(bbar);

		return rootView;
	}
}