package game.gameobjects.gameobjects.cameracontroller;

import game.Game;
import game.gameobjects.AbstractGameObject;

/**
 * Move the camera to show the player
 */
public class CameraController extends AbstractGameObject {

	private int scroll;
	private int moveX, moveY;

	private float currentZoom;
	private float x, y;
	private boolean start;

	public CameraController() {
		scroll = 0;
		currentZoom = 0.15f;
		start = true;
		moveX = 0;
		moveY = 0;
		x = Game.PATH_WIDTH /2.0f;
		y = Game.PATH_HEIGHT/2.0f;
	}

	@Override
	public void update(Game game) {
		if (start) {
			game.getCamera().setPosition(x, y);
			game.getCamera().setZoom(currentZoom);
			start = false;
		} else {
			if (scroll != 0){
				currentZoom *= Math.pow(1.2, scroll);
				game.getCamera().setZoomSmooth(currentZoom, 300);
			}
			if (moveX != 0 || moveY != 0) {
				float z = game.getCamera().getZoom();

				x += 2*moveX / z / game.getWindow().getHeight();
				y += 2*moveY / z / game.getWindow().getHeight();

				game.getCamera().setPosition(x, y);

				moveX = 0;
				moveY = 0;
			}
		}

		scroll = 0;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public void setCameraMovement(int moveX, int moveY) {
		this.moveX = moveX;
		this.moveY = moveY;
	}

	@Override
	public float getPriority() {
		return -1;
	}
}
