package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.music.MusicHandler;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	Context ctx;
	
	public TabsPagerAdapter(Context c, FragmentManager fm) {
		super(fm);
		
		ctx = c;
		
		ContentHandler.queueFragment = new PlayQueue(new ArrayList<Song>());
		ContentHandler.allsongsFragment = new SongList(MusicHandler.getSongList(ctx));
	}

	@Override
	public Fragment getItem(int index) {
		// should be called createItem (only called once for every index)
		
		switch (index) {
		case 0:
			return ContentHandler.allsongsFragment;
		case 1:
			return ContentHandler.queueFragment;
		}

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
