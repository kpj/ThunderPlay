package com.kpj.thunderplay.music;

import java.util.ArrayList;

import android.media.audiofx.Equalizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;

public class MusicEqualizer {
	private Equalizer equi;
	
	private ArrayList<SeekBar> bars = new ArrayList<SeekBar>();  
	private ArrayList<Integer> previousProgress = new ArrayList<Integer>();

	private View view;
	
	public MusicEqualizer(int mp_sessions_id) {
		equi = new Equalizer(0, mp_sessions_id);
		equi.setEnabled(true);
		
		view = setupView(new TableLayout(ContentHandler.ctx));
	}

	public View getView() {
		return view;
	}
	
	private View setupView(TableLayout layer) {
		layer.setOrientation(TableLayout.VERTICAL);

		short bandNum = equi.getNumberOfBands();
		final short minEQLevel = equi.getBandLevelRange()[0];
		final short maxEQLevel = equi.getBandLevelRange()[1];
		
		TableRow.LayoutParams layoutParams;
		for (short i = 0; i < bandNum; i++) {
			final short band = i;

			TableRow row = new TableRow(ContentHandler.ctx);
			row.setOrientation(LinearLayout.HORIZONTAL);

			// current frequency
			TextView freq = new TextView(ContentHandler.ctx);
			layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(4, 0, 0, 0);
			freq.setLayoutParams(layoutParams);
			freq.setText((equi.getCenterFreq(band) / 1000) + " Hz: ");
            row.addView(freq);
			
			// lower limit
			/*TextView minDB = new TextView(ContentHandler.ctx);
			minDB.setText((minEQLevel / 100) + " dB");
			layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			minDB.setLayoutParams(layoutParams);
			row.addView(minDB);*/

			// selection bar
			SeekBar bar = new SeekBar(ContentHandler.ctx);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(equi.getBandLevel(band) - minEQLevel);
			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					equi.setBandLevel(band, (short) (progress + minEQLevel));
				}

				public void onStartTrackingTouch(SeekBar seekBar) {}
				public void onStopTrackingTouch(SeekBar seekBar) {}
			});
			layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			bar.setLayoutParams(layoutParams);
			bar.setTag(bar.getProgress());
			row.addView(bar);
			bars.add(bar);

			// upper limit
			/*TextView maxDB = new TextView(ContentHandler.ctx);
			maxDB.setText((maxEQLevel / 100) + " dB");
			layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 0, 4, 0);
			maxDB.setLayoutParams(layoutParams);
			row.addView(maxDB);*/

			layer.addView(row);
		}
		
		Button reset = new Button(ContentHandler.ctx);
		reset.setText("Reset");
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resetFrequenciesToDefault();
			}
		});
		layer.addView(reset);

		return layer;
	}
	
	public void resetFrequenciesToDefault() {
		for(SeekBar b : bars) {
			b.setProgress((int) b.getTag());
		}
	}
	
	public void saveCurrentFrequencies() {
		previousProgress.clear();
		for(SeekBar b : bars) {
			previousProgress.add(b.getProgress());
		}
	}
	
	public void restoreSavedFrequencies() {
		for(int i = 0 ; i < previousProgress.size() ; i++) {
			SeekBar b = bars.get(i);
			int prev = previousProgress.get(i);
			
			if(b.getProgress() != prev)
				b.setProgress(prev);
		}
	}
	
	public void resetView() {
		View p = (View) view.getParent();
		if(p != null) 
			((ViewGroup) p).removeView(view);
	}
}
