package com.kpj.thunderplay;

import java.util.ArrayList;

import android.content.Context;

import com.kpj.thunderplay.data.Song;
import com.kpj.thunderplay.gui.Queue;
import com.kpj.thunderplay.gui.Playlists;
import com.kpj.thunderplay.gui.Overview;
import com.kpj.thunderplay.gui.bar.MusicController;
import com.kpj.thunderplay.music.MusicPlayer;


public class ContentHandler {
	public static Context ctx;
	
	public static Overview overviewFragment = null;
	public static Queue queueFragment = null;
	public static Playlists playlistFragment = null;
	
	public final static String queue_filename = "queue.txt";
	
	public static ArrayList<Song> queue = new ArrayList<Song>();
	public static ArrayList<Song> alreadyPlayed = new ArrayList<Song>();
	public static int songPosition = -1;
	public static int songProgress = -1;
	public static int songDuration = -1;
	public static boolean songPrepared = false;
	
	public static ArrayList<String> playlists = new ArrayList<String>();

	public static boolean isActivityInForeground = false;
	public static boolean isActivityPaused = false;

	public static MusicPlayer mplayer;
	
	public static MusicController controller;
}
