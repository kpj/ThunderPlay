package com.kpj.thunderplay.gui.bar;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.kpj.thunderplay.ContentHandler;

public class MusicController implements  OnSeekBarChangeListener {
	private Button psButton;
	private Button ppButton;
	private Button nsButton;

	private SeekBar sProgress;

	private Handler handler;
	private boolean stopRunnable = false;

	public MusicController() {
		handler = new Handler();
	}

	/*
	 * Setup layout
	 */
	public void initView(RelativeLayout layout) {
		LayoutParams params;

		// previous song button
		psButton = new Button(ContentHandler.ctx);
		psButton.setText("<<");
		psButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				prevSong();
			}
		});
		layout.addView(psButton);

		// play/pause button
		ppButton = new Button(ContentHandler.ctx);
		ppButton.setText("Play");
		ppButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				playpauseButtonPressed();
			}
		});
		params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		ppButton.setLayoutParams(params);
		layout.addView(ppButton);

		// next song button
		nsButton = new Button(ContentHandler.ctx);
		nsButton.setText(">>");
		nsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				nextSong();
			}
		});
		params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		nsButton.setLayoutParams(params);
		layout.addView(nsButton);

		// progress bar
		sProgress = new SeekBar(ContentHandler.ctx);
		sProgress.setOnSeekBarChangeListener(this);
		sProgress.setProgress(ContentHandler.songProgress);
		sProgress.setMax(ContentHandler.songDuration);
		params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 100, 0, 0);
		sProgress.setLayoutParams(params);
		layout.addView(sProgress);
	}

	/*
	 * Interact with musicplayer
	 */
	private void nextSong() {
		ContentHandler.mplayer.playNext();
		handler.post(progressBarUpdater);
	}

	private void prevSong() {
		ContentHandler.mplayer.playPrev();
		handler.post(progressBarUpdater);
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

		// handle progressbar thread
		handler.postDelayed(progressBarUpdater, 0);
	}

	public void onPause() {
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
