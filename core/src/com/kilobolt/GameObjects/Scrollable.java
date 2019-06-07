package com.kilobolt.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Scrollable { //Scrollable is an object that is scrollable

	//Protected is like private, but it allows inheritance by subclasses
	protected Vector2 position;
	protected Vector2 velocity;
	protected int width;
	protected int height;
	protected boolean isScrolledLeft;
	
	public Scrollable(float x, float y, int width, int height, float scrollSpeed) 
	{
		//properties of Scrollable object dependent on specific object (Grass, pipe, etc)
		position = new Vector2(x,y);
		velocity = new Vector2(scrollSpeed,0);
		this.width = width;
		this.height = height;
		isScrolledLeft = false;
	}
	
	public void update(float delta)
	{
		position.add(velocity.cpy().scl(delta));
		
		//If the Scrollable object is no longer visible
		if (position.x + width < 0)
		{
			isScrolledLeft = true;
		}
		
	}
	
	//stop function
	public void stop()
	{
		velocity.x = 0;
	}
	
	//Reset function; needs @Override in subclass for more specific instructions
	public void reset(float newX)
	{
		position.x = newX;
		isScrolledLeft = false;
	}
	
	//Getter Functions
	
	public boolean isScrolledLeft()
	{
		return isScrolledLeft;		
	}
	
	public float getTailX()
	{
		return position.x + width;
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	
	
}
