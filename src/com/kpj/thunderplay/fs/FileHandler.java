package com.kpj.thunderplay.fs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class FileHandler {

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
}
