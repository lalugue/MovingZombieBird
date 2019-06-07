package com.kilobolt.GameWorld;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.kilobolt.GameObjects.Bird;
import com.kilobolt.ZBHelpers.*;
import com.kilobolt.GameObjects.*;
import com.kilobolt.TweenAccessors.*;
import com.kilobolt.ui.*;


public class GameRenderer { //this defines the GameRenderer, which will be created by GameScreen

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
	
	//for Tweening
	private TweenManager manager;
	private Value alpha = new Value();
	
	//Buttons
	private List<SimpleButton> menuButtons;
	
	public GameRenderer(GameWorld world, int gameHeight, int midPointY)
	{
		myWorld = world;
		
		this.gameHeight = gameHeight;
		this.midPointY = midPointY;
		System.out.println("Attempting to add menuButtons");
		//add buttons
		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
		System.out.println("menuButtons loaded");
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
		setUpTweens();
		
		
	}
	
	private void setUpTweens()
	{
		Tween.registerAccessor(Value.class, new ValueAccessor());
		
			manager = new TweenManager();
			Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad).start(manager);
		
	}
	
	
	public void render(float delta, float runTime)
	{
		//System.out.println("GameRenderer - render");
		
		
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
		//draw only if game is running
		
		
		
		//Draw the BitmapFont object
		//First convert integer to string
		String score = Integer.toString(myWorld.getScore());
		
		//Draw shadow of font
		AssetLoader.shadow.draw(batcher, score, (136 / 2) - (score.length() / 2), 12);
		//Then draw letters
		AssetLoader.font.draw(batcher, score, (136 / 2) - (score.length() / 2) - 1, 11);
		
		if (myWorld.isRunning())
		{
			drawBird(runTime);
			
		}
		else if(myWorld.isReady())
		{
			drawBird(runTime);
		}
		else if(myWorld.isMenu())
		{
			drawBirdCentered(runTime);
			drawMenuUI();
		}
		else if (myWorld.isGameOver())
		{
			drawBird(runTime);
			drawGameOver();
			drawTryAgain();
			
			
		}
		else if (myWorld.isHighScore())
		{
			drawBird(runTime);
			AssetLoader.shadow.draw(batcher, "High Score!", 19, 56);
            AssetLoader.font.draw(batcher, "High Score!", 18, 55);
            drawTryAgain();
		}
		
		
		
		
		/*
		// TEMPORARY CODE! We will fix this section later:
        
        if (myWorld.isReady()) {
            // Draw shadow first
            AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2)
                    - (42), 76);
            // Draw text
            AssetLoader.font.draw(batcher, "Touch me", (136 / 2)
                    - (42 - 1), 75);
        } else if (myWorld.isGameOver() || myWorld.isHighScore()) {
        	
        	if(myWorld.isGameOver())
        	{
                AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
                AssetLoader.font.draw(batcher, "Game Over", 24, 55);
                
                AssetLoader.shadow.draw(batcher, "High Score:", 23, 106);
                AssetLoader.font.draw(batcher, "High Score:", 22, 105);

                String highScore = AssetLoader.getHighScore() + "";
                
             // Draw shadow first
                AssetLoader.shadow.draw(batcher, highScore, (136 / 2)
                        - (3 * highScore.length()), 128);
                // Draw text
                AssetLoader.font.draw(batcher, highScore, (136 / 2)
                        - (3 * highScore.length() - 1), 127);
        	}                
                else {
                    AssetLoader.shadow.draw(batcher, "High Score!", 19, 56);
                    AssetLoader.font.draw(batcher, "High Score!", 18, 55);
                }
                
                AssetLoader.shadow.draw(batcher, "Try again?", 23, 76);
                AssetLoader.font.draw(batcher, "Try again?", 24, 75);
                
                
                
         }
        */
            
           
		
		
		//End SpriteBatch
		batcher.end();		
		drawTransition(delta);		
		
		
		
		
		
		
		
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
	{	/*
		//TODO: For later code is too long
		//Constants hardcoded here were derived using math (Ex: [skull width - pipe width] / 2 = [24 - 22] / 2 = presto! = 1)
		 batcher.draw(skullUp, pipe1.getX() - 1,
	                pipe1.getY() + pipe1.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe1.getX() - 1,
	                pipe1.getBarDown().getY(), 24, 14);

	        batcher.draw(skullUp, pipe2.getX() - 1,
	                pipe2.getY() + pipe2.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe2.getX() - 1,
	               pipe2.getBarDown().getY(), 24, 14);

	        batcher.draw(skullUp, pipe3.getX() - 1,
	                pipe3.getY() + pipe3.getHeight() - 14, 24, 14);
	        batcher.draw(skullDown, pipe3.getX() - 1,
	               pipe2.getBarDown().getY(), 24, 14);
	        */
		batcher.draw(skullUp, pipe1.getX() - 1,
                pipe1.getSkullUp().getY(), 24, 14);
        batcher.draw(skullDown, pipe1.getX() - 1,
                pipe1.getSkullDown().getY(), 24, 14);

        batcher.draw(skullUp, pipe2.getX() - 1,
                pipe2.getSkullUp().getY(), 24, 14);
        batcher.draw(skullDown, pipe2.getX() - 1,
               pipe2.getSkullDown().getY(), 24, 14);

        batcher.draw(skullUp, pipe3.getX() - 1,
                pipe3.getSkullUp().getY(), 24, 14);
        batcher.draw(skullDown, pipe3.getX() - 1,
               pipe3.getSkullDown().getY(), 24, 14);
	}
	
	private void drawPipes()
	{
		//TODO: For later code is too long
		/*
		 batcher.draw(bar, pipe1.getX(), pipe1.getY(), pipe1.getWidth(),
	                pipe1.getHeight());
	        batcher.draw(bar, pipe1.getX(), pipe1.getY() + pipe1.getHeight() + 45,
	                pipe1.getWidth(), midPointY + 66 - (pipe1.getHeight() + 45));
			*/
		
		 batcher.draw(bar, pipe1.getX(), pipe1.getBarUp().getY(), pipe1.getWidth(),
	                pipe1.getBarUp().getHeight());
	        batcher.draw(bar, pipe1.getX(), pipe1.getBarDown().getY(),
	                pipe1.getWidth(), pipe1.getBarDown().getHeight());
	        
	      
		
	        batcher.draw(bar, pipe2.getX(), pipe2.getBarUp().getY(), pipe2.getWidth(),
	                pipe2.getBarUp().getHeight());
	        batcher.draw(bar, pipe2.getX(), pipe2.getBarDown().getY(),
	                pipe2.getWidth(), pipe2.getBarDown().getHeight());

	        batcher.draw(bar, pipe3.getX(), pipe3.getY(), pipe3.getWidth(),
	                pipe3.getBarUp().getHeight());
	        batcher.draw(bar, pipe3.getX(), pipe3.getBarDown().getY(),
	                pipe3.getWidth(), pipe3.getBarDown().getHeight());
	}
	
	//Latest functions
	private void drawMenuUI()
	{
		batcher.draw(AssetLoader.zbLogo, 136 / 2 - 56, midPointY - 50, AssetLoader.zbLogo.getRegionWidth() / 1.2f, AssetLoader.zbLogo.getRegionHeight() / 1.2f);
		
		for(SimpleButton button : menuButtons)
		{
			button.draw(batcher);			
		}
	}
	
	private void drawBird(float runTime)
	{
	if (bird.shouldFlap())
	{
		batcher.draw(birdAnimation.getKeyFrame(runTime), bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

	}
	else
	{
		batcher.draw(birdMid, bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

	}
	}
	
	private void drawBirdCentered(float runTime)
	{
		batcher.draw(birdAnimation.getKeyFrame(runTime), 53, bird.getY() - 15, bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
		
	}
	
	private void drawGameOver()
	{
		AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
        AssetLoader.font.draw(batcher, "Game Over", 24, 55);
        
        AssetLoader.shadow.draw(batcher, "High Score:", 23, 106);
        AssetLoader.font.draw(batcher, "High Score:", 22, 105);

        String highScore = AssetLoader.getHighScore() + "";
        
     // Draw shadow first
        AssetLoader.shadow.draw(batcher, highScore, (136 / 2)
                - (3 * highScore.length()), 128);
        // Draw text
        AssetLoader.font.draw(batcher, highScore, (136 / 2)
                - (3 * highScore.length() - 1), 127);
	}
	
	private void drawTransition(float delta)
	{
		
		if (alpha.getValue() > 0)
		{
			//System.out.println("alpha.getValue() > 0");
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1,1,1,alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			
		}
	}
	
	private void drawTryAgain()
	{
		AssetLoader.shadow.draw(batcher, "Try again?", 23, 76);
        AssetLoader.font.draw(batcher, "Try again?", 24, 75);
	}
	
}
