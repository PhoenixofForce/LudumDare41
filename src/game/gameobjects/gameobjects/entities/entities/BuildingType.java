package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum BuildingType {

	MINE(150, 200, 180, 100, "buiding_mine"),
	LUMBER(200, 150, 150, 100, "building_castle"),		//TODO: Add sprites/ ani
	MILL(400, 200, 250, 100, "building_castle");

	private Sprite sprite;
	private int woodCosts, stoneCosts, goldCosts;
	BuildingType(int wc, int sc, int gc, int as, String... textures) {
		this.woodCosts = wc;
		this.stoneCosts = sc;
		this.goldCosts = gc;

		this.sprite = new Sprite(as, textures);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getWoodCosts() {
		return woodCosts;
	}

	public int getStoneCosts() {
		return stoneCosts;
	}

	public int getGoldCosts() {
		return goldCosts;
	}
}
