package com.kpj.thunderplay.music;

import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.MainActivity;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener  {
	private MediaPlayer player;

	private final IBinder musicBind = new MusicBinder();

	private String currentSongTitle = "";
	private static final int NOTIFY_ID = 1;

	private boolean shuffle = false;
	private Random rand;

	public void onCreate() {
		super.onCreate();

		player = new MediaPlayer();
		rand = new Random();

		initMusicPlayer();
	}

	public void initMusicPlayer() {
		player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public class MusicBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	public void playSong() {
		player.reset();

		Song playSong = ContentHandler.queue.get(ContentHandler.songPosition);
		currentSongTitle = playSong.getTitle();

		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				playSong.getId());

		ContentHandler.queueFragment.onPlaySong();
		
		try {
			player.setDataSource(getApplicationContext(), trackUri);
		} catch(Exception e) {
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		
		ContentHandler.songPrepared = false;
		player.prepareAsync();
	}

	public void toggleShuffle() {
		shuffle = !shuffle;
		resetAlreadyPlayed();
	}

	public void setSong(int songIndex) {
		ContentHandler.songPosition = songIndex;
		resetAlreadyPlayed();
	}

	public int getPos() {
		return player.getCurrentPosition();
	}

	public int getDur() {
		return player.getDuration();
	}

	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void pause() {
		player.pause();
	}

	public void seek(int pos) {
		player.seekTo(pos);
	}

	public void start() {
		player.start();
	}

	public void playPrev() {
		if(ContentHandler.queue.size() == 0) return;

		ContentHandler.songPosition--;
		if(ContentHandler.songPosition < 0) ContentHandler.songPosition = ContentHandler.queue.size()-1;

		playSong();
	}

	public void playNext() {
		if(ContentHandler.queue.size() == 0) return;

		if(shuffle) {
			if(ContentHandler.queue.size() == ContentHandler.alreadyPlayed.size())
				resetAlreadyPlayed();

			if(ContentHandler.queue.size() == 1)
				ContentHandler.alreadyPlayed.clear();
			
			int newSong = ContentHandler.songPosition;
			Song cur = null;
			if(newSong != -1)
				cur = ContentHandler.queue.get(newSong);

			while(ContentHandler.alreadyPlayed.contains(cur)) {
				newSong = rand.nextInt(ContentHandler.queue.size());
				cur = ContentHandler.queue.get(newSong);
			}

			ContentHandler.songPosition = newSong;
			ContentHandler.alreadyPlayed.add(cur);
		} else {
			ContentHandler.songPosition++;
			if(ContentHandler.songPosition >= ContentHandler.queue.size()) ContentHandler.songPosition = 0;
		}

		playSong();
	}

	private void resetAlreadyPlayed() {
		ContentHandler.alreadyPlayed.clear();
		if(ContentHandler.songPosition != -1)
			ContentHandler.alreadyPlayed.add(ContentHandler.queue.get(ContentHandler.songPosition));
	}

	/*
	 * Overridden functions of Service
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		player.stop();
		player.release();

		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if(player.getCurrentPosition() > 0) {
			ContentHandler.songProgress = -1;
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		ContentHandler.songDuration = mp.getDuration();
		
		if(ContentHandler.songProgress != -1) mp.seekTo(ContentHandler.songProgress);
		ContentHandler.songPrepared = true;
		
		mp.start();

		Intent notIntent = new Intent(this, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.status_bar_icon)
		.setTicker(currentSongTitle)
		.setOngoing(true)
		.setContentTitle("Playing")
		.setContentText(currentSongTitle);
		Notification not = builder.build();

		startForeground(NOTIFY_ID, not);
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
	}
}
