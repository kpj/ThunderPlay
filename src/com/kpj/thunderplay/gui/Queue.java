package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.gui.bar.MusicController;

public class Queue extends Songs {
	private int lastMarkedSongPosition = -1;

	public Queue(ArrayList<Song> es) {
		super(es);

		setListItemOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());

				ContentHandler.songProgress = -1;
				ContentHandler.mplayer.musicSrv.setSong(ind);
				ContentHandler.mplayer.startPlayback();
			}
		});
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

	public void onPlaySong() {
		if(lastMarkedSongPosition != -1)
			hideMarker(lastMarkedSongPosition);

		showMarker(ContentHandler.songPosition);

		lastMarkedSongPosition = ContentHandler.songPosition;
	}
	
	public void onSongPause() {		
		hideMarker(ContentHandler.songPosition);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootView = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);		

		LinearLayout bbar = (LinearLayout) rootView.findViewById(R.id.bottom_bar_preview);
		ContentHandler.controller = new MusicController();
		ContentHandler.controller.initView(bbar);

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
