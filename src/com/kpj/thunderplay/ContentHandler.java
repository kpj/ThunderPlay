package com.kpj.thunderplay;

import java.util.ArrayList;

import android.content.Context;

import com.kpj.thunderplay.gui.PlayQueue;
import com.kpj.thunderplay.gui.PlaylistList;
import com.kpj.thunderplay.gui.Song;
import com.kpj.thunderplay.gui.SongList;


public class ContentHandler {
	public static Context ctx;
	
	public static SongList allsongsFragment = null;
	public static PlayQueue queueFragment = null;
	public static PlaylistList playlistFragment = null;
	
	public final static String queue_filename = "queue.txt";
	
	public static ArrayList<Song> queue = new ArrayList<Song>();
	public static int songPosition = -1;
	
	public static ArrayList<String> playlists = new ArrayList<String>();
}
