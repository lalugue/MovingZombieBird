package com.kilobolt.GameWorld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.kilobolt.GameObjects.Bird;
import com.kilobolt.ZBHelpers.AssetLoader;
import com.kilobolt.GameObjects.*;


public class GameRenderer_old2 { //this defines the GameRenderer, which will be created by GameScreen

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	
	private int midPointY;
	private int gameHeight;
	
	private SpriteBatch batcher;
	
	//Game Objects
	private Bird bird;
	private ScrollHandler scroller;
	private Grass frontGrass, backGrass;
	private Pipe pipe1, pipe2, pipe3;
	private Background foreg, backg;
	
	//Game Assets
	private TextureRegion bg, grass;
	private Animation<TextureRegion> birdAnimation;
	private TextureRegion birdMid, birdDown, birdUp;
	private TextureRegion skullUp, skullDown, bar;
	
	public GameRenderer_old2(GameWorld world, int gameHeight, int midPointY)
	{
		myWorld = world;
		
		this.gameHeight = gameHeight;
		this.midPointY = midPointY;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true,136,gameHeight);
		
		batcher = new SpriteBatch();
		//Attach batcher to camera
		batcher.setProjectionMatrix(cam.combined);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		//initiate the Game Objects and Assets here
		initGameObjects();
		initAssets();
	}
	
	
	public void render(float runTime)
	{
		System.out.println("GameRenderer - render");
		
		
		//Fill screen with black background color, prevents flickering				
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Begin ShapeRenderer
		shapeRenderer.begin(ShapeType.Filled);
		
		//Draw Background color
		shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
		shapeRenderer.rect(0, 0, 136, midPointY + 66); //a rectangle of 136 by height of midpoint plus 66
													 //(doesnt fill whole screen)
		
		//Draw Grass
		shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
		shapeRenderer.rect(0, midPointY + 66, 136, 11);
		
		//Draw Dirt
		shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
		shapeRenderer.rect(0, midPointY + 77, 136, 52);
		
		//Note to self: Visualize layers of background, grass, and dirt on screen
		
		//End ShapeRenderer
		shapeRenderer.end();
		
		
		//Begin SpriteBatch
		batcher.begin();
		//Disable transparency
		//This is good for performance when drawing images
		//that do not require transparency
		batcher.disableBlending();
		//batcher.draw(bg, 0, midPointY + 23, 136, 43); //draws the background
		
		//0. Draw Background
		drawBackground();
		
		//1. Draw Grass
		drawGrass();
		
		//2. Draw Pipes
		drawPipes();
			
		//Enable transparency again, because the skulls and bird needs transparency
		batcher.enableBlending();
		
		//3. Draw Skulls
		drawSkulls();
		
		//Draw bird at its coordinates. Retrieve the Animation object from AssetLoader
		//Pass in runTime variable to get current frame, provided if it should flap
		
		if (bird.shouldFlap())
		{
			batcher.draw(birdAnimation.getKeyFrame(runTime), bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

		}
		else
		{
			batcher.draw(birdMid, bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

		}
		
				
		//End SpriteBatch
		batcher.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(bird.getBoundingCircle().x, bird.getBoundingCircle().y, bird.getBoundingCircle().radius);
		
		 /*
         * Excuse the mess below. Temporary code for testing bounding
         * rectangles.
         */
        // Bar up for pipes 1 2 and 3
        shapeRenderer.rect(pipe1.getBarUp().x, pipe1.getBarUp().y,
                pipe1.getBarUp().width, pipe1.getBarUp().height);
        shapeRenderer.rect(pipe2.getBarUp().x, pipe2.getBarUp().y,
                pipe2.getBarUp().width, pipe2.getBarUp().height);
        shapeRenderer.rect(pipe3.getBarUp().x, pipe3.getBarUp().y,
                pipe3.getBarUp().width, pipe3.getBarUp().height);

        // Bar down for pipes 1 2 and 3
        shapeRenderer.rect(pipe1.getBarDown().x, pipe1.getBarDown().y,
                pipe1.getBarDown().width, pipe1.getBarDown().height);
        shapeRenderer.rect(pipe2.getBarDown().x, pipe2.getBarDown().y,
                pipe2.getBarDown().width, pipe2.getBarDown().height);
        shapeRenderer.rect(pipe3.getBarDown().x, pipe3.getBarDown().y,
                pipe3.getBarDown().width, pipe3.getBarDown().height);

        // Skull up for Pipes 1 2 and 3
        shapeRenderer.rect(pipe1.getSkullUp().x, pipe1.getSkullUp().y,
                pipe1.getSkullUp().width, pipe1.getSkullUp().height);
        shapeRenderer.rect(pipe2.getSkullUp().x, pipe2.getSkullUp().y,
                pipe2.getSkullUp().width, pipe2.getSkullUp().height);
        shapeRenderer.rect(pipe3.getSkullUp().x, pipe3.getSkullUp().y,
                pipe3.getSkullUp().width, pipe3.getSkullUp().height);

        // Skull down for Pipes 1 2 and 3
        shapeRenderer.rect(pipe1.getSkullDown().x, pipe1.getSkullDown().y,
                pipe1.getSkullDown().width, pipe1.getSkullDown().height);
        shapeRenderer.rect(pipe2.getSkullDown().x, pipe2.getSkullDown().y,
                pipe2.getSkullDown().width, pipe2.getSkullDown().height);
        shapeRenderer.rect(pipe3.getSkullDown().x, pipe3.getSkullDown().y,
                pipe3.getSkullDown().width, pipe3.getSkullDown().height);

		
		shapeRenderer.end();
		
		
		
		
		
	}
	
	private void initGameObjects()
	{
		bird = myWorld.getBird();
		scroller = myWorld.getScrollHandler();
		frontGrass = scroller.getFrontGrass();
		backGrass = scroller.getBackGrass();
		pipe1 = scroller.getPipe1();
		pipe2 = scroller.getPipe2();
		pipe3 = scroller.getPipe3(); //why not make pipe[] array?
		foreg = scroller.getForeGround();
		backg = scroller.getBackGround();
	}
	
	private void initAssets()
	{
		bg = AssetLoader.bg;
		grass = AssetLoader.grass;
		birdAnimation = AssetLoader.birdAnimation;
		birdMid = AssetLoader.bird;
		birdUp = AssetLoader.birdUp;
		birdDown = AssetLoader.birdDown;
		skullUp = AssetLoader.skullUp;
		skullDown = AssetLoader.skullDown;
		bar = AssetLoader.bar;
		
		
	}
	
	private void drawBackground() //EDIT HERE
	{
		//Draw the grass
		batcher.draw(bg,foreg.getX(), midPointY + 23, foreg.getWidth(), foreg.getHeight());
		batcher.draw(bg,backg.getX(), midPointY + 23, backg.getWidth(), backg.getHeight());
	}
	
	private void drawGrass()
	{
		//Draw the grass
		batcher.draw(grass,frontGrass.getX(), frontGrass.getY(), frontGrass.getWidth(), frontGrass.getHeight());
		batcher.draw(grass,backGrass.getX(), backGrass.getY(), backGrass.getWidth(), backGrass.getHeight());
	}
	
	private void drawSkulls()
	{
		//TODO: For later code is too long
		 batcher.draw(skullUp, pipe1.getX() - 1,
	                pipe1.getY() + pipe1.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe1.getX() - 1,
	                pipe1.getY() + pipe1.getHeight() + 45, 24, 14);

	        batcher.draw(skullUp, pipe2.getX() - 1,
	                pipe2.getY() + pipe2.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe2.getX() - 1,
	                pipe2.getY() + pipe2.getHeight() + 45, 24, 14);

	        batcher.draw(skullUp, pipe3.getX() - 1,
	                pipe3.getY() + pipe3.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe3.getX() - 1,
	                pipe3.getY() + pipe3.getHeight() + 45, 24, 14);
	}
	
	private void drawPipes()
	{
		//TODO: For later code is too long
		 batcher.draw(bar, pipe1.getX(), pipe1.getY(), pipe1.getWidth(),
	                pipe1.getHeight());
	        batcher.draw(bar, pipe1.getX(), pipe1.getY() + pipe1.getHeight() + 45,
	                pipe1.getWidth(), midPointY + 66 - (pipe1.getHeight() + 45));

	        batcher.draw(bar, pipe2.getX(), pipe2.getY(), pipe2.getWidth(),
	                pipe2.getHeight());
	        batcher.draw(bar, pipe2.getX(), pipe2.getY() + pipe2.getHeight() + 45,
	                pipe2.getWidth(), midPointY + 66 - (pipe2.getHeight() + 45));

	        batcher.draw(bar, pipe3.getX(), pipe3.getY(), pipe3.getWidth(),
	                pipe3.getHeight());
	        batcher.draw(bar, pipe3.getX(), pipe3.getY() + pipe3.getHeight() + 45,
	                pipe3.getWidth(), midPointY + 66 - (pipe3.getHeight() + 45));
	}
	
}
