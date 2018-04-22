package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import game.window.shader.shader.HealthBarShader;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Enemy extends BasicMovingEntity{
	private float position;
	private EnemyType type;
	private int health;
	public Enemy(EnemyType t) {
		super(new HitBox(0, 0, 1,1), 0);

		this.health = (int) t.getHealth();
		this.type = t;
		position = 0;

		this.setSprite(t.getSprite());
	}

	@Override
	public void draw(Window window, long time) {
		super.draw(window, time);

		HealthBarShader shader = (HealthBarShader) window.getShaderHandler().getShader(ShaderType.HEALTH_BAR_SHADER);

		shader.start();
		shader.setUseCamera(true);
		shader.setBounds(hitBox.x, hitBox.y, hitBox.width, 0.2f);
		shader.setHealth(((float)health)/type.getHealth());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void update(Game game) {
		super.update(game);

		position += type.getSpeed()/60;

		float[] newPos = game.getPath().getPathPosition(position);
		if (newPos == null || health <= 0) {
			game.removeGameObject(this);
			if(health <= 0) game.getMaterial(Material.GOLD).add(type.getDropedGold());
			//else game.damage(type.getDamage());
			return;
		}
		this.hitBox.x = newPos[0];
		this.hitBox.y = newPos[1];

		setDrawingPriority(hitBox.y - game.getPath().getHeight());

	}

	@Override
	public float getPriority() {
		return 0;
	}

	public void damage(int damage) {
		this.health = Math.max(0, health-damage);
	}

	protected float getPosition() {
		return position;
	}
}
