package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Tower extends BasicStaticEntity {

	private TowerType type;
	public Tower(TowerType t, float x, float y) {
		super(new HitBox(x, y, 1, 2), -Game.PATH_HEIGHT+y);
		this.type = t;
		setSprite(type.getSprite());
	}

	public TowerType getType() {
		return type;
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}
}
