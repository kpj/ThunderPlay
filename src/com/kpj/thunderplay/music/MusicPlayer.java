package com.kpj.thunderplay.music;

import android.content.Intent;
import android.widget.MediaController.MediaPlayerControl;

import com.kpj.thunderplay.ContentHandler;


public class MusicPlayer implements MediaPlayerControl {
	public MusicService musicSrv;
	public Intent playIntent;

	public boolean musicBound = false;
	public boolean playbackPaused = true;

	/*
	 * Control playback
	 */
	public void startPlayback() {
		if(ContentHandler.songPosition == -1)
			return;
		
		musicSrv.playSong();

		if(playbackPaused) {
			playbackPaused = false;
		}
		
		ContentHandler.controller.onPlay();
	}
	
	public void pausePlayback() {
		pause();
		
		ContentHandler.controller.onPause();
	}

	public void playNext() {
		musicSrv.playNext();
		ContentHandler.songProgress = -1;

		if(playbackPaused) {
			playbackPaused = false;
		}
	}

	public void playPrev() {
		musicSrv.playPrev();
		ContentHandler.songProgress = -1;

		if(playbackPaused) {
			playbackPaused = false;
		}
	}

	/*
	 * Overridden functions of MediaPlayerControl
	 */
	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return false;
	}

	@Override
	public boolean canSeekForward() {
		return false;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		if(musicSrv != null && musicBound && musicSrv.isPlaying())
			return musicSrv.getPos();
		else 
			return 0;
	}

	@Override
	public int getDuration() {
		if(musicSrv != null && musicBound && musicSrv.isPlaying())
			return musicSrv.getDur();
		else 
			return 0;
	}

	@Override
	public boolean isPlaying() {
		if(musicSrv != null && musicBound)
			return musicSrv.isPlaying();
		return false;
	}

	@Override
	public void pause() {
		playbackPaused = true;
		musicSrv.pause();
	}

	@Override
	public void seekTo(int pos) {
		musicSrv.seek(pos);
	}

	@Override
	public void start() {
		musicSrv.start();
	}
}
