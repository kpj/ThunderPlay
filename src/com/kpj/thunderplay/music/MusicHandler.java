package com.kpj.thunderplay.music;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.kpj.thunderplay.ContentHandler;
import com.kpj.thunderplay.R;
import com.kpj.thunderplay.data.Song;


public class MusicHandler {
	public static ArrayList<Song> getSongList(Context c) {
		ArrayList<Song> songList = new ArrayList<Song>();

		ContentResolver musicResolver = c.getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

		if(musicCursor != null && musicCursor.moveToFirst()) {
			int idColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media._ID);
			int titleColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media.TITLE);
			int artistColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media.ARTIST);
			int albumColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media.ALBUM);

			int pathColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media.DATA);
			int ismusicColumn = musicCursor.getColumnIndex(
					android.provider.MediaStore.Audio.Media.IS_MUSIC);

			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisAlbum = musicCursor.getString(albumColumn);
				String thisPath = musicCursor.getString(pathColumn);

				if(musicCursor.getInt(ismusicColumn) != 0)
					songList.add(new Song(
							thisId, 
							thisTitle, 
							thisArtist, 
							thisAlbum, 
							thisPath));
			}
			while (musicCursor.moveToNext());
		}

		musicCursor.close();
		return songList;
	}

	public static Bitmap getAlbumart(String path) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		byte[] data = mmr.getEmbeddedPicture();

		Bitmap cover = null;
		if(data != null) {
			cover = BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			cover = BitmapFactory.decodeResource(ContentHandler.ctx.getResources(), R.drawable.missing_cover);
		}

		return cover;
	}
}
