package game.gameobjects;

import game.gameobjects.gameobjects.entities.entities.EnemyType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum Effects {
	PARALYSED(0.6f, 0.95f, 0.5f, 0.7f, Color.BLUE),
	BURNING(0.4f, 3, 2, 1, Color.RED),
	NONE(0, 0, 0, 0, null);

	private Color healthColor;
	private float trigger;
	private Map<EnemyType, Float> damages;
	Effects(float t, float r, float g, float b, Color c) {
		damages = new HashMap<>();
		damages.put(EnemyType.GREEN_SLIME, g);
		damages.put(EnemyType.BLUE_SLIME, b);
		damages.put(EnemyType.RED_SLIME, r);

		this.trigger = t;

		this.healthColor = c;
	}

	public Color getHealthColor() {
		return healthColor;
	}

	public float getTrigger() {
		return trigger;
	}

	public float getEffectOn(EnemyType e) {
		return damages.get(e);
	}
}
