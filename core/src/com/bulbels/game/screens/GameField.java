package com.bulbels.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.MyActor;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.models.boosts.Boost;
import com.bulbels.game.models.boosts.GhostBoost;
import com.bulbels.game.models.boosts.SizeBoost;
import com.bulbels.game.models.boosts.SmallBoost;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.utils.AndroidHelper;
import com.bulbels.game.utils.ButtonPanel;
import com.bulbels.game.utils.MySprite;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bulbels.game.Bulbels.*;


public class GameField extends MyScreen{

	ScrollPane scrollPane;
	AndroidHelper androidHelper;
	public static float bottomY, topY, coefficientFps;
	public AllBalls allBalls;
	public AllShapes allShapes;
	public static int turn;
	public int k=0,amountOfBalls, indexOfSelected;
	long lastTime;
	float aimX,aimY, limitAimY, launchX;
	Table boostTable,scrollTable;
	Skin labelSkin;
	public static boolean controlledAim, ghost;
	public boolean stop, finish=true, lose, boosts;
	boolean canLaunch, launching, isBottomTouch, repeat;
	TextureRegion aimTexture, gameBarTexture;
	Label labelBalls, labelTurn, labelMoney, labelRecord, labelScore;
	ButtonPanel pausePanel, lossPanel;
	OrthographicCamera camera;
	public TextureAtlas atlas;
	Vector3 touchPos;
	Sprite ball;
	Array<Panel> panels;
	Panel boost;
	final Image arrows, background,tableBackground;
	public static Action action;
	public MyActor topBar, bottomBar, coins, record, hand, arrow1, arrow2;

