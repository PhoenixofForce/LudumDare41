package game.data.hitbox;

public class HitBox {
	private float x, y, width, height;
	private HitBox anchor;
	private float anchorX, anchorY;
	public HitBoxType type;

	public HitBox(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = HitBoxType.BLOCKING;
	}

	public HitBox(float x, float y, float width, float height, HitBoxType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public HitBox(HitBox anchor, float anchorX, float anchorY, float width, float height) {
		this.width = width;
		this.height = height;
		this.anchor = anchor;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.type = HitBoxType.BLOCKING;
	}

	public HitBox(HitBox anchor, float anchorX, float anchorY, float width, float height, HitBoxType type) {
		this.width = width;
		this.height = height;
		this.anchor = anchor;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.type = type;
	}

	public float getY() {
		if (anchor != null) return anchor.getY() + anchorY * anchor.getHeight();
		return y;
	}

	public float getX() {
		if (anchor != null) return anchor.getX() + anchorX * anchor.getWidth();
		return x;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setX(float x) {
		this.x = x;
		if (anchor != null) {
			y = getY();
			anchor = null;
		}
	}

	public void setY(float y) {
		this.y = y;
		if (anchor != null) {
			x = getX();
			anchor = null;
		}
	}

	public void setAnchor(HitBox anchor, float anchorX, float anchorY) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.anchor = anchor;
	}

	/**
	 * moves this hitbox
	 * @param mx the x movement
	 * @param my the y movement
	 */
	public void move(float mx, float my) {
		this.setX(this.getX() + mx);
		this.setY(this.getY() + my);
	}

	/**
	 * checks for collision with a second hitbox
	 * @param box2 the second hitbox
	 * @return if they collide
	 */
	public boolean collides(HitBox box2) {
		return ((this.getX() + this.getWidth()) > box2.x && (box2.x + box2.width) > this.getX() && (this.getY() + this.getHeight()) > box2.y && (box2.y + box2.height) > this.getY());
	}

	/**
	 * @param box2 the second box
	 * @return the direction of the given Box relative to this
	 * <p>
	 * LUR
	 * L#R
	 * LDR
	 * (# - This box)
	 */
	public HitBoxDirection direction(HitBox box2) {
		if (collides(box2)) return HitBoxDirection.COLLIDE;

		if ((this.getX() + this.getWidth()) <= box2.x) return HitBoxDirection.RIGHT;
		if ((box2.x + box2.width) <= this.getX()) return HitBoxDirection.LEFT;
		if ((this.getY() + this.getHeight()) <= box2.y) return HitBoxDirection.UP;
		return HitBoxDirection.DOWN;
	}

	/**
	 * @param box2 the movable box
	 * @param ax   the x part of the direction where the object should be moved
	 * @param ay   the y part of the direction where the object should be moved
	 * @return the amount of the given direction the second object has to be moved to avoid collision
	 */
	public float collisionDepth(HitBox box2, float ax, float ay) {
		if (!collides(box2)) return 0;

		float distance = Float.MAX_VALUE;

		if (ax != 0) {
			if (ax < 0) {
				distance = ((this.getX() + this.getWidth()) - box2.x) / (-ax);
			} else {
				distance = ((box2.x + box2.width) - this.getX()) / ax;
			}
		}
		if (ay != 0) {
			if (ay < 0) {
				distance = Math.min(((this.getY() + this.getHeight()) - box2.y) / (-ay), distance);
			} else {
				distance = Math.min(((box2.y + box2.height) - this.getY()) / ay, distance);
			}
		}

		return distance;
	}

	/**
	 * Calculates the distance of the center of a second hitbox to this center
	 * @param hitBox2 the second hitbox
	 * @return the distance
	 */
	public float distance(HitBox hitBox2) {
		return (float) Math.sqrt(Math.pow(hitBox2.getCenterX() - getCenterX(), 2) + Math.pow(hitBox2.getCenterY() - getCenterY(), 2));
	}

	/**
	 * @return the center of the x value of this hitbox
	 */
	public float getCenterX() {
		return this.getX() + this.getWidth() / 2;
	}

	/**
	 * @return the center of the y value of this hitbox
	 */
	public float getCenterY() {
		return this.getY() + this.getHeight() / 2;
	}

	/**
	 * @return a second hitbox with the same position and size
	 */
	@Override
	public HitBox clone() {
		return anchor != null ? new HitBox(this.anchor, this.anchorX, this.anchorY, this.getWidth(), this.getHeight(), this.type): new HitBox(this.getX(), this.getY(), this.getWidth(), this.getHeight(), type);
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f), (%f, %f)]", this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight());
	}

	public enum HitBoxType {
		BLOCKING, HALF_BLOCKING, NOT_BLOCKING

	}
}
