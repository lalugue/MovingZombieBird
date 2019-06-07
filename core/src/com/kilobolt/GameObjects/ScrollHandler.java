package com.kilobolt.GameObjects;

import com.badlogic.gdx.Gdx;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.ZBHelpers.AssetLoader;


public class ScrollHandler { 
	//Scrollhandler handles scrollable objects, such as whether they reach the end
	//It updates the scrollable objects, which move themselves

	private Grass frontGrass, backGrass;
	private Pipe pipe1, pipe2, pipe3;
	private Background fg,bg;
	
	//These constants determine scroll speed and gap between each pair of pipes
	public static final int SCROLL_SPEED = -59;
	public static final int PIPE_GAP = 49;
	
	//store instance of Game World
	private GameWorld gameWorld;
	
	public ScrollHandler(GameWorld gameWorld, float yPos)
	{
		
		frontGrass = new Grass(0, yPos, 143, 11, SCROLL_SPEED); //visualize this
		backGrass = new Grass(frontGrass.getTailX(), yPos, 143, 11, SCROLL_SPEED);
		
		System.out.println("yPos: " + yPos);
		System.out.println("ScrollHandler loaded");
		
		//old
		/*
		pipe1 = new Pipe(210, 40, 22, 60, SCROLL_SPEED, yPos);
		pipe2 = new Pipe(pipe1.getTailX() + PIPE_GAP, 40, 22, 70, SCROLL_SPEED, yPos);
		pipe3 = new Pipe(pipe2.getTailX() + PIPE_GAP, 40, 22, 60, SCROLL_SPEED, yPos);
		*/
		/*
		pipe1 = new Pipe(210, 40, 22, Gdx.graphics.getHeight(), SCROLL_SPEED, yPos);
		pipe2 = new Pipe(pipe1.getTailX() + PIPE_GAP, 40, 22, 70, SCROLL_SPEED, yPos);
		pipe3 = new Pipe(pipe2.getTailX() + PIPE_GAP, 40, 22, 60, SCROLL_SPEED, yPos);
		*/
		pipe1 = new Pipe(210, -(Gdx.graphics.getHeight()) + gameWorld.getMidPointY()/2, 22, Gdx.graphics.getHeight() , SCROLL_SPEED, yPos);
		pipe2 = new Pipe(pipe1.getTailX() + PIPE_GAP, -(Gdx.graphics.getHeight()) + gameWorld.getMidPointY()/2, 22, Gdx.graphics.getHeight() , SCROLL_SPEED, yPos);
		pipe3 = new Pipe(pipe2.getTailX() + PIPE_GAP, -(Gdx.graphics.getHeight()) + gameWorld.getMidPointY()/2, 22, Gdx.graphics.getHeight() , SCROLL_SPEED, yPos);
		
		fg = new Background(0, 0, 136, 43, -5); //foreground
		bg = new Background(fg.getTailX(), 0, 136, 43, -5);
		
		this.gameWorld = gameWorld;
	}
	
	public void grassUpdate(float delta)
	{
		frontGrass.update(delta);
		backGrass.update(delta);
		fg.update(delta);
		bg.update(delta);
		
		if(frontGrass.isScrolledLeft())
		{
			frontGrass.reset(backGrass.getTailX());
		}
		else if(backGrass.isScrolledLeft())
		{
			backGrass.reset(frontGrass.getTailX());
		}
		
		//Check for background
		if(fg.isScrolledLeft())
		{
			fg.reset(bg.getTailX());
		}
		else if(bg.isScrolledLeft())
		{
			bg.reset(fg.getTailX());
		}
	}
	
	
	public void update(float delta)
	{
		//Update objects
		
		pipe1.update(delta);
		pipe2.update(delta);
		pipe3.update(delta);
		
		System.out.println("Pipe1: " + pipe1.getBarUp().getY() + " Skull: " + pipe1.getSkullUp().getY());
		System.out.println("Pipe3: " + pipe3.getBarUp().getY() + " Skull: " + pipe3.getSkullUp().getY());
		
		//Check if pipes have scrolled all the way to the left
		//and reset the pipes that have
		if (pipe1.isScrolledLeft())
		{
			pipe1.reset(pipe3.getTailX() + PIPE_GAP);
		}
		else if (pipe2.isScrolledLeft())
		{
			pipe2.reset(pipe1.getTailX() + PIPE_GAP);
		}
		else if (pipe3.isScrolledLeft())
		{
			pipe3.reset(pipe2.getTailX() + PIPE_GAP);
		}
		
		//Check also for grass
		
		
	}
	
	//getters for each instance variable (grass and pipes)
	public Background getForeGround()
	{
		return fg;
	}
	
	public Background getBackGround()
	{
		return bg;
	}
	
	public Grass getFrontGrass()
	{
		return frontGrass;
	}
	
	public Grass getBackGrass()
	{
		return backGrass;
	}
	
	public Pipe getPipe1()
	{
		return pipe1;
	}
	
	public Pipe getPipe2()
	{
		return pipe2;
	}
	
	public Pipe getPipe3()
	{
		return pipe3;
	}
	
	public boolean collides(Bird bird)
	{
		float birdEdge = bird.getX() + 13;
		
		if(!pipe1.isScored() && pipe1.getX() + (pipe1.getWidth() / 2) < birdEdge)
		{
			gameWorld.addScore(1);
			pipe1.setScore(true);
			AssetLoader.coin.play();
		}
		
		else if(!pipe2.isScored() && pipe2.getX() + (pipe2.getWidth() / 2) < birdEdge)
		{
			gameWorld.addScore(1);
			pipe2.setScore(true);
			AssetLoader.coin.play();
		}
		
		else if(!pipe3.isScored() && pipe3.getX() + (pipe3.getWidth() / 2) < birdEdge)
		{
			gameWorld.addScore(1);
			pipe3.setScore(true);
			AssetLoader.coin.play();
		}
		
		
		return (pipe1.collides(bird) || pipe2.collides(bird) || pipe3.collides(bird));
	}
	
	public void stop()
	{
		frontGrass.stop();
		backGrass.stop();
		pipe1.stop();
		pipe2.stop();
		pipe3.stop();
	}
	
	private void addScore(int increment)
	{
		gameWorld.addScore(increment);
	}
	
	public void onRestart()
	{
		frontGrass.onRestart(0, SCROLL_SPEED);
		backGrass.onRestart(frontGrass.getTailX(), SCROLL_SPEED);
		pipe1.onRestart(201, SCROLL_SPEED);
		pipe2.onRestart(pipe1.getTailX() + PIPE_GAP, SCROLL_SPEED);
		pipe3.onRestart(pipe2.getTailX() + PIPE_GAP, SCROLL_SPEED);
	}
	
	
	
	
	
	
}
