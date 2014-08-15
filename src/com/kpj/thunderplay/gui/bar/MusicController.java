package com.kpj.thunderplay.gui.bar;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.kpj.thunderplay.ContentHandler;

public class MusicController implements OnSeekBarChangeListener {
	private Button psButton;
	private Button ppButton;
	private Button nsButton;

	private SeekBar sProgress;
	
	private ImageView cover;

	private Handler handler;
	private boolean stopRunnable = false;
	
	public MusicController() {
		handler = new Handler();
	}

	/*
	 * Setup layout
	 */
	public void initView(LinearLayout layout) {
		LinearLayout.LayoutParams params;
		LinearLayout row;
		
		//// controls
		row = new LinearLayout(ContentHandler.ctx);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		row.setLayoutParams(params);
		
		// previous song button
		psButton = new Button(ContentHandler.ctx);
		psButton.setText("<<");
		psButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				prevSong();
			}
		});
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		psButton.setLayoutParams(params);
		row.addView(psButton);

		// play/pause button
		ppButton = new Button(ContentHandler.ctx);
		ppButton.setText("Play");
		ppButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				playpauseButtonPressed();
			}
		});
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		ppButton.setLayoutParams(params);
		row.addView(ppButton);

		// next song button
		nsButton = new Button(ContentHandler.ctx);
		nsButton.setText(">>");
		nsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				nextSong();
			}
		});
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		nsButton.setLayoutParams(params);
		row.addView(nsButton);

		//// end controls
		layout.addView(row);
		
		// progress bar
		sProgress = new SeekBar(ContentHandler.ctx);
		sProgress.setOnSeekBarChangeListener(this);
		sProgress.setProgress(ContentHandler.songProgress);
		sProgress.setMax(ContentHandler.songDuration);
		layout.addView(sProgress);
		
		// album cover
		cover = new ImageView(ContentHandler.ctx);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		cover.setLayoutParams(params);
		cover.setAdjustViewBounds(true);
		layout.addView(cover);
	}
	
	/*
	 * Handle async cover loading
	 */
	
	private void updateCover() {
		((Activity) ContentHandler.ctx).runOnUiThread(new CoverLoader());
	}
	
	private class CoverLoader implements Runnable {
        @Override
        public void run() {
        	cover.setImageBitmap(ContentHandler.allSongs.get(ContentHandler.queue.get(ContentHandler.songPosition)).getAlbumCover());
        }
    }

	/*
	 * Interact with music player
	 */
	private void nextSong() {
		ContentHandler.mplayer.playNext();
		handler.post(progressBarUpdater);
		updateCover();
	}

	private void prevSong() {
		ContentHandler.mplayer.playPrev();
		handler.post(progressBarUpdater);
		updateCover();
	}

	private void playpauseButtonPressed() {
		if(ContentHandler.mplayer.playbackPaused) {
			ContentHandler.mplayer.startPlayback();
		} else {
			ContentHandler.mplayer.pausePlayback();
		}
	}

	public void onPlay() {
		ppButton.setText("Pause");

		// handle progress bar thread
		handler.postDelayed(progressBarUpdater, 0);
		
		// handle cover
		updateCover();
	}

	public void onPause() {
		ppButton.setText("Play");
	}
	
	public void onStop() {
		ppButton.setText("Play");
	}

	public void onBackground() {
		stopRunnable = true;
	}
	
	public void onForeground() {
		handler.postDelayed(progressBarUpdater, 0);
	}
	
	/*
	 * Update progress bar
	 */
	final private Runnable progressBarUpdater = new Runnable() {
		public void run() {
			if(!ContentHandler.mplayer.playbackPaused) {
				int pos = ContentHandler.mplayer.getCurrentPosition();
				int dur = ContentHandler.mplayer.getDuration();

				sProgress.setProgress(pos);
				sProgress.setMax(dur);
				
				if(stopRunnable)
					stopRunnable = false;
				else
					handler.postDelayed(this, 100);
			}
		}

	};

	/*
	 * Overridden functions for SeekBar.OnSeekBarChangeListener
	 */
	@Override
	public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
		if(fromUser) {
			ContentHandler.mplayer.seekTo(progress);
		}

		if(ContentHandler.songPrepared && !ContentHandler.mplayer.playbackPaused) {
			ContentHandler.songProgress = progress;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar view) {}

	@Override
	public void onStopTrackingTouch(SeekBar view) {}
}
