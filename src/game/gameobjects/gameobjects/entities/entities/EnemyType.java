package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum EnemyType {

	BLUE_SLIME(1, 5, 5, 5, 100, "enemy_slime"), RED_SLIME(0.25f, 3, 15, 8, 100, "enemy_slime"), GREEN_SLIME(2.5f, 3, 5, 7, 100, "enemy_slime");

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
}
