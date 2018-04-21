package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum TowerType {

	ARCHER(2, 1, 1, 20, 5, 0, 15, 0, 100, "tower_archer");

	private int range, speed, damage;
	private int woodCosts, stoneCosts, metalCosts, goldCosts, energyCosts;
	private Sprite sprite;
	TowerType(int r, int s, int d, int wc, int sc, int mc, int gc, int ec, int as, String... images) {
		this.range = r;
		this.speed = s;
		this.damage = d;

		this.woodCosts = wc;
		this.stoneCosts = sc;
		this.metalCosts = mc;
		this.goldCosts = gc;
		this.energyCosts = ec;

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

	public int getEnergyCosts() {
		return energyCosts;
	}

	public int getGoldCosts() {
		return goldCosts;
	}

	public int getMetalCosts() {
		return metalCosts;
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
