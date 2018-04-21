package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicDrawingEntity;
import game.window.Window;

/**
 * A simple icon drawn on the screen
 */
public class ScreenEntity extends BasicDrawingEntity {
	private HitBox hitBox2;
	private float anchorX, anchorY;

	public ScreenEntity(HitBox hitBox, float drawingPriority, Sprite sprite, float anchorX, float anchorY) {
		super(hitBox, drawingPriority);

		this.hitBox2 = hitBox.clone();

		setSprite(sprite);
		setUseCamera(false);

		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	@Override
	public void draw(Window window, long time) {
		hitBox = hitBox2.clone();
		hitBox.setWidth(hitBox.getWidth() / window.getAspectRatio());
		hitBox.setX(hitBox.getX() - anchorX * hitBox.getWidth());
		hitBox.setY(hitBox.getY() - anchorY * hitBox.getHeight());

		super.draw(window, time);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}

	public float getWidth() {
		return hitBox.getWidth();
	}

	public float getHeight() {
		return hitBox.getHeight();
	}
}
