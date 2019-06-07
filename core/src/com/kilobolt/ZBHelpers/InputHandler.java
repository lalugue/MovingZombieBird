package com.kilobolt.ZBHelpers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;
import com.kilobolt.GameObjects.Bird;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.ui.*;
import com.badlogic.gdx.Input.Keys;

public class InputHandler implements InputProcessor
{

	private Bird myBird;
	GameWorld myWorld;
	
	private List<SimpleButton> menuButtons;
	
	private SimpleButton playButton;
	
	private float scaleFactorX;
	private float scaleFactorY;
	
	public InputHandler(GameWorld myWorld, float scaleFactorX, float scaleFactorY)
	{
		this.myWorld = myWorld;
		myBird = myWorld.getBird();
		
		int midPointY = myWorld.getMidPointY();
		
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		
		menuButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() / 2), midPointY + 50, 29, 16, AssetLoader.playButtonUp, AssetLoader.playButtonDown);
		menuButtons.add(playButton);
		
	}
		
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
			
	if(myWorld.isMenu())
	{
		playButton.isTouchDown(screenX, screenY);
	}
	else if(myWorld.isReady())
		{
			myWorld.start();
		}
		
		myBird.onClick();
		
		if(myWorld.isGameOver() || myWorld.isHighScore()) 
		{
			//if game over or high score, since game ends in either gameover or highscore state
			myWorld.restart();
		}
		return true;
	}

	
	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		
		if(myWorld.isMenu())
		{
			if(playButton.isTouchUp(screenX, screenY))
			{
				myWorld.ready();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private int scaleX(int screenX)
	{
		return (int) (screenX * scaleFactorX); //screenX * gameWidth/screenWidth
		//to shrink the length of screenX to maintain coordinate system of gameWorld
		/*
		 * Example: if tablet were 1360 pixels wide, and we touched the middle of its screen (680px)
		 * scaleX will scale the touchData to gameWorld coordinates
		 * 680px * gameWidth/screenWidth = 680 * 136/1360 = 680 * 1/10
		 * = 68 pixels in gameWorld coordinates, which is also middle of gameWorld coordinates
		 */
	}
	
	private int scaleY(int screenY)
	{
		return (int) (screenY * scaleFactorY);
	}
	
	public List<SimpleButton> getMenuButtons()
	{
		return menuButtons;
	}
	

}
