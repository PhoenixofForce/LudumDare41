package game.gameobjects.gameobjects.entities.entities;

import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;

public class Enemy extends BasicMovingEntity{

	private EnemyType type;
	public Enemy(EnemyType t, float x, float y) {
		super(new HitBox(x, y, 1,1), 1);
		this.type = t;
		this.setSprite(t.getSprite());
	}

	@Override
	public float getPriority() {
		return 0;
	}
}
