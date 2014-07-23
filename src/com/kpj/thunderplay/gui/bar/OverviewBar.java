package com.kpj.thunderplay.gui.bar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;

public class OverviewBar {
	private TextView label;
	private Spinner choices;

	public void initView(RelativeLayout layout) {
		LayoutParams params;

		// label
		label = new TextView(ContentHandler.ctx);
		
		label.setText("Sort by");
		label.setTextSize(15);
		label.setPadding(5, 0, 0, 0);
		
		params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		label.setLayoutParams(params);
		
		layout.addView(label);

		// spinner
		choices = new Spinner(ContentHandler.ctx);

		List<String> spinnerArray =  new ArrayList<String>();
		spinnerArray.add("Title");
		spinnerArray.add("Artist");

		ArrayAdapter<String> spinnerAda = new ArrayAdapter<String>(ContentHandler.ctx, R.layout.spinner_item, spinnerArray);
		choices.setAdapter(spinnerAda);

		choices.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				String selectedItem = parent.getItemAtPosition(pos).toString();
				switch(selectedItem) {
				case "Title":
					ContentHandler.overviewFragment.sort(new Comparator<Song>() {
						public int compare(Song a, Song b) {
							return a.getTitle().compareTo(b.getTitle());
						}
					});
					break;
				case "Artist":
					ContentHandler.overviewFragment.sort(new Comparator<Song>() {
						public int compare(Song a, Song b) {
							return a.getArtist().compareTo(b.getArtist());
						}
					});
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		choices.setLayoutParams(params);

		layout.addView(choices);
	}
}
