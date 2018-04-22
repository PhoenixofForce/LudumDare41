package game.gameobjects.gameobjects.particle;

import game.data.Sprite;

public enum ParticleType {
	EXPLOSION(new Sprite(55, "particle_explosion_0", "particle_explosion_1", "particle_explosion_2", "particle_explosion_3", "particle_explosion_4", "particle_explosion_5", "particle_explosion_7", "particle_explosion_6", "particle_explosion_7"), 30, 2f, 2f, false),
	ARROW_TRAIL_BIG(new Sprite(100, "particle_arrow_trail_big"), 10, 0.5f, 0.5f, false),
	ARROW_TRAIL(new Sprite(100, "particle_arrow_trail"), 10, 1, 1, false),
	MAGIC_PROJECTILE(new Sprite(100, "particle_magic_proj"), 30, 0.25f, 0.25f, false),
	MAGIC_TRAIL_0_BIG(new Sprite(100, "particle_magic_trail_0_big"), 100, 1, 1, false),
	MAGIC_TRAIL_0(new Sprite(100, "particle_magic_trail_0"), 100, 1, 1, false),
	MAGIC_TRAIL_1_BIG(new Sprite(100, "particle_magic_trail_1_big"), 100, 1, 1, false),
	MAGIC_TRAIL_1(new Sprite(100, "particle_magic_trail_1"), 100, 1, 1, false),
	BOMB(new Sprite(100, "particle_bomb"), 35, 1, 1, false),
	THUNDER(new Sprite(100, "particle_thunder_0", "particle_thunder_1", "particle_thunder_2", "particle_thunder_3", "particle_thunder_4", "particle_thunder_5", "particle_thunder_6", "particle_thunder_7", "particle_thunder_8", "particle_thunder_9", "particle_thunder_10", "particle_thunder_11"), 72, 1, 1, false);

	private int lifeTime;
	private boolean gravity;
	private Sprite sprite;
	private float width, height;

	ParticleType(Sprite sprite, int lifeTime, float width, float height, boolean gravity) {
		this.lifeTime = lifeTime;
		this.gravity = gravity;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public boolean isGravity() {
		return gravity;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
}
