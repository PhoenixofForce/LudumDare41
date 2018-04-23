package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;

public enum BuildingType {

	MINE(150, 200, 180, 100, "building_mine"),
	LUMBER(200, 150, 150, 100, "building_lumber"),
	MILL(400, 200, 250, 100, "building_windmill_0", "building_windmill_1", "building_windmill_2", "building_windmill_3");

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
