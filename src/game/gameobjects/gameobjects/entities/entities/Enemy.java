package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.Wave;
import game.data.hitbox.HitBox;
import game.gameobjects.Effects;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.HealthBarShader;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends BasicMovingEntity {

	private EnemyType type;
	private float position;
	private int health, maxHealth;

	private Map<Effects, Integer> effectDurations;

	public Enemy(Wave wave, EnemyType t) {
		super(new HitBox(0, 0, 1, 1), 0);

		this.health = (int) t.getHealth() + (wave.getWave()/5);
		this.maxHealth = (int) t.getHealth() + (wave.getWave()/5);
		this.type = t;
		position = 0;

		effectDurations = new HashMap<>();

		this.setSprite(t.getSprite());
	}

	@Override
	public void draw(Window window, long time) {
		super.draw(window, time);

		if (health != type.getHealth()) {
			HealthBarShader shader = (HealthBarShader) window.getShaderHandler().getShader(ShaderType.HEALTH_BAR_SHADER);
			shader.start();
			shader.setUseCamera(true);
			shader.setBounds(hitBox.x, hitBox.y, hitBox.width, 0.2f);
			shader.setHealth(((float) health) / maxHealth);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		}
	}

	@Override
	public void update(Game game) {
		super.update(game);

		for (Object e2 : effectDurations.keySet().toArray()) {
			Effects e = (Effects) e2;
			if (game.getGameTick() % 60 == 0) effectDurations.put(e, effectDurations.get(e) - 1);
			if (effectDurations.get(e) == 0) effectDurations.remove(e);
			updateColor();
		}

		if (effectDurations.containsKey(Effects.BURNING))
			if (game.getGameTick() % 30 == 0) damage((int) Effects.BURNING.getEffectOn(type));

		position += (effectDurations.containsKey(Effects.PARALYSED) ? Effects.PARALYSED.getEffectOn(type) : 1) * type.getSpeed() / 60.0f;

		float[] newPos = game.getPath().getPathPosition(position);
		if (newPos == null || health <= 0) {
			game.removeGameObject(this);
			if (health <= 0) game.getMaterial(Material.GOLD).add(type.getDropedGold());
			else game.getCastle().damage((int) type.getDamage());
			return;
		}
		this.hitBox.x = newPos[0];
		this.hitBox.y = newPos[1];

		setDrawingPriority(hitBox.y / game.getPath().getHeight());
	}

	private void updateColor() {
		Effects e = Effects.NONE;
		for(Effects e2: effectDurations.keySet()) {
			e = e2;
			break;
		}

		float[] c = new float[4];
		if(e == Effects.NONE) {
			c[3] = 1;
			c[2] = 0;
			c[1] = 0;
			c[0] = 0;
		} else {
			Color c2 = e.getHealthColor();
			c[3] = 1f;
			c[2] = c2.getBlue()/511.0f;
			c[1] = c2.getGreen()/511.0f;
			c[0] = c2.getRed()/511.0f;
		}

		setColor(c);
	}


	public float[] getPositionIn(int ticks) {
		//TODO: Consider paralysis
		return game.getPath().getPathPosition(position + ticks * (type.getSpeed() / 60.0f));
	}

	@Override
	public boolean equals(Object b) {
		if (b instanceof Enemy) {
			Enemy e = (Enemy) b;
			return e.type == type && e.position == position && e.health == health;
		}
		return false;
	}

	@Override
	public float getPriority() {
		return 0;
	}

	public void damage(int damage) {
		this.health = Math.max(0, health - damage);
	}

	protected float getPosition() {
		return position;
	}

	public void applyEffect(Effects ef, int ticks) {
		effectDurations.put(ef, ticks);
		updateColor();
	}

	public boolean hasEffect(Effects ef) {
		return effectDurations.containsKey(ef);
	}
}