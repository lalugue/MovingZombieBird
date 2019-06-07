
package com.kilobolt.ZBHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Preferences;

public class AssetLoader {
	
	public static Texture texture, logoTexture;	
	public static TextureRegion playButtonUp, playButtonDown, logo, zbLogo;
	
	public static TextureRegion bg,grass;
	
	public static Animation birdAnimation;
	public static TextureRegion bird,birdDown,birdUp;
	
	public static TextureRegion skullUp, skullDown, bar;
	
	public static Sound dead, coin, flap;
	
	public static BitmapFont font, shadow;
	
	public static Preferences prefs;
	
	public static void load()
	{
		//load textures
		texture = new Texture(Gdx.files.internal("texture.png"));
		//apply these constants when shrinking/stretching image
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		//load logo
		logoTexture = new Texture(Gdx.files.internal("logo.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		logo = new TextureRegion(logoTexture, 0, 0, 65, 75);
		
		zbLogo = new TextureRegion(texture, 0, 55, 135, 24);
		zbLogo.flip(false, true);
		
		//buttons
		playButtonUp = new TextureRegion(texture, 0, 83, 29, 16);
        playButtonDown = new TextureRegion(texture, 29, 83, 29, 16);
        playButtonUp.flip(false, true);
        playButtonDown.flip(false, true);
		
		//background
		bg = new TextureRegion(texture,0,0,136,43);
		bg.flip(false, true); //To do: check effects of flipping image (Ydown or Cartesian Coordinates by default?)
		
		//grass
		grass = new TextureRegion(texture,0,43,143,11);
		grass.flip(false, true);
		
		//Bird with wings down
		birdDown = new TextureRegion(texture,136,0,17,12);
		birdDown.flip(false,true);
		
		//Default Bird position		
		bird = new TextureRegion(texture,153,0,17,12);
		bird.flip(false,true);
		
		//Bird with wings up
		birdUp = new TextureRegion(texture,170,0,17,12);
		birdUp.flip(false,true);
		
		//Animation for the Flappy Bird!
		TextureRegion[] birds = {birdDown,bird,birdUp};
		birdAnimation = new Animation(0.06f,birds);
		birdAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		
		//skulls for pipes
		
		skullUp = new TextureRegion(texture,192,0,24,14);		
		//create upside down skull by flipping skullUp
		skullDown = new TextureRegion(skullUp);
		skullDown.flip(false, true);
		
		bar = new TextureRegion(texture, 136, 16, 22, 3);
        bar.flip(false, true);
        
        //load sounds
        dead = Gdx.audio.newSound(Gdx.files.internal("dead.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        flap = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
        
        //load fonts
        font = new BitmapFont(Gdx.files.internal("text.fnt"));
        font.getData().setScale(.25f,-.25f);
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadow.getData().setScale(.25f,-.25f); //negative value to flip y-axis (Recall y-down coordinates)
        
        //load high score
        prefs = Gdx.app.getPreferences("ZombieBird");
        //set default high score to 0
        if(!prefs.contains("highScore"))
        {
        	prefs.putInteger("highScore", 0);
        }
        
		
	}
	
		public static void dispose()
		{
			//We must always dispose of texture after use
			texture.dispose();
			dead.dispose();
			coin.dispose();
			flap.dispose();
			font.dispose();
			shadow.dispose();
		}
		
		//sets highScore
		public static void setHighScore(int val)
		{
			prefs.putInteger("highScore",val);
			prefs.flush();
		}
		
		//get highScore
		public static int getHighScore()
		{
			return prefs.getInteger("highScore");
		}
	
		
		
		
		
		
		
}


