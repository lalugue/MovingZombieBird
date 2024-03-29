package com.kilobolt.GameWorld;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameRenderer_old {

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	
	public GameRenderer_old(GameWorld world)
	{
		myWorld = world;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true,136,204);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
	}
	
	
	public void render()
	{
		System.out.println("GameRenderer - render");
	
		
		/*
		// 1. Draw black background to prevent flickering
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// 2. Draw Filled Rectangle
		
		//Tell shapeRenderer to draw filled shapes from now on
		shapeRenderer.begin(ShapeType.Filled);
		
		//Choose this color when in full opacity
		shapeRenderer.setColor(87/255.0f,109/255.0f,120/255.0f,1);
		
		//Draw the rectangle from myWorld using ShapeType.Filled
		shapeRenderer.rect(myWorld.getRect().x, myWorld.getRect().y, myWorld.getRect().width, myWorld.getRect().height);
		
		//shapeRenderer must finish rendering
		shapeRenderer.end();
		
		// 3. Draw the rectangle's outline
		
		//shapeRenderer draws the outline using following shape
		shapeRenderer.begin(ShapeType.Line);
		
		//choose this color at full opacity
		shapeRenderer.setColor(255/255.0f,109/255.0f,120/255.0f,1);
		
		//draw the rectangle from myWorld using the Line
		shapeRenderer.rect(myWorld.getRect().x,myWorld.getRect().y,myWorld.getRect().width,myWorld.getRect().height);
		
		shapeRenderer.end();
		
		//note: whatever shapeRenderer starts drawing must end
		//define type of shape, color, then what to draw
		 
		 */
		
		
	}
	
}
