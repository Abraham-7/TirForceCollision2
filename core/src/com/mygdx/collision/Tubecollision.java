package com.mygdx.collision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Tubecollision extends Game implements Screen, InputProcessor {
	World world;
	Body player;
	BodyDef bdef;
	FixtureDef fdef;
	TextureAtlas taBird;
	Box2DDebugRenderer b2dr;
	float elapsedTime;
	Animation aPlayer;
	Body floor;
	Sprite[] spBird = new Sprite[4];
	com.badlogic.gdx.audio.Music Music;
	com.badlogic.gdx.audio.Sound Sound;
	gravtest Gravtest;
	Texture txTtube, txBtube,txBird;
	SpriteBatch batch;
	Sprite sprTtube, sprBtube,sprBird;
	OrthographicCamera camera;
	Array<Sprite> arsprTtube, arsprBtube;
	long movetime, movetime2;
	int nspawnTime;
	float fTtubey;
	@Override
	public void create() {
		txTtube = new Texture(Gdx.files.internal("toptube.png"));
		txBtube = new Texture(Gdx.files.internal("bottomtube.png"));
		txBird= new Texture(Gdx.files.internal("Bird.png"));
		nspawnTime = 100;
		sprTtube = new Sprite(txTtube);
		sprBtube = new Sprite(txBtube);
		sprBird = new Sprite(txBird);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		arsprTtube = new Array<Sprite>();
		arsprBtube = new Array<Sprite>();
		Music = Gdx.audio.newMusic(Gdx.files.internal("ElginMusic.mp3")); // not the greatest naming
		Sound = Gdx.audio.newSound(Gdx.files.internal("Hitmarker.mp3"));
		Music.setLooping(true); // loops the mp3 file
		Music.play();
		Music.setVolume(1.0f); //controls how loud the music is
		spawnTtube();
		spawnBtube();
		Sound = Gdx.audio.newSound(Gdx.files.internal("Hitmarker.mp3")); // adding the audio sound
		b2dr = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		taBird = new TextureAtlas(Gdx.files.internal("Bird.txt")); // adding in the Bird.pack file
		//Sprite spBird= new Sprite("Bird.txt");
		for (int i = 0; i < 4; i++) {
			spBird[i] = new Sprite(taBird.findRegion("frame_" + i));
			//Sprite spBird= new Sprite("Bird.txt");
			Sprite sprBird = new Sprite(txBird);
		}
		world = new World(new Vector2(0, -150f), true); // making a new wold for gravity, and setting the velocity of the gravity
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
			}
			@Override
			public void endContact(Contact contact) {
			}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
		});
		createPlayer();
		createFloor();
		createRoof();
		//Collision();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		aPlayer = new Animation(1 / 8f, spBird);
	}
	private void createRoof(){
		bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		bdef.position.set(0, 480);
		bdef.type = BodyDef.BodyType.StaticBody;
		floor = world.createBody(bdef);
		shape.setAsBox(Gdx.graphics.getWidth(), 1);
		fdef = new FixtureDef();
		fdef.shape = shape;
		floor.setSleepingAllowed(false);
		floor.createFixture(fdef);
		floor.setGravityScale(0);
	} private void createPlayer() {
		bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		bdef.position.set(0, 90);
		bdef.type = BodyDef.BodyType.DynamicBody;
		player = world.createBody(bdef);
		shape.setAsBox(spBird[0].getWidth(), spBird[0].getHeight() / 2);
		fdef = new FixtureDef();
		fdef.shape = shape;
		player.setSleepingAllowed(false);
		player.createFixture(fdef);
		player.setGravityScale(1);
	}
	private void createFloor() { // creating a floor so megaman will not pass through the ground
		bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		bdef.position.set(0, 3);
		bdef.type = BodyDef.BodyType.StaticBody;
		floor = world.createBody(bdef);
		shape.setAsBox(Gdx.graphics.getWidth(), 1);
		fdef = new FixtureDef();
		fdef.shape = shape;
		floor.setSleepingAllowed(false);
		floor.createFixture(fdef);
		floor.setGravityScale(0);
	}
	private void spawnTtube() {
		Sprite sprTtube = new Sprite(txTtube);
		sprTtube.setX(750);
		sprTtube.setY(MathUtils.random(400 - 200) + 250);
		arsprTtube.add(sprTtube);
		fTtubey = sprTtube.getY() - 150;
		movetime = TimeUtils.nanoTime();
	}
	private void spawnBtube() {
		Sprite sprBtube = new Sprite(txBtube);
		sprBtube.setX(750);
		sprBtube.setY(fTtubey - 300);
		arsprBtube.add(sprBtube);
		movetime2 = TimeUtils.nanoTime();
	}
	//private void Collision() {
	//	if(spBird.getBoundingRectangle().overlaps(sprBTube.getBoundingRectangle())) {
	//	}
	//}
	@Override
	public void render() {
		elapsedTime += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1 / 60f, 6, 2);
		b2dr.render(world, camera.combined);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(aPlayer.getKeyFrame(elapsedTime, true), player.getPosition().x, player.getPosition().y - spBird[0].getHeight() / 2);
		if(Gdx.input.justTouched()){
			Sound.play();
		}
		if (sprBird.getBoundingRectangle().overlaps(sprBtube.getBoundingRectangle())) {
		System.out.println("Hello");
		}
		for (Sprite sprTtube : arsprTtube) {
			batch.draw(sprTtube, sprTtube.getX(), sprTtube.getY());
		}
		for (Sprite sprBtube : arsprBtube) {
			batch.draw(sprBtube, sprBtube.getX(), sprBtube.getY());
		}
		batch.end();
		if (TimeUtils.nanoTime() - movetime > 100000000 * nspawnTime) spawnTtube();
		Iterator<Sprite> iter = arsprTtube.iterator();
		while (iter.hasNext()) {
			Sprite sprTtube = iter.next();
			sprTtube.setX(sprTtube.getX() - (200) * Gdx.graphics.getDeltaTime());
		}
		if (TimeUtils.nanoTime() - movetime2 > 100000000 * nspawnTime) spawnBtube();
		Iterator<Sprite> iters = arsprBtube.iterator();
		while (iters.hasNext()) {
			Sprite sprBtube = iters.next();
			sprBtube.setX(sprBtube.getX() - (200) * Gdx.graphics.getDeltaTime());
		}if (sprBird.getBoundingRectangle().overlaps(sprBtube.getBoundingRectangle()))
			System.out.println("Hello");

	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		player.applyForceToCenter(0, 2000000, true);
		return true;

	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void hide() {

	}
}
//assistance from Don's code and teaching me how to do sprite arrays
