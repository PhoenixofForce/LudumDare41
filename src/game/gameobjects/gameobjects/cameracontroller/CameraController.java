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
		game.getCamera().setPosition(Game.PATH_WIDTH/2.0f, Game.PATH_HEIGHT/2.0f);
		game.getCamera().setZoom(0.1f);
	}

	@Override
	public float getPriority() {
		return -1;
	}
}