	public GameField(Bulbels game, final SettingsScreen settings) {
		super(game);
		androidHelper = game.androidHelper;
		atlas = game.atlas;
		limitAimY = Gdx.graphics.getHeight()*2 / 3.0f;
		bottomY = Gdx.graphics.getHeight()/8f;
		topY = Gdx.graphics.getHeight()*.93f;
		allBalls = new AllBalls();
		allShapes = new AllShapes(this);
		Label.LabelStyle style =new Label.LabelStyle(game.font,Color.YELLOW);
		panels = new Array<>();
		labelSkin = new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skins/label_skin.json"));

//		game.font.setColor(Color.YELLOW);


		touchPos = new Vector3();
		aimTexture= atlas.findRegion("ball");

		bottomBar = new MyActor(atlas.findRegion("square"),0,0,Gdx.graphics.getWidth(),bottomY);
		topBar = new MyActor(atlas.findRegion("square"),0, topY, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-topY);

		topBar.setZIndex(1);
		allShapes.addActor(topBar);

		record = new MyActor(atlas.findRegion("record"),0,0,topBar.getHeight()*.8f,topBar.getHeight()*.8f);
		record.setPosition(20* coefficientWidth,topBar.getY()+topBar.getHeight()/2,Align.center|Align.left);
		labelRecord = new Label("000",style);
		labelRecord.setPosition(record.getRight()+10*coefficientWidth,topBar.getY()+topBar.getHeight()/2,Align.center|Align.left);

		labelTurn = new Label(androidHelper.getString("turn")+": 000",style);
		labelBalls = new Label(androidHelper.getString("balls")+": 000",style);
		labelTurn.setPosition(labelRecord.getRight()+10*coefficientWidth,topBar.getY()+topBar.getHeight()/2,Align.center|Align.left);
		labelBalls.setPosition(labelTurn.getRight()+10*coefficientWidth,topBar.getY()+topBar.getHeight()/2,Align.center|Align.left);


		labelMoney = new Label("000",style);
//		labelMoney.setScale(topBar.getHeight()/112);
		labelMoney.pack();
		labelMoney.setPosition(stage.getWidth()-10*coefficientWidth,labelBalls.getY(),Align.bottomRight);
		coins = new MyActor(atlas.findRegion("coin"),0,0,topBar.getHeight()*.8f,topBar.getHeight()*.8f);
		coins.setPosition(labelMoney.getX()-5* coefficientWidth,topBar.getY()+topBar.getHeight()/2,Align.center|Align.right);


		hand = new MyActor(atlas.findRegion("hand"));
		float width = 50*coefficientWidth;
		hand.setSize(width,width*1.53f);
		hand.setPosition(Gdx.graphics.getWidth()/2f,bottomY*1.5f,Align.center);

		arrow1 = new MyActor(atlas.findRegion("arrow1"));
		width = Gdx.graphics.getWidth()*.8f;
		arrow1.setSize(width, width*.19f);
		arrow1.setPosition(Gdx.graphics.getWidth()/2f,bottomY*2f,Align.center);

		arrow2 = new MyActor(atlas.findRegion("arrow2"));
		arrow2.setSize(width, width*.105f);
		arrow2.setPosition(Gdx.graphics.getWidth()/2f,bottomY/2f,Align.center);

		width*=.6f;
		hand.addAction(sequence(moveBy(-width/2,0,1.5f), delay(.3f), Actions.forever(sequence(moveBy(width,0,3f),delay(.3f),moveBy(-width,0,3f),delay(.3f)))));

		String[] s = {game.androidHelper.getString("resume"), game.androidHelper.getString("menu"), game.androidHelper.getString("settings"),game.androidHelper.getString("restart")};
		Color[] colors ={Color.BLUE, Color.CYAN, Color.PURPLE,Color.MAROON};
		InputListener[] listeners = {
				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						back();
					}
				},
				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						pausePanel.setVisible(false);
						slideBack();
					}
				},
				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						settings.slideStart();
						GameField.this.game.add(settings);
					}
				},

				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						restart();
					}
				},
		};
		width = 550*coefficientWidth;
		pausePanel = new ButtonPanel(game,game.androidHelper.getString("pause"),width,s,colors,listeners);
		pausePanel.setPosition(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,Align.center);
		pausePanel.setVisible(false);

		s = new String[]{ game.androidHelper.getString("menu"), game.androidHelper.getString("restart")};
		colors = new Color[]{Color.BLUE,Color.MAROON};
		listeners = new InputListener[]{
				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						slideBack();
					}
				},
				new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						lossPanel.setVisible(false);
						restart();
					}
				}
		};


		lossPanel = new ButtonPanel(game,game.androidHelper.getString("game_over"),width,s,colors,listeners);
		lossPanel.setPosition(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,Align.center);
		Label score = new Label(game.androidHelper.getString("score"),style);//new Skin(Gdx.files.internal("skins/label_skin.json")),"yellow");
		score.setFontScale(coefficientWidth);
		score.pack();
		score.setPosition(lossPanel.getWidth()/2f, lossPanel.bottom-10*coefficientHeight,Align.center|Align.top);
		MyActor cup = new MyActor(atlas.findRegion("record"));
		cup.setSize(100*coefficientWidth,100*coefficientWidth);
		cup.setPosition(lossPanel.getWidth()/2,score.getY(),Align.topRight);
		labelScore = new Label("100",new Skin(Gdx.files.internal("skins/label_skin.json")),"white");
		labelScore.setColor(new Color(102/255f,0,236/255f,1));
		labelScore.setFontScale(coefficientWidth);
		labelScore.pack();
		labelScore.setPosition(cup.getRight()+10,cup.getY()+cup.getHeight()/2,Align.center|Align.left);
		lossPanel.addActor(score);
		lossPanel.addActor(cup);
		lossPanel.addActor(labelScore);

		AllBalls.startX = stage.getWidth()/2f;
		AllBalls.startY = bottomY+allBalls.getRadius();

		boostTable = new Table();



		//Тестовые:
		ball = new Sprite(atlas.findRegion("square"));
		ball.setX(250);
		ball.setY(600);
		ball.setColor(Color.GREEN);
		ball.setSize(AllShapes.width*1.2f,AllShapes.width*1.2f);


		scrollTable = new Table();

		width = Gdx.graphics.getWidth()*.2f;
		float height = width*1.25f;
