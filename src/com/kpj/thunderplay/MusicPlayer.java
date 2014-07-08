package com.kpj.thunderplay;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;


public class MusicPlayer implements MediaPlayerControl {
	public MusicService musicSrv;
	public Intent playIntent;
	public boolean musicBound = false;

	public boolean paused = false, playbackPaused = false;

	public MusicController controller;

	private FragmentActivity ctx;


	public MusicPlayer(FragmentActivity c) {
		ctx = c;
	}

	public void setController(){
		controller = new MusicController(ctx);

		controller.setPrevNextListeners(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playNext();
			}
		}, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playPrev();
			}
		});

		controller.setMediaPlayer(this);
		controller.setAnchorView(ctx.findViewById(android.R.id.content));
		controller.setEnabled(true);
	}

	private void playNext(){
		musicSrv.playNext();

		if(playbackPaused) {
			setController();
			playbackPaused = false;
		}

		controller.show(0);
	}

	private void playPrev(){
		musicSrv.playPrev();

		if(playbackPaused) {
			setController();
			playbackPaused = false;
		}

		controller.show(0);
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
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
		if(musicSrv!=null && musicBound && musicSrv.isPlaying())
			return musicSrv.getDur();
		else 
			return 0;
	}

	@Override
	public boolean isPlaying() {
		if(musicSrv!=null && musicBound)
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
		musicSrv.go();
	}
}
