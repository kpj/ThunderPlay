package com.kpj.thunderplay.gui;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.music.MusicHandler;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	Context ctx;
	
	public TabsPagerAdapter(Context c, FragmentManager fm) {
		super(fm);
		
		ctx = c;
		
		// load old list if it was created
		ArrayList<Song> tmp = (ArrayList<Song>) FileHandler.readObject(ctx, ContentHandler.queue_filename);
		ContentHandler.queue = (tmp!=null)?tmp:new ArrayList<Song>();
		
		// load playlists
		ContentHandler.playlists = FileHandler.getPlaylistNames(ctx);
		
		ContentHandler.queueFragment = new PlayQueue();
		ContentHandler.allsongsFragment = new SongList(MusicHandler.getSongList(ctx));
		ContentHandler.playlistFragment = new PlaylistList();
	}

	@Override
	public Fragment getItem(int index) {
		// should be called createItem (only called once for every index)
		
		switch (index) {
		case 0:
			return ContentHandler.allsongsFragment;
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
