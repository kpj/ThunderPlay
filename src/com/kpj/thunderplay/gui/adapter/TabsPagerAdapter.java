package com.kpj.thunderplay.gui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.gui.Overview;
import com.kpj.thunderplay.gui.Playlists;
import com.kpj.thunderplay.gui.Queue;
import com.kpj.thunderplay.music.MusicHandler;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	Context ctx;

	public TabsPagerAdapter(Context c, FragmentManager fm) {
		super(fm);

		ctx = c;		

		// create objects
		ContentHandler.overviewFragment = new Overview(MusicHandler.getSongList(ctx));
		ContentHandler.queueFragment = new Queue(ContentHandler.queue);
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
