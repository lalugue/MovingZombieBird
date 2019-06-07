package com.kilobolt.GameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;



public class CopyOfPipe extends Scrollable {
	
	private Random r;
	private Rectangle skullUp, skullDown, barUp, barDown;
	
	public static final int VERTICAL_GAP = 45;
	public static final int SKULL_WIDTH = 24;
	public static final int SKULL_HEIGHT = 11;
		
	private float groundY;
	
	
	
	int gap; //we need this for the update function below
	
	private boolean isScored;
	
	boolean northBound; 
	
	//invoke Pipe's constructor by invoking the parent/superclass's constructor (Scrollable is its parent)
	public CopyOfPipe(float x, float y, int width, int height, float scrollSpeed, float groundY) {
		
		super(x, y, width, height, scrollSpeed);
		
		
		//initialize random number
		r = new Random();
		skullUp = new Rectangle();
		skullDown = new Rectangle();
		barUp = new Rectangle();
		barDown = new Rectangle();
		this.groundY = groundY;
		
		velocity.y = r.nextInt(40 * 2) - 40;
		
		northBound = true;
		
		
	}
	
	@Override
	public void update(float delta)
	{
		
		super.update(delta);
		
		//set() function sets position of topleft corner and width and height of rectangle
		//this moves the rectangle itself
		
		//todo, height passed by ScrollHandler is height of Whole Pipe Object itself
		//code for height of barUp and barDown has to be modified
		
		barUp.set(position.x,position.y, width, height);
		barDown.set(position.x, position.y + height + VERTICAL_GAP, width, groundY - (position.y + height + VERTICAL_GAP)); //yPos is position of Grass
		
		
		//refer to handout for derivation of values
		gap = (SKULL_WIDTH - width) / 2;
		skullUp.set(position.x - gap, position.y + height - SKULL_HEIGHT, SKULL_WIDTH, SKULL_HEIGHT);
		skullDown.set(position.x - gap, barDown.y, SKULL_WIDTH, SKULL_HEIGHT);
		
		
		if((skullUp.getY() <= 0) && (this.velocity.y < 0))
		{
			System.out.println("bumped north! position: " + barUp.getY());
			velocity.y *= -1;
		}
		if(skullDown.getY() >= groundY && this.velocity.y > 0)
		{
			System.out.println("bumped south! position: " + barDown.getY());
			velocity.y *= -1;
		}
		
		
	}
	
	@Override
	public void reset(float newX)
	{
		//call Reset function of Scrollable
		super.reset(newX);
		
		//changing height to a random number
		height = r.nextInt(90) + 15;
		
		//reset isScored to score again
		isScored = false;
		
		//reset speed of pipe
		velocity.y = r.nextInt(80 * 2) - 80;
	}
	
	//getters
	public Rectangle getSkullUp()
	{
		return skullUp;
	}
	
	public Rectangle getSkullDown()
	{
		return skullDown;
	}
	
	public Rectangle getBarUp()
	{
		return barUp;
	}
	
	public Rectangle getBarDown()
	{
		return barDown;
	}
		
	
	public boolean collides(Bird bird)
	{
		if(position.x < bird.getX() + bird.getWidth()) //rightmost part of bird
		{
			return ( Intersector.overlaps(bird.getBoundingCircle(),barUp) || 
					Intersector.overlaps(bird.getBoundingCircle(), barDown) || 
					Intersector.overlaps(bird.getBoundingCircle(), skullUp) || 
					Intersector.overlaps(bird.getBoundingCircle(), skullDown));
		}
		
		return false;
	}
	
	public void setScore(boolean foo)
	{
		isScored = foo;
	}
	
	public boolean isScored()
	{
		return isScored;
	}
	
	public void onRestart(float x, float scrollSpeed)
	{
		velocity.x = scrollSpeed;
		reset(x); //reset was defined earlier
	}
	
	
	
}