//		String[] types = {"common_item","common_item","common_item","common_item","gold_item","gold_item","rainbow_item"};
		panels.add(new Panel(width, height, null ,"common_item"));
		scrollTable.row();
		for (int i = 0; i <ShopScreen.boostPanels.size; i++) {
			ShopScreen.BoostPanel panel = ShopScreen.boostPanels.get(i);
			panels.add(new Panel(width, height, panel.boost, panel.type));
			/*if ((i+1)%2==0)*/ scrollTable.row();
		}
		panels.first().amount = 1;
		panels.get(indexOfSelected).select();


		game.settings.loadField(this);
		launchX = AllBalls.startX;


		arrows = new Image(atlas.findRegion("arrows"));
		background = new Image(atlas.findRegion("color_pane"));
		arrows.setSize(40*coefficientWidth,40*coefficientWidth);
		arrows.setOrigin(Align.center);
		background.setSize(arrows.getWidth(),arrows.getHeight());
		background.setColor(game.backgroundColor);
		Group button = new Group();
		button.addActor(background);
		button.addActor(arrows);
		button.setSize(arrows.getWidth(),arrows.getHeight());
		arrows.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				moveBoosts();
			}
		});

//		scrollTable.setHeight(topY-bottomY);

		scrollPane = new ScrollPane(scrollTable,new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skins/scroll_pane_skin.json"),game.atlas));
		scrollPane.setScrollingDisabled(true,false);
		boostTable.align(Align.center|Align.left);
		boostTable.setBounds(Gdx.graphics.getWidth()-arrows.getWidth(),bottomY,Gdx.graphics.getWidth()/2f,topY-bottomY);
//		boostTable.add(arrows).width(arrows.getWidth()).height(arrows.getHeight());
		boostTable.add(button);
		boostTable.add(scrollPane);
		tableBackground = new Image(atlas.findRegion("square"));
		tableBackground.setBounds(arrows.getWidth(),0, boostTable.getWidth()-arrows.getWidth(),boostTable.getHeight());
		boostTable.addActorBefore(scrollPane,tableBackground);

		/*allShapes.addActor(shape);
		shape.addAction(Actions.fadeOut(2));
		shape.act(Gdx.graphics.getDeltaTime());
		shape.checkCollision(allBalls.getArrayBalls().first());
		allShapes.shapes[0][0] = shape;
		allShapes.shapes[0][1] = allShapes.shapes[0][0];
		allShapes.shapes[0][0]=null;*/

		//shape.remove();
		//shape = null;
		//stage.addAction(moveBy(-Gdx.graphics.getWidth(),0));

		table.addActor(allShapes);
		table.addActor(allBalls);
		table.addActor(bottomBar);
		table.addActor(labelTurn);
		table.addActor(labelBalls);
		table.addActor(labelMoney);
		table.addActor(coins);
		table.addActor(record);
		table.addActor(labelRecord);
		table.addActor(hand);
		table.addActor(arrow1);
		table.addActor(arrow2);
		table.addActor(pausePanel);
		table.addActor(lossPanel);


		table.setY(Gdx.graphics.getHeight());
		stage.addActor(table);
		stage.addActor(boostTable);
		updateLabels();
//		stage.setDebugAll(true);
	}

	@Override
	public void render (float delta) {
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

//		System.out.println("FPS "+(int)(1/delta));
		coefficientFps = delta/.016f;

		stage.act();
		stage.draw();
		if (!stop) {
			game.batch.begin();
			if (canLaunch) drawAim();
			game.batch.end();


			if (launching) launching();
			if (allBalls.inMotion()) allBalls.moveAll();

			else if (!finish) {
				//System.out.println("check");
				if (!allBalls.allIn(AllBalls.startX, AllBalls.startY))
					allBalls.moveWithAnimationTo(AllBalls.startX, AllBalls.startY);
				else nextTurn();
			}
			for (Ball b : allBalls.getArrayBalls()) allShapes.checkCollision(b);
		}
	}



	void topAim(float x, float y){
//		ball.moveTo(touchPos.x,touchPos.y);
//		ball.setY(touchPos.y);
//		System.out.println("x:"+touchPos.x+" y:"+touchPos.y);
		if ((finish||controlledAim) && !stop && y < limitAimY &&  Math.toDegrees(Math.atan((y - AllBalls.startY) / Math.abs((AllBalls.startX - x))))> 10) {
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
			final Ball b = allBalls.getArrayBalls().get(k);
			b.launch(aimX, aimY);
			if (action!=null){
				System.out.println("launching"+k);
				if (ghost) {
					b.ghost = true;
					b.addAction(sequence(action, run(new Runnable() {
						@Override
						public void run() {
							b.ghost = false;
						}
					})));
				}else b.addAction(action);

				if (repeat)boost.activate();
			}
			k++;
			if (k > amountOfBalls - 1) {
				launching = false;
				controlledAim = false;
				k = 0;
			}
		}
	}

	public void nextTurn(){
		allBalls.update();
		if (!allBalls.allIn(AllBalls.startX,AllBalls.startY))return;
		finish = true;
		boostTable.setVisible(true);
		next();
		endBoost();
		updateLabels();
		game.audioManager.next_turn.play(game.audioManager.getVolumeEffects());
	}
	public void next(){
		turn++;
		if (turn/2>amountOfBalls)allBalls.addBall();
		amountOfBalls = allBalls.getArrayBalls().size;
		allShapes.generateNewLine();
		launchX = AllBalls.startX;
	}

	void restart(){
		allShapes.initialize();
		allShapes.addActor(topBar);
		allBalls.initialize();
		AllBalls.startX = stage.getWidth()/2f;
		turn =0;
		gameResume();
		nextTurn();
		if (allBalls.getArrayBalls().size==0){
			allBalls.addBall();
			amountOfBalls++;
		}
		updateLabels();
		lose=false;
		labelRecord.setColor(Color.YELLOW);
	}

	void updateColors(){
		bottomBar.setColor(game.second_backgroundColor);
		topBar.setColor(bottomBar.getColor());
		background.setColor(game.backgroundColor);
		tableBackground.setColor(game.second_backgroundColor);
	}
	public void updateLabels(){
		labelTurn.setText(androidHelper.getString("turn")+": "+turn);
		labelBalls.setText(androidHelper.getString("balls")+": "+amountOfBalls);
		labelMoney.setText(game.money);
		if (turn>=game.record){
			labelRecord.setColor(Color.ORANGE);
			game.record =turn;
		}
		labelRecord.setText(game.record);
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
		//System.out.println(main2.set(Cx + right.x,Cy + right.y));
//		System.out.println(MathUtils.cosDeg(135));
	}

	void moveBoosts(){
		System.out.println("booosts");
		if (boosts)boostTable.addAction(Actions.moveBy(scrollTable.getWidth(),0,.5f));
		else boostTable.addAction(Actions.moveBy(-scrollTable.getWidth(),0,.5f));
		arrows.rotateBy(180);
		updatePanels();
		boosts=!boosts;
		stop=!stop;
	}

	void showHint(){
		hand.setVisible(true);
		arrow1.setVisible(true);
		arrow2.setVisible(true);
	}

	void hideHint(){
		hand.setVisible(false);
		arrow1.setVisible(false);
		arrow2.setVisible(false);
	}

	void gamePause(){
		stop=true;
		pausePanel.setVisible(true);
		arrows.setTouchable(Touchable.disabled);
	}

	void gameResume(){
		stop=false;
		pausePanel.setVisible(false);
		arrows.setTouchable(Touchable.enabled);
	}


	public void lose(){
		lose =true;
		stop =true;
		lossPanel.setVisible(true);
		labelScore.setText(turn);
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
		updateColors();
		stop =true;
		InputMultiplexer multiplexer = new InputMultiplexer(stage,GameField.this);
		Gdx.input.setInputProcessor(multiplexer);
		updateLabels();
		if (lose)restart();
		lossPanel.setVisible(false);
		if (skin.changed){
			skin.changed = false;
			allBalls.setSprites(skin.getSprite());
		}
		allShapes.checkLose();
		loadPanels();
	}

	@Override
	public void dispose () {
		allBalls.dispose();

	}

	@Override
	public void back() {
		if (boosts)moveBoosts();
		else {
			if (lose) {
				slideBack();
				return;
			}
			if (stop) gameResume();
			else gamePause();
		}
	}

	@Override
	public void slideStart() {
		Runnable start = new Runnable() {
			@Override
			public void run() {
				gameResume();
			}
		};
		table.addAction( sequence(moveBy(0, -Gdx.graphics.getHeight(), .6f, Interpolation.swingOut),run(start)));
	}

	@Override
	public void slideBack() {
		table.addAction( sequence(moveBy(0, Gdx.graphics.getHeight(), .6f, Interpolation.swingIn), run(back)));
	}





	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		hideHint();
		switch (pointer) {
			case 0:
				touchPos.set(screenX, screenY,0);
				camera.unproject(touchPos);

				isBottomTouch= touchPos.y < bottomY;
				touchDragged(screenX,screenY,pointer);
//				if (isBottomTouch)bottomAim(touchPos.x,touchPos.y);
//				else topAim(touchPos.x,touchPos.y);
//				touchDown(screenX, screenY, true);
				break;
			case 1:
				allBalls.addBall();
				amountOfBalls++;
				labelBalls.setText(androidHelper.getString("balls")+": "+amountOfBalls);
				break;
			case 2:
				turn+=5;
				labelTurn.setText(androidHelper.getString("turn")+": "+turn);
				break;
			case 3:
				allShapes.generateNewLine();
				break;
			case 4:
				allBalls.moveWithAnimationTo(Gdx.input.getX(pointer), Gdx.graphics.getHeight() - Gdx.input.getY(pointer));
				break;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!stop) {
			if (canLaunch && !controlledAim) {
				boostTable.setVisible(false);
				canLaunch = false;
				launching = true;
				finish = false;
				game.hint = false;
				useBoost();
			} else if (game.hint) {
				showHint();
			}
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touchPos.set(screenX,screenY,0);
		camera.unproject(touchPos);
		if (isBottomTouch)bottomAim(touchPos.x,touchPos.y);
		else topAim(touchPos.x,touchPos.y);
//		touchDown(screenX,screenY,false);
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {

//        System.out.println(keycode);
		switch (keycode){
			case 8:
//				boost= new SpeedBoost();
				break;
			case 9:
//				boost= new ControlledAimBoost();
				break;
			case 10:
//				boost= new DoubleDamageBoost();
				break;
			case 11:
//				boost= new SizeBoost();
				break;
			case 12:
//				boost= new GhostBoost();
				break;
			case 13:

				break;
			case 14:

				break;
			case 45:
				allBalls.addBall();
				amountOfBalls++;
				labelBalls.setText(androidHelper.getString("balls")+": "+amountOfBalls);
				break;
			case 51:
				turn+=30;
				labelTurn.setText(androidHelper.getString("turn")+": "+turn);
				break;
			case 33:
				allShapes.generateNewLine();
				break;
			case 46:
				allBalls.moveWithAnimationTo(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY());
				break;
			case 32:
				AllBalls.startX = allBalls.getArrayBalls().get(0).getCenterX()+30;
				allBalls.setX(AllBalls.startX);
				break;
			case 29:
				AllBalls.startX =allBalls.getArrayBalls().get(0).getCenterX()-30;
				allBalls.setX(AllBalls.startX);
				break;
			case Input.Keys.ESCAPE:
				back();
				break;
			case Input.Keys.T:
				allBalls.moveWithAnimationTo(AllBalls.startX,AllBalls.startY);
				break;
			case Input.Keys.TAB:
				stop=!stop;
				break;
			case Input.Keys.Z:
//				System.out.println(allBalls.allIn(AllBalls.startX,AllBalls.startY));
//				allBalls.print();
				System.out.println(allBalls.getArrayBalls().first().sprite.getColor());
				break;
			case Input.Keys.C:
				allBalls.update();
				//System.out.println(allBalls.allIn(AllBalls.startX,AllBalls.startY));
				break;
		}
		return true;
	}
	void loadPanels(){
		for (int i = 1; i < panels.size; i++) {
			Panel p = panels.get(i);
			p.amount = ShopScreen.boostPanels.get(i-1).amount;
			p.level = ShopScreen.boostPanels.get(i-1).level;
		}
	}

	void updatePanels(){
		for (Panel panel:panels)panel.update();
	}
	void useBoost(){
		Panel p = panels.get(indexOfSelected);
		if (p.boost != null) {
			Class aClass = p.boost.getClass();
			if (aClass==SizeBoost.class||aClass==GhostBoost.class||aClass==SmallBoost.class){
				repeat = true;
				boost = p;
			}
			p.use();
			p.deselect();
		}
	}
	void endBoost(){
		Panel p = panels.get(indexOfSelected);
		repeat = false;
		if (p.boost != null) {
			action = null;
			p.boost.end();
			panels.first().select();
		}
	}
	class Panel extends Group {
		int level, amount;
		float width, height;
		Boost boost;
		Image image;
		Label label;
		Image mark;
		public Image button;

		public Panel(float width, float height, Boost boost, String typeName) {
			super();
			this.boost = boost;
			this.width = width;
			this.height = height;
			float coeff = width / 300;
			setSize(width, height);
			button = new Image(game.atlas.findRegion(typeName));
			button.setSize(width, height);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					select();
					game.audioManager.click();
				}
			});
			addActor(button);

			label = new Label( "" ,labelSkin , "white");
			if (boost == null){
				image = new Image(atlas.findRegion("cancel"));
				label.setVisible(false);
			} else image = new Image(atlas.findRegion(boost.getRegionName()));
			image.setOrigin(Align.center);
			image.setPosition(width / 2, height * .7f, Align.center);
			image.setScale(coeff);
			image.setTouchable(Touchable.disabled);

			addActor(image);

			// System.out.println(label.getPrefWidth());
			label.setFontScale(.5f * coeff);
			updateLabel();
			//System.out.println(label.getPrefWidth());

			label.setTouchable(Touchable.disabled);
			mark = new Image(new Sprite(game.atlas.findRegion("checkmark")));
			deselect();
			mark.setSize(80 * coeff, 80 * coeff);
			mark.setPosition(width,0,Align.bottomRight);
			mark.setTouchable(Touchable.disabled);
			addActor(mark);

			addActor(label);

			scrollTable.add(this).pad(10*coefficientHeight);
