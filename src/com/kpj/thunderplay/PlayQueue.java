package com.kpj.thunderplay;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlayQueue extends Fragment {
	private ArrayList<Song> songList;
	private SongAdapter songAdt;
	
	public PlayQueue(ArrayList<Song> sl) {
		for(Song s : sl) {
			Log.d("SONG", s.getTitle());
		}
		
		songList = sl;
	}
	
	public void addSong(Song s) {
		songList.add(s);		
		songAdt.notifyDataSetChanged();
	}
	public Song getSongAt(int pos) {
		return songList.get(pos);
	}
	public ArrayList<Song> getSongs() {
		return songList;
	}

	public void clear() {
		songList.clear();
		songAdt.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(R.layout.song_list, container, false);		
		
		songAdt = new SongAdapter(inflater, songList, R.layout.song_playqueue);
		rootView.setAdapter(songAdt);
		
		return rootView;
	}
}
