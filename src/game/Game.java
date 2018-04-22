package game;

import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.GameMaterial;
import game.gameobjects.GameObject;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.cameracontroller.CameraController;
import game.gameobjects.gameobjects.entities.BasicDrawingEntity;
import game.gameobjects.gameobjects.entities.entities.Tower;
import game.gameobjects.gameobjects.entities.entities.TowerType;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.gameobjects.gameobjects.wall.Background;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Drawable;
import game.window.Keyboard;
import game.window.Window;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	public static final int PATH_WIDTH = 32;
	public static final int PATH_HEIGHT = 18;

	private Window window;							//displays the game

	private int gameTick;							//current tick of the game (starts at 0) -> 60 Ticks Per Second

	private List<GameObject> gameObjects;			//list of gameObjects, that are updated every tick

	private Queue<GameObject> toRemove;				//list of gameObjects, that are removed next Tick
	private Queue<GameObject> toAdd;				//list of gameObjects, that are added next Tick

	private ParticleSystem particleSystem;			//display and store all particles
	private CameraController cameraController;

	private int mouseFieldX, mouseFieldY;
	private Map<Material, GameMaterial> materials;

	private boolean[][] path;

	public Game(Window window) {
		this.window = window;
		Options.applyOptions(this);

		gameTick = 0;

		gameObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		this.addGameObject(new CameraController());

		mouseFieldX = 0;
		mouseFieldY = 0;

		this.addGameObject(new BasicDrawingEntity(new HitBox(0, 0, 1, 1), -2) {
			{
				setSprite(new Sprite(100, "selection"));
			}
			@Override
			public float getPriority() {
				return 100;
			}

			@Override
			public void update(Game game) {
				hitBox.x = mouseFieldX;
				hitBox.y = mouseFieldY;
			}
		});

		generatePath();

		materials = new HashMap<>();
		for (int i = 0; i < Material.values().length; i++) {
			GameMaterial m = new GameMaterial(i);
			this.addGameObject(m);
			materials.put(Material.values()[i], m);
		}
	}

	private void generatePath() {
		path = new boolean[PATH_WIDTH][PATH_HEIGHT];
		Random r = new Random();
		int yDump = PATH_HEIGHT/2;

		for(int x = 0; x < PATH_WIDTH; x++) {
			path[x][yDump] = true;

			int mode = r.nextInt(3);
			if(x > 2 && path[x-2][yDump]) {
				if(mode == 2 && yDump > 0) yDump--;
				else if(mode == 1 && yDump < PATH_HEIGHT-1) yDump++;
				path[x][yDump] = true;
			}
		}
		Map<HitBox, String> background = new HashMap<>();
		for (int x = 0; x < PATH_WIDTH; x++) {
			for (int y = 0; y < PATH_HEIGHT; y++) {

				String tile = "grass";
				if(path[x][y]) {
					if(x > 0 && path[x-1][y] && y > 0 && path[x][y-1]) tile = "path_tr";
					else if(x > 0 && path[x-1][y] && y < PATH_HEIGHT-1 && path[x][y+1]) tile = "path_br";
					else if(x < PATH_WIDTH-1 && path[x+1][y] && y > 0 && path[x][y-1]) tile = "path_tl";
					else if(x < PATH_WIDTH-1 && path[x+1][y] && y < PATH_HEIGHT-1 && path[x][y+1]) tile = "path_bl";
					else tile = Math.random() <= 0.5f? "path_t": "path_b";

					if(x == PATH_WIDTH-1) {
						if(y > 0 && path[x][y-1] && !path[x-1][y]) tile = "path_tl";
						else if(y < PATH_HEIGHT-1 && path[x][y+1] && !path[x-1][y]) tile = "path_bl";
					}
				}

				background.put(new HitBox(x, y, 1, 1), tile);
			}
		}
		this.addGameObject(new Background(background));

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

			//Remove gameObjects
			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();

				gameObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = null;
				if (gameObject instanceof CameraController) cameraController = null;
			}

			//Add gameObjects
			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = (ParticleSystem) gameObject;
				if (gameObject instanceof CameraController) cameraController = (CameraController) gameObject;
			}

			handleInput();

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
	private int lastMouseClickTick = 0;
	private void handleInput() {
		Keyboard keyboard = window.getKeyboard();

		int a = keyboard.getScrollAmount();
		cameraController.setScroll(a);

		int[] curr = {keyboard.getMouseX(), window.getHeight()-keyboard.getMouseY()};
		int[] last = {keyboard.getLastMouseX(), window.getHeight()-keyboard.getLastMouseY()};

		if (keyboard.isPressed(Keyboard.MOUSE_BUTTON_MIDDLE)) cameraController.setCameraMovement(last[0] - curr[0], last[1] - curr[1]);
		if(keyboard.isPressed(Keyboard.MOUSE_BUTTON_1) && (lastMouseClickTick +1 != gameTick)) {
			int[] currC = {keyboard.getMouseX(), window.getHeight()-keyboard.getMouseY()};
			int clickFieldX = (int) (getCamera().getX() + 2*(currC[0] - window.getWidth()/2) / getCamera().getZoom() / window.getHeight());
			int clickFieldY = (int) (getCamera().getY() + 2*(currC[1] - window.getHeight()/2) / getCamera().getZoom() / window.getHeight());

			TowerType t = TowerType.VOLT;
			if(clickFieldX < PATH_WIDTH && clickFieldX >= 0 && clickFieldY >= 0 && clickFieldY < PATH_HEIGHT && !path[clickFieldX][clickFieldY] && t.getStoneCosts() <= materials.get(Material.STONE).getAmount() && t.getWoodCosts() <= materials.get(Material.WOOD).getAmount() && t.getGoldCosts() <= materials.get(Material.GOLD).getAmount()) {
				path[clickFieldX][clickFieldY] = true;
				this.addGameObject(new Tower(t, clickFieldX, clickFieldY));
				materials.get(Material.GOLD).remove(t.getGoldCosts());
				materials.get(Material.WOOD).remove(t.getWoodCosts());
				materials.get(Material.STONE).remove(t.getStoneCosts());
			} else getCamera().addScreenshake(0.02f);
		}

		if (keyboard.isPressed(Keyboard.MOUSE_BUTTON_1))
			lastMouseClickTick = gameTick;


		mouseFieldX = (int) (getCamera().getX() + 2*(curr[0] - window.getWidth()/2) / getCamera().getZoom() / window.getHeight());
		mouseFieldY = (int) (getCamera().getY() + 2*(curr[1] - window.getHeight()/2) / getCamera().getZoom() / window.getHeight());
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

	public Window getWindow() {
		return window;
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
