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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.kpj.thunderplay.fs.FileHandler;
import com.kpj.thunderplay.gui.DialogHandler;
import com.kpj.thunderplay.gui.Song;
import com.kpj.thunderplay.gui.TabsPagerAdapter;
import com.kpj.thunderplay.music.MusicPlayer;
import com.kpj.thunderplay.music.MusicService;
import com.kpj.thunderplay.music.MusicService.MusicBinder;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private Context ctx = this;

	private MusicPlayer mplayer;

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	private String[] tabs = { "All Songs", "Queue", "Playlists" };

	private boolean isActivityInForeground = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ContentHandler.ctx = this;

		mplayer = new MusicPlayer(this);

		// tabbed view
		viewPager = (ViewPager) findViewById(R.id.activity_main);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(this, getFragmentManager());

		registerForContextMenu(findViewById(R.id.activity_main));

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		for (String tab_name : tabs) {
			Tab cur = actionBar.newTab().setText(tab_name).setTabListener(this);
			//cur.setTag(tab_name.toString().toLowerCase(Locale.getDefault()).replaceAll(" ", "_"));

			actionBar.addTab(cur);
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

		// enable controller
		mplayer.setController();
	}

	public void songlist_onclick(View view){
		int ind = Integer.parseInt(view.getTag().toString());
		ContentHandler.queueFragment.addSong(ContentHandler.allsongsFragment.getSongAt(ind));
	}

	public void playqueue_onclick(View view) {
		mplayer.musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
		mplayer.musicSrv.playSong();

		if(mplayer.playbackPaused) {
			mplayer.setController();
			mplayer.playbackPaused = false;
		}

		if(isActivityInForeground) {
			mplayer.controller.show(0);
		}
	}

	public void playlistlist_onclick(View view) {
		int ind = Integer.parseInt(view.getTag().toString());

		String plname = ContentHandler.playlists.get(ind);
		//ContentHandler.queue = FileHandler.readPlaylist(ctx, plname);
		//ContentHandler.queueFragment.update();
		// why does the above not work?

		ContentHandler.queueFragment.clear();
		for(Song s : FileHandler.readPlaylist(ctx, plname)) {
			ContentHandler.queueFragment.addSong(s);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(mplayer.playIntent == null){
			mplayer.playIntent = new Intent(this, MusicService.class);
			bindService(mplayer.playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(mplayer.playIntent);
		}
	}

	private ServiceConnection musicConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;

			mplayer.musicSrv = binder.getService();
			mplayer.musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mplayer.musicBound = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toggle_shuffle, menu);
		getMenuInflater().inflate(R.menu.save_queue, menu);
		getMenuInflater().inflate(R.menu.clear_queue, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
		case R.id.clear_queue:
			ContentHandler.queueFragment.clear();
			mplayer.pause();
			mplayer.controller.myHide();
			return true;
		case R.id.toggle_shuffle:
			mplayer.musicSrv.toggleShuffle();
			item.setChecked(!item.isChecked());
			return true;
		case R.id.save_queue:
			DialogHandler.saveQueue(ctx);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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

	@Override
	protected void onPause(){
		super.onPause();

		mplayer.paused = true;
	}

	@Override
	protected void onResume(){
		super.onResume();

		if(mplayer.paused){
			mplayer.setController();
			mplayer.paused = false;
		}

		isActivityInForeground = true;
	}

	@Override
	protected void onStop() {
		super.onStop();

		mplayer.controller.hide();
		isActivityInForeground = false;

		FileHandler.writeObject(
			ctx,
			ContentHandler.queue_filename,
			ContentHandler.queue
		);
	}

	@Override
	protected void onDestroy() {
		stopService(mplayer.playIntent);
		mplayer.musicSrv = null;

		super.onDestroy();
	}
}
