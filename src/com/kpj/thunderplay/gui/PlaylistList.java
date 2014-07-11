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
import com.kpj.thunderplay.fs.FileHandler;

public class PlaylistList extends Fragment {
	private PlaylistAdapter plAdt;

	public PlaylistList() {}

	public void addPlaylist(Song s) {
		ContentHandler.queue.add(s);		
		plAdt.notifyDataSetChanged();
	}
	public void rmPlaylistAt(int pos) {		
		ContentHandler.playlists.remove(pos);
		plAdt.notifyDataSetChanged();
	}

	public void clear() {
		ContentHandler.playlists.clear();
		plAdt.notifyDataSetChanged();
	}
	
	public void update() {
		plAdt.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(R.layout.song_list, container, false);		

		plAdt = new PlaylistAdapter(inflater, ContentHandler.playlists);
		rootView.setAdapter(plAdt);

		registerForContextMenu(rootView);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(Menu.NONE, R.id.cm_delete_playlist, Menu.NONE, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;

		switch(item.getItemId()) {
		case R.id.cm_delete_playlist:
			FileHandler.deletePlaylist(ContentHandler.ctx, ContentHandler.playlists.get(pos));
			rmPlaylistAt(pos);
			break;
		}
		
		return super.onContextItemSelected(item);
	}
}
