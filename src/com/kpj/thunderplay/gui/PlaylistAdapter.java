package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpj.thunderplay.R;

public class PlaylistAdapter extends BaseAdapter {

	private ArrayList<String> playlists;
	private LayoutInflater inflater;
	
	public PlaylistAdapter(LayoutInflater infl, ArrayList<String> pls){
		playlists = pls;
		inflater = infl;
	}

	@Override
	public int getCount() {
		return playlists.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layer = (LinearLayout) inflater.inflate(R.layout.playlist_item, parent, false);

		TextView playlistView = (TextView) layer.findViewById(R.id.playlist_name);
		String cur = playlists.get(position);
		
		playlistView.setText(cur);

		layer.setTag(position);
		return layer;
	}
}
