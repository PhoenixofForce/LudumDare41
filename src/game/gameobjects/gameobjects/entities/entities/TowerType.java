package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;
import game.gameobjects.gameobjects.particle.ParticleType;

public enum TowerType {

	ARCHER(5, 1, 1, 20, 5, 15, ParticleType.ARROW_TRAIL_BIG, 100, "tower_archer"),
	MAGE(3, 2, 3, 5, 20, 30,ParticleType.MAGIC_PROJECTILE, 100, "tower_mage_0", "tower_mage_1", "tower_mage_2"),
	VOLT(3, 2, 2, 10, 40, 50, ParticleType.THUNDER, 100, "tower_volto_0", "tower_volto_1", "tower_volto_2", "tower_volto_2", "tower_volto_3", "tower_volto_3", "tower_volto_4", "tower_volto_5", "tower_volto_6", "tower_volto_7", "tower_volto_8", "tower_volto_9", "tower_volto_10"),
	BOMB(2, 4, 10, 50, 100, 200, ParticleType.BOMB, 100, "tower_bomb_0", "tower_bomb_1", "tower_bomb_2", "tower_bomb_3");


	private int range, speed, damage;
	private int woodCosts, stoneCosts, goldCosts;
	private Sprite sprite;
	private ParticleType type;
	TowerType(int r, int s, int d, int wc, int sc, int gc, ParticleType type, int as, String... images) {
		this.range = r;
		this.speed = s;
		this.damage = d;

		this.woodCosts = wc;
		this.stoneCosts = sc;
		this.goldCosts = gc;

		this.type = type;
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

	public ParticleType getParticleType() {
		return type;
	}
}
