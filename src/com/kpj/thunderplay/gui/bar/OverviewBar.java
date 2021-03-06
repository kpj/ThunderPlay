package com.kpj.thunderplay.gui.bar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;

public class OverviewBar {
	private TextView label;
	private Spinner choices;
	private Button addList;
	private EditText searchBar;

	public void initView(LinearLayout layout) {
		// container table
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);

		TableLayout table = new TableLayout(ContentHandler.ctx);
		table.setLayoutParams(tableParams);

		TableRow row;
		TableRow.LayoutParams params;
		params = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, 
				TableRow.LayoutParams.WRAP_CONTENT, 
				1.0f);


		// first row
		row = new TableRow(ContentHandler.ctx);
		row.setLayoutParams(tableParams);

		// add all button
		addList = new Button(ContentHandler.ctx);
		addList.setLayoutParams(params);
		addList.setText("Add All");
		addList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				for(Long l : ContentHandler.overviewFragment.getSongs()) {
					ContentHandler.queueFragment.addSong(l);
				}
			}
		});
		row.addView(addList);

		// search bar
		searchBar = new EditText(ContentHandler.ctx);
		searchBar.setLayoutParams(params);
		searchBar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				ContentHandler.overviewFragment.setFilter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
		});
		row.addView(searchBar);

		// finish first row
		table.addView(row);
		
		
		// second row
		row = new TableRow(ContentHandler.ctx);
		row.setLayoutParams(tableParams);

		// label
		label = new TextView(ContentHandler.ctx);

		label.setText("Sort by");
		label.setTextSize(15);
		label.setPadding(5, 0, 0, 0);

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
					ContentHandler.overviewFragment.sort(new Comparator<Long>() {
						public int compare(Long a, Long b) {
							return ContentHandler.allSongs.get(a).getTitle().compareTo(ContentHandler.allSongs.get(b).getTitle());
						}
					});
					break;
				case "Artist":
					ContentHandler.overviewFragment.sort(new Comparator<Long>() {
						public int compare(Long a, Long b) {
							return ContentHandler.allSongs.get(a).getArtist().compareTo(ContentHandler.allSongs.get(b).getArtist());
						}
					});
					break;
				case "Album":
					ContentHandler.overviewFragment.sort(new Comparator<Long>() {
						public int compare(Long a, Long b) {
							return ContentHandler.allSongs.get(a).getAlbum().compareTo(ContentHandler.allSongs.get(b).getAlbum());
						}
					});
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		choices.setLayoutParams(params);
		row.addView(choices);

		// finish second row
		table.addView(row);


		// finish layout
		layout.addView(table);
	}
}
