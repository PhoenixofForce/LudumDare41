package game.gameobjects;

import game.Game;
import game.gameobjects.gameobjects.Text;

import java.awt.*;

public class GameMaterial extends AbstractGameObject {
	private int amount;
	private int displayAmount;
	private Text displayText;
	private Material material;

	public GameMaterial(int i) {
		this.amount = 100;
		this.displayAmount = 100;
		this.material = Material.values()[i];
		this.displayText = new Text(-0.99f, 0.95f - i*0.0625f, -100, "<"+material.toString().toLowerCase() +"> 0", 0.04f, false, 0f, 0f, Color.WHITE);
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
			if (game.getGameTick()%4 ==0) displayAmount--;
			displayText.setColor(Color.RED);
		} else if (displayAmount == amount) {
			displayText.setColor(Color.WHITE);
		} else {
			if (game.getGameTick()%4 ==0) displayAmount++;
			displayText.setColor(Color.GREEN);
		}

		displayText.setText("<"+material.toString().toLowerCase() +"> " + displayAmount);
	}

	public int getAmount() {
		return amount;
	}

	public void remove(int amount) {
		this.amount -= amount;
	}

	public void add(int amount) {
		this.amount += amount;
	}
}
