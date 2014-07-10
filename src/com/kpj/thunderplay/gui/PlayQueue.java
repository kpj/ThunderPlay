package com.kpj.thunderplay.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;

public class PlayQueue extends Fragment {
	private SongAdapter songAdt;

	public PlayQueue() {}

	public void addSong(Song s) {
		ContentHandler.queue.add(s);		
		songAdt.notifyDataSetChanged();
	}
	public void rmSongAt(int pos) {
		ContentHandler.queue.remove(pos);
		songAdt.notifyDataSetChanged();
	}
	public Song getSongAt(int pos) {
		return ContentHandler.queue.get(pos);
	}
	public void moveSong(int curPos, int dist) {
		int newPos = curPos + dist;
		if(newPos < 0) newPos = ContentHandler.queue.size() - 1;
		if(newPos >= ContentHandler.queue.size()) newPos = 0;
		
		Song tmp = ContentHandler.queue.get(curPos);
		ContentHandler.queue.remove(curPos);
		ContentHandler.queue.add(newPos, tmp);
		
		ContentHandler.songPosition += dist;
		
		songAdt.notifyDataSetChanged();
	}

	public void clear() {
		ContentHandler.queue.clear();
		songAdt.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(R.layout.song_list, container, false);		

		songAdt = new SongAdapter(inflater, ContentHandler.queue, R.layout.song_playqueue);
		rootView.setAdapter(songAdt);

		registerForContextMenu(rootView);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(Menu.NONE, R.id.cm_delete_item, Menu.NONE, "Delete");
		menu.add(Menu.NONE, R.id.cm_move_up, Menu.NONE, "Move Up");
		menu.add(Menu.NONE, R.id.cm_move_down, Menu.NONE, "Move Down");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;

		switch(item.getItemId()) {
		case R.id.cm_delete_item:
			rmSongAt(pos);
			break;
		case R.id.cm_move_up:
			moveSong(pos, -1);
			break;
		case R.id.cm_move_down:
			moveSong(pos, 1);
			break;
		}
		return super.onContextItemSelected(item);
	}
}
