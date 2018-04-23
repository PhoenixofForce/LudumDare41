package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.HealthBarShader;
import org.lwjgl.opengl.GL11;

public class Castle extends BasicStaticEntity {

	private int health;

	public Castle(float x, float y) {
		super(new HitBox(x, y, 1, 2), y);
		this.health = 100;
		this.setSprite(new Sprite(100, "building_castle"));
	}

	@Override
	public void init(Game game) {
		super.init(game);
		this.setDrawingPriority(hitBox.y / game.getPath().getHeight());
	}

	@Override
	public void draw(Window window, long time) {
		super.draw(window, time);

		HealthBarShader shader = (HealthBarShader) window.getShaderHandler().getShader(ShaderType.HEALTH_BAR_SHADER);
		shader.start();
		shader.setUseCamera(true);
		shader.setBounds(hitBox.x, hitBox.y-0.2f, hitBox.width, 0.2f);
		shader.setHealth(((float) health) / 100);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}

	public void damage(int d) {
		this.health = Math.max(0, health-d);
	}

	public int getHealth() {
		return health;
	}
}
