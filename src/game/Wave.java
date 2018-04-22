package game;

import game.gameobjects.gameobjects.entities.entities.Enemy;
import game.gameobjects.gameobjects.entities.entities.EnemyType;
import game.util.TimeUtil;

public class Wave {

	private Game game;
	private int wave;
	private long waveEnded;

	private int state;

	private int currentGs = -1, currentRs = -1, currentBs = -1;
	private int lastTickSpawned;
	public Wave(Game game) {
		this.game = game;
		wave = 1;
		waveEnded = TimeUtil.getTime();

		state = 0;
	}

	public void update() {
		if(TimeUtil.getTime()-10000L >= waveEnded && state == 0) {
			currentBs = 0;
			currentGs = 0;
			currentRs = 0;

			state = 1;
		}

		System.out.println(state);

		if(state == 1 && blueSlimes() <= currentBs && redSlimes() <= currentRs && greenSlimes() <= currentGs) state = 2;
		if(state == 1) {
			if(currentRs <= redSlimes() && game.getGameTick()-60 >= lastTickSpawned) {
				game.addGameObject(new Enemy(EnemyType.RED_SLIME));
				currentRs++;
				lastTickSpawned = game.getGameTick();
			}else if(currentBs <= blueSlimes() && game.getGameTick()-60 >= lastTickSpawned) {
				game.addGameObject(new Enemy(EnemyType.BLUE_SLIME));
				currentBs++;
				lastTickSpawned = game.getGameTick();
			} else if(currentGs <= greenSlimes() && game.getGameTick()-60 >= lastTickSpawned) {
				game.addGameObject(new Enemy(EnemyType.GREEN_SLIME));
				currentGs++;
				lastTickSpawned = game.getGameTick();
			}
		}

		if((waveEnded <= TimeUtil.getTime() - 90000L && game.getEnemies().size() == 0) && state == 2) {
			state = 0;
			wave++;
			waveEnded = TimeUtil.getTime();
		}
	}

	private int blueSlimes() {
		return Math.round(1.5f * wave);
	}

	private int redSlimes() {
		return Math.round(1.5f * wave);
	}

	private int greenSlimes() {
		return Math.round(1.5f * wave);
	}
}