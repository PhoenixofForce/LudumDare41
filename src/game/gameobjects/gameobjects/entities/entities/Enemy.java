package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;

public class Enemy extends BasicMovingEntity{
	private float position;
	private EnemyType type;
	public Enemy(EnemyType t) {
		super(new HitBox(0, 0, 1,1), 0);

		this.type = t;
		position = 0;

		this.setSprite(t.getSprite());
	}

	@Override
	public void update(Game game) {
		super.update(game);

		position += type.getSpeed()/60;

		float[] newPos = game.getPath().getPathPosition(position);
		if (newPos == null) {
			game.removeGameObject(this);
			return;
		}
		this.hitBox.x = newPos[0];
		this.hitBox.y = newPos[1];

		setDrawingPriority(hitBox.y - game.getPath().getHeight());

	}

	@Override
	public float getPriority() {
		return 0;
	}
}
