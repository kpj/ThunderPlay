package com.kpj.thunderplay.gui.adapter;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.gui.Playlists;
import com.kpj.thunderplay.gui.Queue;
import com.kpj.thunderplay.gui.Overview;
import com.kpj.thunderplay.music.MusicHandler;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	Context ctx;

	public TabsPagerAdapter(Context c, FragmentManager fm) {
		super(fm);

		ctx = c;

		// load old list if it was created
		@SuppressWarnings("unchecked")
		ArrayList<Song> tmp = (ArrayList<Song>) FileHandler.readObject(ctx, ContentHandler.queue_filename);
		ContentHandler.queue = (tmp!=null)?tmp:new ArrayList<Song>();

		// create objects
		ContentHandler.overviewFragment = new Overview(MusicHandler.getSongList(ctx));
		ContentHandler.overviewFragment.setListItemOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());
				ContentHandler.queueFragment.addSong(ContentHandler.overviewFragment.getSongAt(ind));
			}
		});

		ContentHandler.queueFragment = new Queue(ContentHandler.queue);
		ContentHandler.queueFragment.setListItemOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int ind = Integer.parseInt(view.getTag().toString());

				ContentHandler.mplayer.musicSrv.setSong(ind);
				ContentHandler.mplayer.startPlayback();

				if(ContentHandler.isActivityInForeground) {
					ContentHandler.mplayer.controller.show(0);
				}
			}
		});
		
		ContentHandler.playlistFragment = new Playlists();
	}

	@Override
	public Fragment getItem(int index) {
		// should be called createItem (only called once for every index)

		switch (index) {
		case 0:
			return ContentHandler.overviewFragment;
		case 1:
			return ContentHandler.queueFragment;
		case 2:
			return ContentHandler.playlistFragment;
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
