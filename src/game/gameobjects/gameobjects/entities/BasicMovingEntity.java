package game.gameobjects.gameobjects.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple entity, that implements movement and collision detection
 */
public abstract class BasicMovingEntity extends BasicDrawingEntity {
	private List<HitBox> hitBoxList;
	protected float vx, vy;			//Velocity in x and y direction
	protected float kx, ky;			//KnockBack in x and y direction


	public BasicMovingEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);
		this.hitBoxList = new ArrayList<>();
		hitBoxList.add(hitBox);

		vx = 0;
		vy = 0;
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	@Override
	public void update(Game game) {
		float vx_ = vx;
		vx = 0;
		vy += ky;
		move(game);
		vx += vx_;
		vy -= ky;

		float vy_ = vy;
		vy = 0;
		vx += kx;
		move(game);
		vy += vy_;
		vx -= kx;

		kx *= 0.95f;
		ky *= 0.95f;
	}

	private void move(Game game) {
		List<HitBoxDirection> directions = new ArrayList<>();
		List<Float> velocities = new ArrayList<>();
		HitBox targetLocation = hitBox.clone();
		targetLocation.move(vx, vy);

		hitBox.move(vx, vy);

		for (int i = 0; i < directions.size(); i++) {
			HitBoxDirection direction = directions.get(i);
			if (velocities.get(i) == 0) continue;

			if (direction == HitBoxDirection.LEFT || direction == HitBoxDirection.RIGHT) {
				vx = 0;
				ky *= 0.75f;
				kx = 0;
			} else if (direction == HitBoxDirection.UP || direction == HitBoxDirection.DOWN) {
				vy = 0;
				kx *= 0.75f;
				ky = 0;
			}
		}
	}

	private int lastAttackKnockBack = -MIN_TIME_BETWEEN_ATTACK_KNOCK_BACK;
	private static final int MIN_TIME_BETWEEN_ATTACK_KNOCK_BACK = 30;

	public void addKnockBack(float kx, float ky) {
		this.kx += kx;
		this.ky += ky;
	}


}
