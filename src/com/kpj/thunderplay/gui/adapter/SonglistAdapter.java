package com.kpj.thunderplay.gui.adapter;

import java.util.ArrayList;

import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SonglistAdapter extends BaseAdapter {
	private ArrayList<Song> elements;
	private LayoutInflater inflater;
	
	private OnClickListener onClickListener = null;

	public SonglistAdapter(LayoutInflater infl, ArrayList<Song> es) {
		elements = es;
		inflater = infl;
	}
	
	public void setOnClickListener(OnClickListener listener) {
		onClickListener = listener;
	}

	@Override
	public int getCount() {
		return elements.size();
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
		LinearLayout layer = (LinearLayout) inflater.inflate(R.layout.list_songlist_item, parent, false);

		// fill current item with information
		TextView songView = (TextView) layer.findViewById(R.id.song_title);
		TextView artistView = (TextView) layer.findViewById(R.id.song_artist);

		Song currSong = elements.get(position);

		songView.setText(currSong.getTitle());
		artistView.setText(currSong.getArtist());

		layer.setTag(position);
		
		// set onClick event handler
		layer.setOnClickListener(onClickListener);
		
		// enable long clicks
		layer.setLongClickable(true);
		
		return layer;
	}
}
