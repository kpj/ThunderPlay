package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.gui.adapter.SonglistAdapter;

public abstract class Songs extends Fragment {
	protected ArrayList<Song> elements;
	protected SonglistAdapter adapter;
	
	private OnClickListener itemOnClickListener;
		
	public Songs(ArrayList<Song> es) {
		elements = es;
	}
	
	public void addSong(Song s) {
		elements.add(s);
		update();
	}
	
	public void rmSongAt(int pos) {
		ContentHandler.alreadyPlayed.remove(elements.get(pos));
		elements.remove(pos);
		update();
	}
	
	public void clear() {
		elements.clear();
		update();
	}
	
	public Song getSongAt(int pos) {
		return elements.get(pos);
	}
	
	public void setListItemOnClickEvent(OnClickListener listener) {
		itemOnClickListener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(R.layout.list_songlist, container, false);		

		adapter = new SonglistAdapter(inflater, elements);
		adapter.setOnClickListener(itemOnClickListener);
		rootView.setAdapter(adapter);
		
		return rootView;
	}
	
	protected void update() {
		adapter.notifyDataSetChanged();
	}
}
