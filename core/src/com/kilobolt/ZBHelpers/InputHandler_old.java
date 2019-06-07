package com.kilobolt.ZBHelpers;

import com.badlogic.gdx.InputProcessor;
import com.kilobolt.GameObjects.Bird;
import com.kilobolt.GameWorld.GameWorld;

public class InputHandler_old implements InputProcessor
{
	
	private Bird myBird;
	GameWorld myWorld;
	
	public InputHandler_old(GameWorld myWorld)
	{
		this.myWorld = myWorld;
		myBird = myWorld.getBird();
		
	}
		
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		if(myWorld.isReady())
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
		// TODO Auto-generated method stub
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
	

}
