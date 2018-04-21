package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum TowerType {

	ARCHER(2, 1, 1, 100, "tower_archer");

	private int range, speed, damage;
	private Sprite sprite;

	TowerType(int r, int s, int d, int as, String... images) {
		this.range = r;
		this.speed = s;
		this.damage = d;
		this.sprite = new Sprite(as, images);
	}

	public int getDamage() {
		return damage;
	}

	public int getRange() {
		return range;
	}

	public int getSpeed() {
		return speed;
	}

	public Sprite getSprite() {
		return sprite;
	}
}
