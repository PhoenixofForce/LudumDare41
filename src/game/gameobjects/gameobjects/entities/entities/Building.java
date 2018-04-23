package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.gameobjects.gameobjects.particle.ParticleType;

public class Building extends BasicStaticEntity {

	private BuildingType type;

	public Building(BuildingType t, float x, float y) {
		super(new HitBox(x, y, t == BuildingType.MILL? 2: 1, 2), y);
		this.type = t;
		setSprite(type.getSprite());
	}

	@Override
	public void init(Game game) {
		super.init(game);
		this.setDrawingPriority(hitBox.y / game.getPath().getHeight());
	}

	public BuildingType getType() {
		return type;
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {
		if(game.getGameTick()%60 == 0) {
			Material m = null;
			if(type == BuildingType.MINE) m = Material.STONE;
			else if(type == BuildingType.LUMBER) m = Material.WOOD;

			if(m != null) {
				game.getMaterial(m).add(1);
				game.getParticleSystem().createParticle(ParticleType.PLUS_1, getHitBox().getCenterX(), getHitBox().getCenterY(), 0, 0.025f);
			}
		}
	}

	public HitBox getHitBox() {
		return hitBox;
	}
}
