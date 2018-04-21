package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum TowerType {

	ARCHER(2, 1, 1, 20, 5, 15, 100, "tower_archer");

	private int range, speed, damage;
	private int woodCosts, stoneCosts, goldCosts;
	private Sprite sprite;
	TowerType(int r, int s, int d, int wc, int sc, int gc, int as, String... images) {
		this.range = r;
		this.speed = s;
		this.damage = d;

		this.woodCosts = wc;
		this.stoneCosts = sc;
		this.goldCosts = gc;

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

	public int getGoldCosts() {
		return goldCosts;
	}

	public int getStoneCosts() {
		return stoneCosts;
	}

	public int getWoodCosts() {
		return woodCosts;
	}

	public Sprite getSprite() {
		return sprite;
	}
}
