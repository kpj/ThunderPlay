package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.kpj.thunderplay.R;

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
	public void rmSongAt(int pos) {
		songList.remove(pos);
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

		registerForContextMenu(rootView);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, R.id.context_menu_delete_item, Menu.NONE, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;

		switch(item.getItemId()) {
		case R.id.context_menu_delete_item:
			rmSongAt(pos);
			break;
		}
		return super.onContextItemSelected(item);
	}
}
