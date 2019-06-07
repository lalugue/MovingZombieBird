package com.kilobolt.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Circle;
import com.kilobolt.ZBHelpers.AssetLoader;

public class Bird {
	
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	
	private float rotation;
	private int width;
	private int height;
	
	private Circle boundingCircle;
	
	private boolean isAlive;
	
	private float originalY;
	
		
	public Bird(float x, float y, int width, int height)
	{
		 this.width = width;
		 this.height = height;
		 position = new Vector2(x,y);
		 velocity = new Vector2(0,0);
		 acceleration = new Vector2(0,460);
		 boundingCircle = new Circle();
		 isAlive = true;
		 this.originalY = y;
	}
	
	public void update(float delta)
	{
		velocity.add(acceleration.cpy().scl(delta));
		
		if(velocity.y > 200)
		{
			velocity.y = 200; //terminal velocity
		}
		
		position.add(velocity.cpy().scl(delta));
		
		//Ceiling check
		if(position.y <= -13)
		{
			position.y = -13;
			velocity.y = 0;
		}
		
		//System.out.println("Bird height: " + this.getY());
		
		//Circle has radius of 6.5 units
		//Circle's center will be centered on bird, which is approximately (9,6)
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f); //position is on upperleft corner
		
		//Rotate counterclockwise
		if(velocity.y < 0)
		{
			rotation -= 600 * delta;
			
			//cap rotation
			if(rotation <= -20)
			{
				rotation = -20;
			}	
		}
		
		//Rotate clockwise
		if(this.isFalling() || !isAlive) //if falling or dead
		{
			rotation += 480 * delta;
			
			//cap rotation
			if(rotation > 90)
			{
				rotation = 90;
			}
		}
		
		
	}
	
	//Detects input
	public void onClick()
	{
		if(isAlive)
		{
		AssetLoader.flap.play();
		velocity.y = -140;
		}
	}
	
	//Detects if Bird is falling
	public boolean isFalling()
	{
		return velocity.y > 110; //note Y-down coordinates
	}
	
	//Detects if bird should be Flapping or not
	public boolean shouldFlap()
	{
		return (velocity.y < 70 && isAlive); //If bird is flying upwards at more than 70 pixels/sec
	}
	
	//Bird dies
	public void die()
	{
		isAlive = false;
		velocity.y = 0;
	}
	
	//Bird decelerates
	public void decelerate()
	{
		//Bird does not accelerate when dead (similar to poorly aerodynamic body)
		acceleration.y = 0;
	}
	
	//getter functions
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public Circle getBoundingCircle()
	{
		return boundingCircle;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	
	public void onRestart(int y)
	{
		rotation = 0;
		position.y = y;
		velocity.x = 0;
		velocity.y = 0;
		isAlive = true;
		acceleration.y = 460; //needs to be reset since it was set to 0 when dead
	}
	
	public void updateReady(float runTime)
	{
		position.y = 2 * (float)Math.sin(7 * runTime) + originalY;
	}
}
