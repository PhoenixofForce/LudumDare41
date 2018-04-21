package game.gameobjects.gameobjects.cameracontroller;

import game.Game;
import game.gameobjects.AbstractGameObject;

/**
 * Move the camera to show the player
 */
public class CameraController extends AbstractGameObject {

	public CameraController() {

	}

	@Override
	public void update(Game game) {
		game.getCamera().setPosition(0, 0);
		game.getCamera().setZoom(1);
	}

	@Override
	public float getPriority() {
		return -1;
	}
}
