package com.kpj.thunderplay.data;

import java.io.Serializable;

public class Song implements Serializable {
	private static final long serialVersionUID = 42L;
	
	private long id;
	private String title;
	private String artist;
	private String album;

	public Song(long songID, String songTitle, String songArtist, String songAlbum) {
		id = songID;
		title = songTitle;
		artist = songArtist;
		album = songAlbum;
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
}
