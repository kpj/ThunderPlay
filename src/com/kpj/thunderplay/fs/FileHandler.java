package com.kpj.thunderplay.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

import com.kpj.thunderplay.data.Song;

public class FileHandler {
	private final static String playlistDir = "playlists"; 


	public static void writeObject(Context ctx, String filename, Object obj) {
		FileOutputStream fos;
		ObjectOutputStream oos;

		try {
			fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);

			oos.writeObject(obj);

			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeObject(Context ctx, File file, Object obj) {
		FileOutputStream fos;
		ObjectOutputStream oos;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);

			oos.writeObject(obj);

			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object readObject(Context ctx, String filename) {
		Object obj = null;

		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = ctx.openFileInput(filename);
			ois = new ObjectInputStream(fis);

			obj = ois.readObject();

			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static Object readObject(Context ctx, File file) {
		Object obj = null;

		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);

			obj = ois.readObject();

			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static void savePlaylist(Context ctx, String name, ArrayList<Song> playlist) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		dir.mkdirs();

		File fd = new File(dir, name);
		FileHandler.writeObject(ctx, fd, playlist);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Song> readPlaylist(Context ctx, String name) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		File fd = new File(dir, name);

		return (ArrayList<Song>) FileHandler.readObject(ctx, fd);
	}

	public static void deletePlaylist(Context ctx, String name) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		dir.mkdirs();

		File fd = new File(dir, name);
		fd.delete();
	}

	public static ArrayList<String> getPlaylistNames(Context ctx) {
		String[] res =  new File(ctx.getFilesDir(), playlistDir).list();
		ArrayList<String> ichBinDoof = new ArrayList<String>();

		if(res != null) {
			for(String s : res) {
				ichBinDoof.add(s);
			}
		}

		return ichBinDoof;
	}
}
