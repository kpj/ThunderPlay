package com.kpj.thunderplay.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.kpj.thunderplay.gui.Song;

import android.content.Context;
import android.util.Log;

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
	
	public static void savePlaylist(Context ctx, String name, ArrayList<Song> playlist) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		dir.mkdirs();
		
		File fd = new File(dir, name);
		FileHandler.writeFile(ctx, fd, playlist);
	}
	
	public static ArrayList<Song> readPlaylist(Context ctx, String name) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		File fd = new File(dir, name);
		
		return (ArrayList<Song>) FileHandler.readFile(ctx, fd);
	}
	
	public static void writeFile(Context ctx, File file, Object obj) {
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
	
	public static void deletePlaylist(Context ctx, String name) {
		File dir = new File(ctx.getFilesDir(), playlistDir);
		dir.mkdirs();
		
		File fd = new File(dir, name);
		fd.delete();
	}
	
	public static Object readFile(Context ctx, File file) {
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
	
	public static ArrayList<String> getPlaylistNames(Context ctx) {
		String[] res =  new File(ctx.getFilesDir(), playlistDir).list();
		ArrayList<String> ichBinDoof = new ArrayList<String>();
		
		for(String s : res)
			ichBinDoof.add(s);
		
		return ichBinDoof;
	}
}
