package com.kpj.thunderplay.gui;

import java.io.Serializable;

public class Song implements Serializable {
	private static final long serialVersionUID = 42L;
	
	private long id;
	private String title;
	private String artist;

	public Song(long songID, String songTitle, String songArtist) {
		id = songID;
		title = songTitle;
		artist = songArtist;
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
}
