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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.gui.adapter.PlaylistAdapter;

public class Playlists extends Fragment {
	private PlaylistAdapter plAdt;

	public Playlists() {
		reload();
	}

	public void rmPlaylistAt(int pos) {		
		ContentHandler.playlists.remove(pos);
		update();
	}

	public void reload() {
		ContentHandler.playlists = FileHandler.getPlaylistNames(ContentHandler.ctx);
		update(); // why is this not working?
	}

	public void clear() {
		ContentHandler.playlists.clear();
		update();
	}

	private void update() {
		if(plAdt != null)
			plAdt.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.list_playlist, container, false);		

		ListView lview = (ListView) rootView.findViewById(R.id.song_list);
		plAdt = new PlaylistAdapter(inflater, ContentHandler.playlists);
		lview.setAdapter(plAdt);

		registerForContextMenu(lview);

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
