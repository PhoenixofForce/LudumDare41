package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tower extends BasicStaticEntity {

	private TowerType type;
	private long lastAttack;

	Map<Enemy, List<Integer>> damageQueue;

	public Tower(TowerType t, float x, float y) {
		super(new HitBox(x, y, 1, 2), y);
		damageQueue = new HashMap<>();
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
		for(Enemy e: damageQueue.keySet()) {
			List<Integer> damageTimer = damageQueue.get(e);
			for(int i = 0; i < damageTimer.size(); i++) {
				damageTimer.set(i, damageTimer.get(i)-1);
				if(damageTimer.get(i) == 0) {
					e.damage(type.getDamage());
					damageTimer.remove(i);
				}
			}
		}

		Enemy focus = null;
		for(int i = 0; i < game.getEnemies().size(); i++) {
			Enemy e = game.getEnemies().get(i);
			if(inRange(e.getHitBox().x, e.getHitBox().y) && (focus == null || focus.getPosition() < e.getPosition())) {
				focus = e;
			}
		}
		if(focus != null) {
			if(TimeUtil.getTime()-lastAttack > type.getSpeed() * 1000L) {
				if(!damageQueue.containsKey(focus)) {
					damageQueue.put(focus, new ArrayList<>());
				}
				int damageIn = type.getParticleType().getLifeTime();
				damageQueue.get(focus).add(damageIn);

				float[] pos = focus.getPositionIn(damageIn);
				game.getParticleSystem().createParticle(type.getParticleType(), getHitBox().x, getHitBox().y, (pos[0] - getHitBox().x)/((float)damageIn), (pos[1] - getHitBox().y)/((float)damageIn));

				lastAttack = TimeUtil.getTime();
			}
		}
	}

	public HitBox getHitBox() {
		return hitBox;
	}
}
