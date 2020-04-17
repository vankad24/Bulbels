package com.bulbels.game;

import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bulbels.game.Bulbels;
import com.bulbels.game.utils.AndroidHelper;

public class AndroidLauncher extends AndroidApplication implements AndroidHelper{

	Thread thread;
	Bulbels bulbels;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer=false;
		config.useCompass=false;
		//config.hideStatusBar = true;
		bulbels = new Bulbels(this);
		/*Bulbels bulbels = new Bulbels(this);
		thread = new Thread(bulbels);
		thread.start();
		thread.setPriority(Thread.MAX_PRIORITY);*/


		initialize(bulbels, config);
	}

	@Override
	public void printMessage(String title, String text) {

	}

	@Override
	public void makeToast(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public String getSt(int id) {
		return getString(id);
	}

	@Override
	public void onBackPressed() {
		bulbels.back();
	}

}
