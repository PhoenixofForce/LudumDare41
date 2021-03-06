package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.Effects;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
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

	public boolean inRange(float[] pos) {
		if (pos == null) return false;
		return Math.sqrt(Math.pow(pos[0] +0.5f - getHitBox().getCenterX(), 2) + Math.pow(pos[1] +0.5f - getHitBox().getCenterY(), 2)) <= type.getRange();
	}

	@Override
	public void init(Game game) {
		super.init(game);
		this.setDrawingPriority(hitBox.y / game.getPath().getHeight());
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

					//SCREENSHAKE
					if(type.getParticleType() == ParticleType.BOMB) {
						game.getCamera().addScreenshake(0.01f);
					}

					//EXPLOSION
					if(type.getParticleType() == ParticleType.BOMB) {
						game.getParticleSystem().createParticle(ParticleType.EXPLOSION, e.getHitBox().getCenterX(), e.getHitBox().getCenterY(), 0, 0);
					}

					//AOE
					if(type.getDamageRange() > 0) {
						for(int j = 0; j < game.getEnemies().size(); j++) {
							Enemy e2 = game.getEnemies().get(j);
							if(e2.equals(2)) continue;
							double distance = Math.sqrt(Math.pow(e2.getHitBox().x - e.getHitBox().x, 2) + Math.pow(e2.getHitBox().y - e.getHitBox().y, 2));
							if(distance < type.getDamageRange()) {
								e2.damage(Math.round((float)((type.getDamage())/(Math.pow(distance+1, 2)))));
								if(type.getEffects() != Effects.NONE) {
									if(Math.random() < type.getEffects().getTrigger()/2.0f) e2.applyEffect(type.getEffects(), 3);
								}
							}
						}
					}

					e.damage(type.getDamage());
					if(type.getEffects() != Effects.NONE) {
						if(Math.random() < type.getEffects().getTrigger()) e.applyEffect(type.getEffects(), 3);
					}
					damageTimer.remove(i);
				}
			}
		}

		Enemy focus = null;
		for(int i = 0; i < game.getEnemies().size(); i++) {
			Enemy e = game.getEnemies().get(i);
			if(inRange(e.getPositionIn(type.getParticleType().getLifeTime())) && (focus == null || focus.getPosition() < e.getPosition() || (focus.hasEffect(type.getEffects()) && type.getEffects() != Effects.BURNING))) {
				focus = e;
			}
		}
		if(focus != null) {
			if(TimeUtil.getTime()-lastAttack > type.getSpeed() * 1000L) {
				if(!damageQueue.containsKey(focus)) {
					damageQueue.put(focus, new ArrayList<>());
				}
				int damageIn = type.getParticleType().getLifeTime();

				float[] pos = focus.getPositionIn(damageIn);
				if(type.getParticleType() != ParticleType.THUNDER) {
					damageQueue.get(focus).add(damageIn);
					game.getParticleSystem().createParticle(type.getParticleType(), getHitBox().getCenterX(), getHitBox().getCenterY(), (pos[0] +0.5f - getHitBox().getCenterX())/((float)damageIn), (pos[1] +0.5f - getHitBox().getCenterY())/((float)damageIn));
				} else {
					pos = focus.getPositionIn(42);
					game.getParticleSystem().createParticle(ParticleType.THUNDER, pos[0], pos[1]+0.9f, 0, 0);
					damageQueue.get(focus).add(42);
				}

				lastAttack = TimeUtil.getTime();
			}
		}
	}

	public HitBox getHitBox() {
		return hitBox;
	}
}
