package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum TowerType {

	ARCHER(2, 1, 1, 20, 5, 15, 100, "tower_archer"),
	MAGE(1, 2, 3, 5, 20, 30, 100, "tower_mage_0", "tower_mage_1", "tower_mage_2"),
	VOLT(3, 2, 2, 10, 40, 50, 100, "tower_volto_0", "tower_volto_1", "tower_volto_2", "tower_volto_2", "tower_volto_3", "tower_volto_3", "tower_volto_4", "tower_volto_5", "tower_volto_6", "tower_volto_7", "tower_volto_8", "tower_volto_9", "tower_volto_10");

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
