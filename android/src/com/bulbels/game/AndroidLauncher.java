package com.bulbels.game;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
		createStrings();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer=false;
		config.useCompass=false;
		config.hideStatusBar = true;
		/*Context themedContext = new ContextThemeWrapper(this, );
		View view = new View(themedContext);*/

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
	public String getString(String name) {
		return STRINGS.get(name);
	}

	void createStrings(){
		STRINGS.put("exit",getString(R.string.exit));
		STRINGS.put("confirmExit",getString(R.string.confirmExit));
		STRINGS.put("balls",getString(R.string.balls));
		STRINGS.put("play",getString(R.string.play));
		STRINGS.put("settings",getString(R.string.settings));
		STRINGS.put("turn",getString(R.string.turn));
		STRINGS.put("shop",getString(R.string.shop));
		STRINGS.put("bought",getString(R.string.bought));
		STRINGS.put("chosen",getString(R.string.chosen));
		STRINGS.put("effect_volume",getString(R.string.effect_volume));
		STRINGS.put("music_volume",getString(R.string.music_volume));
		STRINGS.put("general_volume",getString(R.string.general_volume));
		STRINGS.put("resume",getString(R.string.resume));
		STRINGS.put("menu",getString(R.string.menu));
		STRINGS.put("pause",getString(R.string.pause));
		STRINGS.put("game_over",getString(R.string.game_over));
		STRINGS.put("restart",getString(R.string.restart));
		STRINGS.put("score",getString(R.string.score));
	}

	@Override
	public void onBackPressed() {
		bulbels.back();
		System.out.println(getString(R.string.exit));
	}



}
