package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kpj.thunderplay.R;


public class SongList extends Fragment {
	private ArrayList<Song> songList;
	private SongAdapter songAdt;

	public SongList(ArrayList<Song> sl) {
		songList = sl;
	}

	public Song getSongAt(int pos) {
		return songList.get(pos);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(R.layout.song_list, container, false);		

		songAdt = new SongAdapter(inflater, songList, R.layout.song_songlist);
		rootView.setAdapter(songAdt);
		
		return rootView;
	}
}
