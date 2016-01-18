package com.mygdx.collision;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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

/**
 * Created by Abraham on 2016-01-15.
 */
public class gravtest implements Screen, InputProcessor {
    com.badlogic.gdx.audio.Sound Sound;
    World world;
    Body player;
    BodyDef bdef;
    FixtureDef fdef;
    Texture txTtube,txBtube;
    Sprite[] spBird = new Sprite[4];
    Sprite sprTtube,sprBtube;
    Array<Sprite> arsprTtube,arsprBtube;
    long movetime,movetime2;
    int nspawnTime;
    float fTtubey;
    TextureAtlas taBird;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    float elapsedTime;
    Animation aPlayer;
    SpriteBatch batch;
    Body floor;
//Trying to get this working

    public gravtest(Tubecollision game) {
        Sound = Gdx.audio.newSound(Gdx.files.internal("Hitmarker.mp3")); // adding the audio sound
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        taBird = new TextureAtlas(Gdx.files.internal("Bird.txt")); // adding in the megaman.pack file


        for (int i = 0; i < 4; i++) {
            spBird[i] = new Sprite(taBird.findRegion("frame_" + i));
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
        //  createTubeBot();
        //   createTubeTop();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        aPlayer = new Animation(1 / 8f, spBird);
    }
    public void create (){
        txTtube = new Texture(Gdx.files.internal("toptube.png"));
        txBtube = new Texture(Gdx.files.internal("bottomtube.png"));
        nspawnTime = 100;
        sprTtube= new Sprite(txTtube);
        sprBtube= new Sprite(txBtube);
        //Creating sprite and camera
        camera= new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        batch= new SpriteBatch();
        arsprTtube= new Array<Sprite>();
        arsprBtube= new Array<Sprite>();
        spawnTtube();
        spawnBtube();
    }
    private void spawnTtube(){
        Sprite sprTtube = new Sprite(txTtube);
        sprTtube.setX(750);
        sprTtube.setY(MathUtils.random(400-200) + 250);
        arsprTtube.add(sprTtube);
        fTtubey= sprTtube.getY()-150;
        movetime= TimeUtils.nanoTime();

    }
    private void spawnBtube(){
        Sprite sprBtube = new Sprite(txBtube);
        sprBtube.setX(750);
        sprBtube.setY(fTtubey-300);
        arsprBtube.add(sprBtube);
        movetime2=TimeUtils.nanoTime();

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

    }


    private void createPlayer() {
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
    /* private void createTubeBot() {
         BotTube = new Texture("bottomtube.png");
         sprite = new Sprite(BotTube);
         bdef = new BodyDef();
         PolygonShape shape = new PolygonShape();

         bdef.position.set(450, 450);
         bdef.type = BodyDef.BodyType.StaticBody;
         player = world.createBody(bdef);

         shape.setAsBox(BotTube.getWidth(), BotTube.getHeight() / 2);
         fdef = new FixtureDef();
         fdef.shape = shape;
         player.setSleepingAllowed(false);
         player.createFixture(fdef);
         player.setGravityScale(0);
     }*/
   /* private void createTubeTop() {
        TopTube = new Texture("toptube.png");
        sprite = new Sprite(TopTube);

        bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(450, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        player = world.createBody(bdef);

        shape.setAsBox(TopTube.getWidth(), TopTube.getHeight() / 2);
        fdef = new FixtureDef();
        fdef.shape = shape;
        player.setSleepingAllowed(false);
        player.createFixture(fdef);
        player.setGravityScale(0);
    }*/
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
        player.applyForceToCenter(0, 200000, true);
        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        b2dr.render(world, camera.combined);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite sprTtube : arsprTtube) {
            batch.draw(sprTtube, sprTtube.getX(), sprTtube.getY());
        }
        for (Sprite sprBtube : arsprBtube){
            batch.draw(sprBtube,sprBtube.getX(),sprBtube.getY());
        }
        //System.out.println(player.getPosition(x,y);
        batch.draw(aPlayer.getKeyFrame(elapsedTime, true), player.getPosition().x, player.getPosition().y - spBird[0].getHeight() / 2);
        if(TimeUtils.nanoTime()-movetime>100000000*nspawnTime)spawnTtube();
        Iterator<Sprite> iter=arsprTtube.iterator();
        while(iter.hasNext()){
            Sprite sprTtube = iter.next();
            sprTtube.setX(sprTtube.getX()- (200)* Gdx.graphics.getDeltaTime());

        }if(TimeUtils.nanoTime()-movetime2>100000000*nspawnTime)spawnBtube();
        Iterator<Sprite> iters=arsprBtube.iterator();
        while(iters.hasNext()){
            Sprite sprBtube=iters.next();
            sprBtube.setX(sprBtube.getX()-(200)*Gdx.graphics.getDeltaTime());
        }
        if(Gdx.input.justTouched()) //used for dectecting if the screen is clicked
            Sound.play();
        batch.end();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
//http://www.gamefromscratch.com/post/2014/08/27/LibGDX-Tutorial-13-Physics-with-Box2D-Part-1-A-Basic-Physics-Simulations.aspx
