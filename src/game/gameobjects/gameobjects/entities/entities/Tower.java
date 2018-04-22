package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.util.TimeUtil;

public class Tower extends BasicStaticEntity {

	private TowerType type;
	private long lastAttack;
	public Tower(TowerType t, float x, float y) {
		super(new HitBox(x, y, 1, 2), y);
		this.type = t;
		lastAttack = 0L;
		setSprite(type.getSprite());
	}

	public boolean inRange(float x, float y) {
		return Math.sqrt(Math.pow(x - getHitBox().x, 2) + Math.pow(y - getHitBox().y, 2)) <= type.getRange();
	}

	@Override
	public void init(Game game) {
		super.init(game);
		this.setDrawingPriority(hitBox.y - game.getPath().getHeight());
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
		Enemy focus = null;
		for(int i = 0; i < game.getEnemies().size(); i++) {
			Enemy e = game.getEnemies().get(i);
			if(inRange(e.getHitBox().x, e.getHitBox().y) && (focus == null || focus.getPosition() < e.getPosition())) {
				focus = e;
			}
		}
		if(focus != null) {
			if(TimeUtil.getTime()-lastAttack > type.getSpeed() * 1000L) {
				focus.damage(type.getDamage());
				lastAttack = TimeUtil.getTime();
			}
		}
	}

	public HitBox getHitBox() {
		return hitBox;
	}
}
