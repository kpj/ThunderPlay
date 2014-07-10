package com.kpj.thunderplay;

import java.util.ArrayList;

import com.kpj.thunderplay.gui.PlayQueue;
import com.kpj.thunderplay.gui.Song;
import com.kpj.thunderplay.gui.SongList;


public class ContentHandler {
	public static PlayQueue queueFragment = null;
	public static SongList allsongsFragment = null;
	
	public final static String queue_filename = "queue.txt";
	
	public static ArrayList<Song> queue = new ArrayList<Song>();
	public static int songPosition = -1;
}
