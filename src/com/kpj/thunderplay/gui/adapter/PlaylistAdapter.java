package com.kpj.thunderplay.gui.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.fs.FileHandler;

public class PlaylistAdapter extends BaseAdapter {

	private ArrayList<String> playlists;
	private LayoutInflater inflater;

	public PlaylistAdapter(LayoutInflater infl, ArrayList<String> pls) {
		playlists = pls;
		inflater = infl;
	}

	@Override
	public int getCount() {
		return playlists.size();
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
		LinearLayout layer = (LinearLayout) inflater.inflate(R.layout.list_playlist_item, parent, false);

		// handle content
		TextView playlistView = (TextView) layer.findViewById(R.id.playlist_name);
		playlistView.setText(playlists.get(position));
		layer.setTag(position);

		// handle onClick events
		layer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());
				String plname = ContentHandler.playlists.get(ind);

				ContentHandler.queueFragment.clear();
				for(Song s : FileHandler.readPlaylist(ContentHandler.ctx, plname)) {
					ContentHandler.queueFragment.addSong(s);
				}
			}
		});

		return layer;
	}
}
