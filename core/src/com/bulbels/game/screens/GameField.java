package com.bulbels.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.TextureManager;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.models.boosts.Boost;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.models.shapes.Square;
import com.bulbels.game.utils.AndroidHelper;

import java.util.Scanner;


public class GameField implements MyScreen{
	Bulbels game;
	AndroidHelper androidHelper;
	public static float bottomY, topY, coefficientFps, coefficientWidth, coefficientHeight;
	public AllBalls allBalls;
	public AllShapes allShapes;
	public Boost boost;
	public static int turn;
	public int k=0,amountOfBalls;
	long lastTime;
	float aimX,aimY, limitAimY, launchX;

	public boolean can;
	public boolean controlledAim;
	boolean canLaunch, launching, finish=true, isBottomTouch;
	TextureRegion aimTexture, gameBarTexture;
	OrthographicCamera camera;
	TextureAtlas atlas;
	Vector3 touchPos;
	Sprite ball;
	Rectangle rectangle, topBar, bottomBar;
	public GameField(Bulbels game) {
		this.game = game;
		androidHelper = game.androidHelper;
		Boost.gameField = this;
		try {
			if (androidHelper != null) androidHelper.makeToast("Hello, i do it!!!");
		}catch (Exception e){e.printStackTrace();}
		atlas = game.atlas;
		AllShapes.textureAtlas = atlas;
		coefficientWidth=Gdx.graphics.getWidth()/720f;
		game.font.getData().setScale(coefficientWidth);
		allBalls = new AllBalls();
		limitAimY = Gdx.graphics.getHeight()*2 / 3.0f;
		bottomY = Gdx.graphics.getHeight()/8f;
		topY = Gdx.graphics.getHeight()*.93f;
		AllBalls.startX = Gdx.graphics.getWidth()/2f;
		AllBalls.startY = bottomY+allBalls.getRadius();
		allBalls.createBall(AllBalls.startX,AllBalls.startY);
		allShapes = new AllShapes();
		nextTurn();

		game.font.setColor(Color.YELLOW);


		touchPos = new Vector3();
		gameBarTexture = atlas.findRegion("game_bar");
		aimTexture= atlas.findRegion("ball");
		bottomBar = new Rectangle(0,0,Gdx.graphics.getWidth(),bottomY);
		topBar = new Rectangle(0, topY, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-topY);

		//Тестовые:
		ball = new Sprite(atlas.findRegion("square"));
		ball.setX(250);
		ball.setY(600);
		ball.setColor(Color.GREEN);
		ball.setSize(AllShapes.width*1.2f,AllShapes.width*1.2f);
		rectangle = new Rectangle(200,300,300,300);

	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(.17f, .17f, .17f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

//		System.out.println("FPS "+(int)(1/delta));
		coefficientFps = delta/.016f;
		for (Ball b:allBalls.getArrayBalls())allShapes.checkCollision(b);
		game.batch.begin();


		allShapes.draw(game.batch);
		allBalls.drawBalls(game.batch);
		if (canLaunch)drawAim();

		game.batch.disableBlending();
		game.batch.draw(gameBarTexture, bottomBar.x, bottomBar.y, bottomBar.width, bottomBar.height);
		game.batch.draw(gameBarTexture,topBar.x,topBar.y,topBar.width,topBar.height);
		game.batch.enableBlending();

		game.font.draw(game.batch,"Шариков: "+amountOfBalls,Gdx.graphics.getWidth()/8f,(Gdx.graphics.getHeight()+topY)/1.97f);
		game.font.draw(game.batch,"Ход: "+turn,Gdx.graphics.getWidth()/2f,(Gdx.graphics.getHeight()+topY)/1.97f);
		game.batch.end();

		if (launching)launching();
		if (allBalls.inMotion())allBalls.moveAll();

		else if (!finish ){
			//System.out.println("check");
			if (!allBalls.allIn(AllBalls.startX,AllBalls.startY))allBalls.moveWithAnimationTo(AllBalls.startX,AllBalls.startY);
			else nextTurn();
		}
	}



	void topAim(float x, float y){

//		ball.moveTo(touchPos.x,touchPos.y);
//		ball.setY(touchPos.y);
//		System.out.println("x:"+touchPos.x+" y:"+touchPos.y);
		if ((finish||controlledAim) && y < limitAimY &&  Math.toDegrees(Math.atan((y - AllBalls.startY) / Math.abs((AllBalls.startX - x))))> 10) {
			aimX = x;
			aimY = y;
			canLaunch=true;
		} else {
			canLaunch = false;
		}
	}

	void bottomAim(float x, float y) {
		float factor = (limitAimY - AllBalls.startY) / AllBalls.startY;
		float cathetusX = AllBalls.startX - x;
		float cathetusY = AllBalls.startY - y;
		topAim(AllBalls.startX + cathetusX * factor, AllBalls.startY + cathetusY * factor);
	}

	public void touchUp(float x, float y){
		if (canLaunch&&!controlledAim) {
			canLaunch = false;
			launching = true;
			finish=false;
			if (boost!=null)boost.activate();
		}
	}

	public void touchDown(float x, float y, boolean start){
		touchPos.set(x,y,0);
		camera.unproject(touchPos);
		if (start){
			if (touchPos.y<bottomY)isBottomTouch=true;
			else isBottomTouch=false;
		}
		if (isBottomTouch)bottomAim(touchPos.x,touchPos.y);
		else topAim(touchPos.x,touchPos.y);
	}

	void drawAim(){
		int amount = 8;
		float stepX = (launchX - aimX)/amount;
		float stepY = (aimY - AllBalls.startY)/amount;
		float stepDiameter = (AllBalls.radius*.4f)/amount;
		for (int i = 1; i <= amount; i++) {
			float diameter = AllBalls.radius*.4f + stepDiameter * i *2;
			game.batch.draw(aimTexture,launchX-stepX*i - diameter/2,AllBalls.startY+stepY*i- diameter/2,diameter,diameter);
		}
	}

	void launching() {
		if (System.currentTimeMillis() - lastTime > 200) {
			lastTime = System.currentTimeMillis();
			allBalls.getArrayBalls().get(k).launch(aimX, aimY);
			k++;
			if (k > amountOfBalls - 1) {
				launching = false;
				controlledAim=false;
				k = 0;
			}
		}
	}

	void nextTurn(){
		finish = true;
		turn++;
	//	allBalls.addBall();
		amountOfBalls = allBalls.getArrayBalls().size;
		allShapes.generateNewLine();
		if (boost!=null) boost.end();
		launchX = AllBalls.startX;
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("resize");
		float aspectRatio =(float) height/width;
		camera = new OrthographicCamera(1f,1f*aspectRatio);
		camera.setToOrtho(false);

		Vector2 main2 = new Vector2(0,-6);
		Vector2 right = new Vector2(-2,-4);
		float x1 = 3,y1 =6, ballX=1, ballY=2;

		main2.setLength(right.len()*Math.abs(MathUtils.cosDeg(main2.angle(right))));
		float Cx = x1+main2.x;
		float Cy = y1+main2.y;
		right.set(ballX-Cx,ballY-Cy).setLength(3);
		System.out.println(main2.set(Cx + right.x,Cy + right.y));
		System.out.println(MathUtils.cosDeg(135));
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}

	@Override
	public void dispose () {
		atlas.dispose();
		allBalls.dispose();
		AllShapes.textureAtlas.dispose();
	}

	@Override
	public void back() {
		game.remove();
	}
}
