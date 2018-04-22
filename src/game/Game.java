package game;

import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.GameMaterial;
import game.gameobjects.GameObject;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.cameracontroller.CameraController;
import game.gameobjects.gameobjects.entities.BasicDrawingEntity;
import game.gameobjects.gameobjects.entities.entities.Enemy;
import game.gameobjects.gameobjects.entities.entities.EnemyType;
import game.gameobjects.gameobjects.entities.entities.Tower;
import game.gameobjects.gameobjects.entities.entities.TowerType;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Drawable;
import game.window.Keyboard;
import game.window.Window;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	private Window window;							//displays the game

	private int gameTick;							//current tick of the game (starts at 0) -> 60 Ticks Per Second

	private List<GameObject> gameObjects;			//list of gameObjects, that are updated every tick

	private Queue<GameObject> toRemove;				//list of gameObjects, that are removed next Tick
	private Queue<GameObject> toAdd;				//list of gameObjects, that are added next Tick

	private ParticleSystem particleSystem;			//display and store all particles
	private CameraController cameraController;

	private int mouseFieldX, mouseFieldY;
	private Map<Material, GameMaterial> materials;
	private List<Enemy> enemies;
	private TowerType selectedTower = TowerType.MAGE;
	private Path path;

	public Game(Window window) {
		this.window = window;
		Options.applyOptions(this);

		gameTick = 0;

		gameObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		path = new Path(this, 32, 18);
		this.addGameObject(new CameraController(this));

		mouseFieldX = 0;
		mouseFieldY = 0;

		enemies = new ArrayList<>();

		this.addGameObject(new BasicDrawingEntity(new HitBox(0, 0, 1, 1), 5) {
			{
				setSprite(new Sprite(100, "range_circle"));
			}

			private Tower mouseOverTower;

			@Override
			public float getPriority() {
				return 100;
			}

			@Override
			public void update(Game game) {
				mouseOverTower = path.getTower(mouseFieldX, mouseFieldY);
				if (mouseOverTower == null) {
					hitBox.width = 0;
					hitBox.height = 0;
				} else {
					hitBox.width = mouseOverTower.getType().getRange()*2;
					hitBox.height = mouseOverTower.getType().getRange()*2;
					hitBox.x = mouseOverTower.getHitBox().getCenterX() - mouseOverTower.getType().getRange();
					hitBox.y = mouseOverTower.getHitBox().getCenterY() - mouseOverTower.getType().getRange() - 0.5f;
				}
			}
		});

		this.addGameObject(new BasicDrawingEntity(new HitBox(0, 0, 1, 1), 4) {
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

		materials = new HashMap<>();
		for (int i = 0; i < Material.values().length; i++) {
			GameMaterial m = new GameMaterial(i);
			this.addGameObject(m);
			materials.put(Material.values()[i], m);
		}
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
				if(gameObject instanceof Enemy) enemies.remove(gameObject);
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
		mouseFieldX = (int) (getCamera().getX() + 2*(curr[0] - window.getWidth()/2) / getCamera().getZoom() / window.getHeight());
		mouseFieldY = (int) (getCamera().getY() + 2*(curr[1] - window.getHeight()/2) / getCamera().getZoom() / window.getHeight());

		if (keyboard.isPressed(Keyboard.MOUSE_BUTTON_MIDDLE)) cameraController.setCameraMovement(last[0] - curr[0], last[1] - curr[1]);
		if(keyboard.isPressed(Keyboard.MOUSE_BUTTON_1) && (lastMouseClickTick +1 != gameTick)) {
			if(selectedTower != null && mouseFieldX < path.getWidth() && mouseFieldX >= 0 && mouseFieldY >= 0 && mouseFieldY < path.getHeight() && !path.isBlocked(mouseFieldX, mouseFieldY) && selectedTower.getStoneCosts() <= materials.get(Material.STONE).getAmount() && selectedTower.getWoodCosts() <= materials.get(Material.WOOD).getAmount() && selectedTower.getGoldCosts() <= materials.get(Material.GOLD).getAmount()) {
				Tower tower = new Tower(selectedTower, mouseFieldX, mouseFieldY);
				path.addTower(mouseFieldX, mouseFieldY, tower);
				this.addGameObject(tower);

				materials.get(Material.GOLD).remove(selectedTower.getGoldCosts());
				materials.get(Material.WOOD).remove(selectedTower.getWoodCosts());
				materials.get(Material.STONE).remove(selectedTower.getStoneCosts());
			} else {
				createErrorText("You cannot place this here");
				getCamera().addScreenshake(0.02f);
			}
		}

		if (Math.random() < (1/60f) && keyboard.isPressed(Keyboard.MOUSE_BUTTON_2)) {
			this.addGameObject(new Enemy(EnemyType.values()[(int)Math.round(Math.random()*(EnemyType.values().length-1))]));
		}

		if (keyboard.isPressed(Keyboard.MOUSE_BUTTON_1))
			lastMouseClickTick = gameTick;
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
		if (!toAdd.contains(gameObject) && !gameObjects.contains(gameObject)) {
			toAdd.add(gameObject);
			if(gameObject instanceof Enemy) enemies.add((Enemy) gameObject);
		}
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

	public void createErrorText(String text) {
		Text error = new Text(-0.975f, -0.5f, -1000, text, 0.05f, false, 0f, 1f, Color.RED);
		error.setTimer(120);

		this.addGameObject(error);
	}

	public Path getPath() {
		return path;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}
}