//        System.out.println(group.getWidth()+" "+group.getHeight()+" "+button.getWidth()+" "+button.getHeight());

//        return group;
		}

		void updateLabel(){
			label.setText("x"+Panel.this.amount);
			label.pack();
			label.setPosition(width / 2, height * .2f, Align.center);
		}

		void dark(){
			label.setColor(Color.DARK_GRAY);
			mark.setColor(Color.DARK_GRAY);
			image.setColor(Color.DARK_GRAY);
			button.setColor(Color.DARK_GRAY);
			button.setTouchable(Touchable.disabled);
		}
		void light(){
			label.setColor(Color.WHITE);
			mark.setColor(Color.WHITE);
			image.setColor(Color.WHITE);
			button.setColor(Color.WHITE);
			button.setTouchable(Touchable.enabled);
		}

		void update(){
			if (amount<1)dark();
			else light();
			updateLabel();
		}

		void select(){
			panels.get(indexOfSelected).deselect();
			indexOfSelected = panels.indexOf(this,true);
			mark.setVisible(true);
			updateLabel();
		}
		void deselect(){
			mark.setVisible(false);
		}

		void use(){
			amount--;
			ShopScreen.BoostPanel panel = ShopScreen.boostPanels.get(indexOfSelected-1);
			panel.amount--;
			panel.updateLabel();
			activate();
		}
		void activate(){
			boost.activate(level);
		}

	}
}
