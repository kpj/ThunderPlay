package com.kpj.thunderplay.gui;

import java.util.ArrayList;

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
import com.kpj.thunderplay.data.Song;

public class Queue extends Songs {
	public Queue(ArrayList<Song> es) {
		super(es);
	}

	public void moveSong(int curPos, int dist) {
		int newPos = curPos + dist;
		if(newPos < 0) newPos = ContentHandler.queue.size() - 1;
		if(newPos >= ContentHandler.queue.size()) newPos = 0;

		Song tmp = ContentHandler.queue.get(curPos);
		ContentHandler.queue.remove(curPos);
		ContentHandler.queue.add(newPos, tmp);

		ContentHandler.songPosition += dist;

		update();
	}

	public void clear() {
		ContentHandler.queue.clear();
		update();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) super.onCreateView(inflater, container, savedInstanceState);		
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
