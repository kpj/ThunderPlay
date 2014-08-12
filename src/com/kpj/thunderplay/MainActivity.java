package com.kpj.thunderplay;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.gui.DialogHandler;
import com.kpj.thunderplay.gui.adapter.TabsPagerAdapter;
import com.kpj.thunderplay.music.MusicHandler;
import com.kpj.thunderplay.music.MusicPlayer;
import com.kpj.thunderplay.music.MusicService;
import com.kpj.thunderplay.music.MusicService.MusicBinder;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private Context ctx = this;

	private ViewPager viewPager;
	private TabsPagerAdapter tabAdapter;
	private ActionBar actionBar;

	private String[] tabs = { "All Songs", "Queue", "Playlists" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ContentHandler.ctx = this;
		ContentHandler.mplayer = new MusicPlayer();

		// handle configuration
		load_configuration();

		// setup activity
		setContentView(R.layout.activity_main);
		registerForContextMenu(findViewById(R.id.activity_main));

		// load all songs
		ContentHandler.allSongs = MusicHandler.getAllSongs(ctx);
		ContentHandler.allSongIds = MusicHandler.map2list(ContentHandler.allSongs);

		// tabbed view
		setupTabs();
	}

	/*
	 * Set up the GUI
	 */
	public void setupTabs() {
		viewPager = (ViewPager) findViewById(R.id.activity_main);
		actionBar = getActionBar();
		tabAdapter = new TabsPagerAdapter(this, getFragmentManager());

		viewPager.setAdapter(tabAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// reselect tab on swipe
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/*
	 * Handle configuration
	 */
	private void load_configuration() {
		// preferences
		SharedPreferences settings = getSharedPreferences("com.kpj.thunderplay", 0);

		ContentHandler.songPosition = settings.getInt("songPosition", -1);
		ContentHandler.songProgress = settings.getInt("songProgress", -1);

		// files
		@SuppressWarnings("unchecked")
		ArrayList<Long> tmp = (ArrayList<Long>) FileHandler.readObject(ctx, ContentHandler.queue_filename);
		ContentHandler.queue = (tmp!=null)?tmp:new ArrayList<Long>();
	}

	private void save_configuration() {
		// preferences
		SharedPreferences settings = getSharedPreferences("com.kpj.thunderplay", 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putInt("songPosition", ContentHandler.songPosition);
		editor.putInt("songProgress", ContentHandler.songProgress);

		editor.commit();

		// files
		FileHandler.writeObject(
				ctx,
				ContentHandler.queue_filename,
				ContentHandler.queue);
	}

	/*
	 * Handle the music connection
	 */
	private ServiceConnection musicConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;

			ContentHandler.mplayer.musicSrv = binder.getService();
			ContentHandler.mplayer.musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			ContentHandler.mplayer.musicBound = false;
		}
	};

	/*
	 * Create the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.om_toggle_shuffle, menu);
		getMenuInflater().inflate(R.menu.om_shuffle_queue, menu);
		getMenuInflater().inflate(R.menu.om_save_queue, menu);
		getMenuInflater().inflate(R.menu.om_clear_queue, menu);
		getMenuInflater().inflate(R.menu.om_show_equalizer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
		case R.id.clear_queue:
			ContentHandler.queueFragment.clear();
			ContentHandler.controller.onStop();
			ContentHandler.songPosition = -1;
			if(ContentHandler.mplayer.isPlaying()) ContentHandler.mplayer.pause();
			return true;
		case R.id.toggle_shuffle:
			ContentHandler.mplayer.musicSrv.toggleShuffle();
			item.setChecked(!item.isChecked());
			return true;
		case R.id.save_queue:
			DialogHandler.saveQueue(ctx);
			return true;
		case R.id.shuffle_queue:
			ContentHandler.queueFragment.shuffle();
			return true;
		case R.id.show_equalizer:
			DialogHandler.showEqualizer(ctx);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Overridden functions of the tab listener
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	/*
	 * Overridden functions of the FragmentActivity
	 */
	@Override
	protected void onStart() {
		super.onStart();

		if(ContentHandler.mplayer.playIntent == null) {
			ContentHandler.mplayer.playIntent = new Intent(this, MusicService.class);
			bindService(ContentHandler.mplayer.playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(ContentHandler.mplayer.playIntent);
		}
	}

	@Override
	protected void onPause() {
		ContentHandler.isActivityPaused = true;
		save_configuration();

		ContentHandler.controller.onBackground();

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(ContentHandler.isActivityPaused) {			
			ContentHandler.isActivityPaused = false;
		}

		ContentHandler.isActivityInForeground = true;

		if(ContentHandler.controller != null) ContentHandler.controller.onForeground();
	}

	@Override
	protected void onStop() {
		ContentHandler.isActivityInForeground = false;

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		stopService(ContentHandler.mplayer.playIntent);
		unbindService(musicConnection);
		ContentHandler.mplayer.musicSrv = null;

		super.onDestroy();
	}
}
