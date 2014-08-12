package com.kpj.thunderplay.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

	public void setList(ArrayList<Song> sl) {
		elements = sl;
		update();
	}

	public int getSize() {
		return elements.size();
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

	public void sort(Comparator<Song> comp) {
		Collections.sort(elements, comp);
		update();
	}

	public void shuffle() {
		Collections.shuffle(elements);
		update();
	}

	public Song getSongAt(int pos) {
		return elements.get(pos);
	}

	public void setListItemOnClickEvent(OnClickListener listener) {
		itemOnClickListener = listener;
	}

	public void showMarker(int pos) {
		View v = getItemViewAt(pos);
		if(v != null)
			v.findViewById(R.id.song_indicator).setVisibility(View.VISIBLE);
	}
	public void hideMarker(int pos) {
		View v = getItemViewAt(pos);
		if(v != null)
			v.findViewById(R.id.song_indicator).setVisibility(View.INVISIBLE);
	}

	private View getItemViewAt(int pos) {
		ListView lview = (ListView) getView().findViewById(R.id.song_list);

		int firstListItemPosition = lview.getFirstVisiblePosition();
		int lastListItemPosition = firstListItemPosition + lview.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return lview.getAdapter().getView(pos, null, lview);
		} else {
			int childIndex = pos - firstListItemPosition;
			return lview.getChildAt(childIndex);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.list_songlist, container, false);
		
		ListView lview = (ListView) rootView.findViewById(R.id.song_list);
		registerForContextMenu(lview);
		adapter = new SonglistAdapter(inflater, elements);
		adapter.setOnClickListener(itemOnClickListener);
		lview.setAdapter(adapter);

		return rootView;
	}

	protected void update() {
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}
}
