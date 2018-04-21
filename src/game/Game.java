package game;

import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.cameracontroller.CameraController;
import game.gameobjects.gameobjects.entities.entities.ScreenEntity;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.gameobjects.gameobjects.wall.Background;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Drawable;
import game.window.Keyboard;
import game.window.Window;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	public static final int PATH_WIDTH = 16;
	public static final int PATH_HEIGHT = 9;

	private Window window;							//displays the game

	private int gameTick;							//current tick of the game (starts at 0) -> 60 Ticks Per Second

	private List<GameObject> gameObjects;			//list of gameObjects, that are updated every tick

	private Queue<GameObject> toRemove;				//list of gameObjects, that are removed next Tick
	private Queue<GameObject> toAdd;				//list of gameObjects, that are added next Tick

	private ParticleSystem particleSystem;			//display and store all particles

	private boolean[][] path;

	public Game(Window window) {
		this.window = window;
		Options.applyOptions(this);

		generatePath();
		gameTick = 0;

		gameObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		this.addGameObject(new CameraController());

		generatePath();
		Map<HitBox, String> background = new HashMap<>();
		for (int x = 0; x < PATH_WIDTH; x++) {
			for (int y = 0; y < PATH_HEIGHT; y++) {

				String tile = "grass";
				if(path[x][y]) {
					if(x > 0 && path[x-1][y] && y > 0 && path[x][y-1]) tile = "path_tr";
					else if(x > 0 && path[x-1][y] && y < PATH_HEIGHT-1 && path[x][y+1]) tile = "path_br";
					else if(x < PATH_WIDTH-1 && path[x+1][y] && y > 0 && path[x][y-1]) tile = "path_tl";
					else if(x < PATH_WIDTH-1 && path[x+1][y] && y < PATH_HEIGHT-1 && path[x][y+1]) tile = "path_bl";
					else tile = "path_t";

					if(x == PATH_WIDTH-1) {
						if(y > 0 && path[x][y-1] && !path[x-1][y]) tile = "path_tl";
						else if(y < PATH_HEIGHT-1 && path[x][y+1] && !path[x-1][y]) tile = "path_bl";
					}
				}

				background.put(new HitBox(x, y, 1, 1), tile);
			}
		}
		this.addGameObject(new Background(background));
		HitBox h1 = new HitBox(-1, 1, 0.1f, 0.1f);
		this.addGameObject(new ScreenEntity(h1, -5, new Sprite(100, "path_tl"), 0, 1));
	}

	private void generatePath() {

		path = new boolean[PATH_WIDTH][PATH_HEIGHT];
		Random r = new Random();
		int yDump = r.nextInt(PATH_HEIGHT);

		for(int x = 0; x < PATH_WIDTH; x++) {
			path[x][yDump] = true;

			int mode = r.nextInt(3);
			if(x > 2 && path[x-2][yDump]) {
				if(mode == 2 && yDump > 0) yDump--;
				else if(mode == 1 && yDump < PATH_HEIGHT-1) yDump++;
				path[x][yDump] = true;
			}
		}

		/*for(boolean[] a: path) {
			System.out.println(Arrays.toString(a));
		}*/
	}

	/**
	 * Update the game 60 times per second
	 **/
	public void gameLoop() {
		long time;

		while (window.isRunning()) {
			gameTick++;
			time = TimeUtil.getTime();
			handleInput();

			//Remove gameObjects
			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();

				gameObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = null;
			}

			//Add gameObjects
			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = (ParticleSystem) gameObject;
			}

			//Sort gameObjects for priority
			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(), o1.getPriority()));

			//Update every gameObject
			for (GameObject gameObject : gameObjects) {
				gameObject.update(this);
			}

			//Sync the updates to TPS
			long newTime = TimeUtil.getTime();
			TimeUtil.sleep((int) (1000.0f / Constants.TPS - (newTime - time)));
		}

		cleanUp();
	}

	/**
	 * update the Keyboard and Controller Inputs
	 **/
	private void handleInput() {
		Keyboard keyboard = window.getKeyboard();

		int a = keyboard.getScrollAmount();
		getCamera().addScreenshake(Math.abs(a) * 0.01f);
	}

	/**
	 * executed when the window closes -> Save Options
	 **/
	private void cleanUp() {
		Options.save();
	}

	/**
	 * add a new GameObject to the Game
	 *
	 * @param gameObject the gameObject to be added
	 **/
	public void addGameObject(GameObject gameObject) {
		if (!toAdd.contains(gameObject) && !gameObjects.contains(gameObject)) toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		if (!toRemove.contains(gameObject) && gameObjects.contains(gameObject)) {
			toRemove.add(gameObject);
		}
	}

	/**
	 * @return the camera used to display the game
	 **/
	public Camera getCamera() {
		return window.getCamera();
	}

	/**
	 * @return the width of the window divided by the height
	 **/
	public float getAspectRatio() {
		return window.getAspectRatio();
	}

	/**
	 * @return the particleSystem used to display and store particles
	 **/
	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	/**
	 * @return the current gameTick
	 **/
	public int getGameTick() {
		return gameTick;
	}
}
