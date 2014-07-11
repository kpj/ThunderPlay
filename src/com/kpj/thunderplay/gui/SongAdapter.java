package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpj.thunderplay.R;

public class SongAdapter extends BaseAdapter {

	private ArrayList<Song> songs;
	private LayoutInflater songInf;
	
	private int item_layout;

	public SongAdapter(LayoutInflater infl, ArrayList<Song> theSongs, int il){
		songs = theSongs;
		songInf = infl;
		item_layout = il;
	}

	@Override
	public int getCount() {
		return songs.size();
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
		LinearLayout songLay = (LinearLayout) songInf.inflate(item_layout, parent, false);

		TextView songView = (TextView) songLay.findViewById(R.id.song_title);
		TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);

		Song currSong = songs.get(position);

		songView.setText(currSong.getTitle());
		artistView.setText(currSong.getArtist());

		songLay.setTag(position);
		return songLay;
	}
}
