package com.kpj.thunderplay.music;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {
	public MusicController(Context c){
		super(c);
	}
	
	@Override
	public void hide() {}
	
	public void myHide() {
		super.hide();
	}
}