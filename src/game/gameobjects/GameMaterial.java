package game.gameobjects;

import game.Game;
import game.gameobjects.gameobjects.Text;

import java.awt.*;

public class GameMaterial extends AbstractGameObject {
	private int amount;
	private int displayAmount;
	private Text displayText;
	private Material material;

	private float speed;
	private float remainder;

	public GameMaterial(int i) {
		this.amount = 300;
		this.displayAmount = 0;
		this.material = Material.values()[i];
		this.displayText = new Text(-0.99f, 0.95f - i*0.0625f, -100, "<"+material.toString().toLowerCase() +"> 0", 0.04f, false, 0f, 0f, Color.WHITE);

		speed = 1;
		remainder = 0;
	}

	@Override
	public void init(Game game) {
		super.init(game);
		game.addGameObject(displayText);
	}

	@Override
	public float getPriority() {
		return 100;
	}

	@Override
	public void update(Game game) {
		if (displayAmount > amount) {
			remainder -= speed;

			if (displayAmount + remainder <= amount) {
				displayAmount = amount;
				remainder = 0;
			} else {
				int rem = (int) remainder;
				displayAmount += rem;
				remainder -= rem;
			}

			displayText.setColor(Color.RED);
		} else if (displayAmount == amount) {
			displayText.setColor(Color.WHITE);
		} else {
			remainder += speed;

			if (displayAmount + remainder >= amount) {
				displayAmount = amount;
				remainder = 0;
			} else {
				int rem = (int) remainder;
				displayAmount += rem;
				remainder -= rem;
			}

			displayText.setColor(Color.GREEN);
		}

		displayText.setText("<"+material.toString().toLowerCase() +"> " + displayAmount);
	}

	public int getAmount() {
		return amount;
	}

	public void remove(int amount) {
		this.amount -= amount;

		this.speed = Math.max(Math.abs(this.displayAmount - this.amount) / (1f) / 60f, 4f/60);
	}

	public void add(int amount) {
		this.amount += amount;

		this.speed = Math.max(Math.abs(displayAmount - this.amount) / (1f) / 60f, 4f/60);
	}
}
