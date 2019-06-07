package com.kilobolt.ZombieBird;

import com.badlogic.gdx.Game;
import com.kilobolt.Screens.GameScreen;
import com.kilobolt.ZBHelpers.AssetLoader;
import com.kilobolt.Screens.SplashScreen;

public class ZBGame extends Game{ //This creates GameScreen and loads assets,
								  //this is on top of the hierarchy

	@Override
	public void create() {
		AssetLoader.load();
		
		setScreen(new SplashScreen(this));
		System.out.println("ZBGame Created!");
		
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		AssetLoader.dispose();
	}

}