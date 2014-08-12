package com.kpj.thunderplay.data;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.kpj.thunderplay.music.MusicHandler;

public class Song implements Serializable {
	private static final long serialVersionUID = 42L;
	
	private long id;
	private String title;
	private String artist;
	private String album;
	private String filepath;

	public Song(long songID, String songTitle, String songArtist, String songAlbum, String songPath) {
		id = songID;
		title = songTitle;
		artist = songArtist;
		album = songAlbum;
		filepath = songPath;
	}

	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public Bitmap getAlbumCover() {
		return MusicHandler.getAlbumart(filepath);
	}
}
