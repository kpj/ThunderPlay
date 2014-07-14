package com.kpj.thunderplay;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.gui.DialogHandler;
import com.kpj.thunderplay.gui.adapter.TabsPagerAdapter;
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

		// setup activity
		setContentView(R.layout.activity_main);
		registerForContextMenu(findViewById(R.id.activity_main));

		// tabbed view
		setupTabs();

		// initialize controller
		ContentHandler.mplayer.enableController();
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
		getMenuInflater().inflate(R.menu.om_save_queue, menu);
		getMenuInflater().inflate(R.menu.om_clear_queue, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
		case R.id.clear_queue:
			ContentHandler.queueFragment.clear();
			ContentHandler.mplayer.controller.myHide();
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
		ContentHandler.mplayer.controller.myHide();
		ContentHandler.isActivityPaused = true;

		FileHandler.writeObject(
				ctx,
				ContentHandler.queue_filename,
				ContentHandler.queue);
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(ContentHandler.isActivityPaused) {
			ContentHandler.mplayer.enableController();
			if(ContentHandler.mplayer.isPlaying())
				ContentHandler.mplayer.controller.show();
			
			ContentHandler.isActivityPaused = false;
		}

		ContentHandler.isActivityInForeground = true;
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
