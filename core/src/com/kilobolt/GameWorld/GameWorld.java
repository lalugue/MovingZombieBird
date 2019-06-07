package com.kilobolt.GameWorld;

import com.kilobolt.GameObjects.*;
import com.kilobolt.ZBHelpers.AssetLoader;
import com.kilobolt.GameObjects.Bird;

public class GameWorld {
	
	private Bird bird;
	private ScrollHandler scroller; //scrollhandler containts the pipes, grass, background
			
	int score = 0;
	public int midPointY;
	private float runTime;
	
	public enum GameState
	{
		READY, RUNNING, GAMEOVER, HIGHSCORE, MENU
	}
	
	private GameState currentState;
	
	
	public GameWorld(int midPointY)
	{
		currentState = GameState.MENU;
		bird = new Bird(33, midPointY - 5,17,12);
		
		System.out.println("midPoint Y: " + midPointY);
		System.out.println("midPointY + 66: " + (midPointY + 66));
		
		scroller = new ScrollHandler(this, midPointY + 66);		
		//currentState = GameState.READY;
		this.midPointY = midPointY;
		
	}
	
	public void update(float delta)
	{
		runTime += delta;
		
		switch(currentState)
		{
		case READY:
		case MENU:
			updateReady(delta);			
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		case GAMEOVER:
			updateRunning(delta);
			break;
		default:
			break;
		}
	}
	
	public void updateRunning(float delta)
	{
		//cap delta time
		if (delta > .15f)
		{
			delta = .15f;
		}
		
		
		bird.update(delta);
		scroller.grassUpdate(delta);
		scroller.update(delta);
		
		//these are 2 possible death scenarios
		if(scroller.collides(bird) && bird.isAlive())
		{
			//run game over sequence
			bird.die();
			scroller.stop();
			AssetLoader.dead.play();
			
		}	
		
		if (bird.getY() + 13f >= scroller.getFrontGrass().getY()) //recall diameter of circle is 13 pixels
		{ //if bird is on the ground
			
			if(bird.isAlive()) //todo, improve death scenario structure
			{
			AssetLoader.dead.play();
			}
			
			scroller.stop();
			bird.die();
			bird.decelerate();	
			currentState = GameState.GAMEOVER;
			
			if(score > AssetLoader.getHighScore())
			{
				AssetLoader.setHighScore(score);
				currentState = GameState.HIGHSCORE;
			}
			
		}
		
		
				
	}
	
	private void updateReady(float delta)
	{
		
		scroller.grassUpdate(delta);
		bird.updateReady(runTime);
		
	}
	
	public void restart()
	{
		currentState = GameState.READY; //make game ready
		score = 0; //reset score
		bird.onRestart(midPointY - 5); //respawn bird
		scroller.onRestart(); //respawn scrollers
		
		
	}
	
	public void start()
	{
		currentState = GameState.RUNNING;
	}
	
	public void ready()
	{
		currentState = GameState.READY;
	}
	
	
	
	public Bird getBird()
	{
		return bird;
	}
	
	public ScrollHandler getScrollHandler()
	{
		return scroller;
	}
	
	public void addScore(int foo)
	{
		score += foo;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public boolean isReady()
	{
		return currentState == GameState.READY;
	}
	
	public boolean isGameOver()
	{
		return currentState == GameState.GAMEOVER;
	}
	
	public boolean isHighScore()
	{
		return currentState == GameState.HIGHSCORE;
	}
	
	public boolean isMenu()
	{
		return currentState == GameState.MENU;
	}
	
	public int getMidPointY()
	{
		return midPointY;
	}
	
	public boolean isRunning()
	{
		return currentState == GameState.RUNNING;
	}


}
