package com.kpj.thunderplay.gui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;

public class SonglistAdapter extends BaseAdapter {
	private ArrayList<Long> elements;
	private LayoutInflater inflater;

	private OnClickListener onClickListener = null;

	public SonglistAdapter(LayoutInflater infl, ArrayList<Long> es) {
		elements = es;
		inflater = infl;
	}

	public void setOnClickListener(OnClickListener listener) {
		onClickListener = listener;
	}

	public void sort(Comparator<Long> comp) {
		Collections.sort(elements, comp);
		notifyDataSetChanged();
	}
	
	public int getSize() {
		return elements.size();
	}
	
	public ArrayList<Long> getSongs() {
		return elements;
	}
	
	public Filter getFilter() {
		return new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				elements = (ArrayList<Long>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				ArrayList<Long> filteredResults = getFilteredResults(constraint);

				FilterResults results = new FilterResults();
				results.values = filteredResults;

				return results;
			}

			private ArrayList<Long> getFilteredResults(CharSequence constraint) {
				ArrayList<Long> res = new ArrayList<Long>();
				constraint = constraint.toString().toLowerCase();
				for(Long l : ContentHandler.allSongIds) {
					Song cur = ContentHandler.allSongs.get(l);
					if(
							cur.getTitle().toLowerCase().contains(constraint) || 
							cur.getArtist().toLowerCase().contains(constraint) || 
							cur.getAlbum().toLowerCase().contains(constraint)) {
						res.add(l);
					}
					
				}
				return res;
			}
		};
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int i) {
		return elements.get(i);
	}

	@Override
	public long getItemId(int i) {
		return ContentHandler.allSongs.get(elements.get(i)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout layer = (RelativeLayout) inflater.inflate(R.layout.list_songlist_item, parent, false);

		// fill current item with information
		TextView songView = (TextView) layer.findViewById(R.id.song_title);
		TextView artistView = (TextView) layer.findViewById(R.id.song_artist);
		TextView albumView = (TextView) layer.findViewById(R.id.song_album);

		Song currSong = ContentHandler.allSongs.get(elements.get(position));

		songView.setText(currSong.getTitle());
		artistView.setText(currSong.getArtist());
		albumView.setText(currSong.getAlbum());

		layer.setTag(position);

		// set onClick event handler
		layer.setOnClickListener(onClickListener);

		// enable long clicks
		layer.setLongClickable(true);

		return layer;
	}
}
