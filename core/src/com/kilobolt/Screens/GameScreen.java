package com.kilobolt.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.GameRenderer;
import com.kilobolt.ZBHelpers.InputHandler;

public class GameScreen implements Screen{

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
	
	public GameScreen() //this sets up the screen's dimension and creates the GameWorld and GameRenderer
	{
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		float gameWidth = 136; //this is always 136
		float gameHeight = screenHeight / (screenWidth/gameWidth); //this is math
		
		int midPointY = (int) (gameHeight/2); //typecast as int
		
		world = new GameWorld(midPointY);
		System.out.println("GameWorld loaded");		
		
		Gdx.input.setInputProcessor(new InputHandler(world, gameWidth/screenWidth, gameHeight/screenHeight));
		
		renderer = new GameRenderer(world, (int)gameHeight, midPointY);
		System.out.println("GameRenderer loaded");
		
		System.out.println("world, renderer, inputhandler loaded from GameScreen");
		System.out.println("screenHeight: " + screenHeight);
		//easier to think in terms of smaller fractions
		
	}
	
	
	@Override
	public void render(float delta) {
		
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
		
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("Resizing GameScreen");
		
	}

	@Override
	public void show() {
		System.out.println("Gamescreen - show called");
		
	}

	@Override
	public void hide() {
		System.out.println("Gamescreen - hide called");
		
	}

	@Override
	public void pause() {
		System.out.println("Gamescreen - puase called");
		
	}

	@Override
	public void resume() {
		System.out.println("Gamescree - resume called");
		
	}

	@Override
	public void dispose() {
		//blank
		
	}

}
