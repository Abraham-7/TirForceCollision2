package com.mygdx.collision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Tubecollision extends Game {
	TextureAtlas taMegaman;
	Sprite[] spMegaman;
	int j, nSpeed = 0;
	com.badlogic.gdx.audio.Music Music;
	com.badlogic.gdx.audio.Sound Sound;
	gravtest Gravtest;
	Texture txTtube, txBtube;
	SpriteBatch batch;
	Sprite sprTtube, sprBtube;
	OrthographicCamera camera;
	Array<Sprite> arsprTtube, arsprBtube;
	long movetime, movetime2;
	int nspawnTime;
	float fTtubey;


	@Override
	public void create() {
		//j = 0;

		//nSpeed = 0;
		txTtube = new Texture(Gdx.files.internal("toptube.png"));
		txBtube = new Texture(Gdx.files.internal("bottomtube.png"));
		nspawnTime = 100;
		sprTtube = new Sprite(txTtube);
		sprBtube = new Sprite(txBtube);
		//Creating sprite and camera
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
		setScreen(new gravtest(this));
		spawnTtube();
		spawnBtube();
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


	@Override
	public void render() {
		super.render();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
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
		}
	}
}
//assistance from Don's code and teaching me how to do sprite arrays
