package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum EnemyType {

	BLUE_SLIME(1, 5, 5, 5, 100, "enemy_slime_b_0", "enemy_slime_b_1", "enemy_slime_b_2", "enemy_slime_b_1"),
	RED_SLIME(0.75f, 3, 15, 8, 100, "enemy_slime_r_0", "enemy_slime_r_1", "enemy_slime_r_2", "enemy_slime_r_1"),
	GREEN_SLIME(2.5f, 3, 5, 7, 100, "enemy_slime_g_0", "enemy_slime_g_1", "enemy_slime_g_2", "enemy_slime_g_1");

	private float speed, damage, health;
	private int dropedGold;
	private Sprite sprite;
	EnemyType(float speed, int damage, int health, int dg, int as, String... sprites) {
		this.speed = speed;
		this.damage = damage;
		this.health = health;
		this.dropedGold = dg;
		this.sprite = new Sprite(as, sprites);
	}

	public float getDamage() {
		return damage;
	}

	public float getHealth() {
		return health;
	}

	public float getSpeed() {
		return speed;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getDropedGold() {
		return dropedGold;
	}
}
