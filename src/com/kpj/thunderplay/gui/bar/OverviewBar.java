package com.kpj.thunderplay.gui.bar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;

public class OverviewBar {
	private TextView label;
	private Spinner choices;

	public void initView(LinearLayout layout) {
		TableRow.LayoutParams params;

		// container table
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		
		TableLayout table = new TableLayout(ContentHandler.ctx);
		table.setLayoutParams(tableParams);
		
		TableRow row;
		
		// first row
		row = new TableRow(ContentHandler.ctx);
		row.setLayoutParams(tableParams);
		row.setGravity(Gravity.CENTER);
		
		// label
		label = new TextView(ContentHandler.ctx);

		label.setText("Sort by");
		label.setTextSize(15);
		label.setPadding(5, 0, 0, 0);

		params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		label.setLayoutParams(params);

		row.addView(label);

		// spinner
		choices = new Spinner(ContentHandler.ctx);

		List<String> spinnerArray =  new ArrayList<String>();
		spinnerArray.add("Title");
		spinnerArray.add("Artist");
		spinnerArray.add("Album");

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
				case "Album":
					ContentHandler.overviewFragment.sort(new Comparator<Song>() {
						public int compare(Song a, Song b) {
							return a.getAlbum().compareTo(b.getAlbum());
						}
					});
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		choices.setLayoutParams(params);

		row.addView(choices);
		
		// finish first row
		table.addView(row);
		
		// finish layout
		layout.addView(table);
	}
}
