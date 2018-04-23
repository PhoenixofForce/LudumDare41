package game;

import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.entities.Enemy;
import game.gameobjects.gameobjects.entities.entities.EnemyType;
import game.util.TimeUtil;

import java.awt.*;

public class Wave {

	private static final int SECONDS_BETWEENROUNDS 	= 15;
	private static final int SECONDS_UNTIL_NEXT		= 90;

	private Game game;
	private int wave;
	private long waveEnded;

	private int state;
	private boolean skipToNext;

	private int currentGs = -1, currentRs = -1, currentBs = -1;
	private int lastTickSpawned;

	private Text waveDisplay, secondLine;

	public Wave(Game game) {
		this.game = game;
		wave = 1;
		waveEnded = TimeUtil.getTime();

		waveDisplay = new Text(0.95f, 0.95f, -100, "Next Wave in " + SECONDS_BETWEENROUNDS, 0.04f, false, 1f, 1f, Color.WHITE);
		secondLine = new Text(0.95f, 0.9f, -100, "Spacebar to jump", 0.04f, false, 1f, 1f, Color.WHITE);
		game.addGameObject(waveDisplay);
		game.addGameObject(secondLine);

		skipToNext = false;
		state = 0;
	}

	public void update() {
		if((skipToNext || TimeUtil.getTime()-(1000L*SECONDS_BETWEENROUNDS) >= waveEnded) && state == 0) {
			currentBs = 0;
			currentGs = 0;
			currentRs = 0;

			state = 1;
		} else {
			waveDisplay.setText("Wave " + wave + " in " + Math.max(0, (SECONDS_BETWEENROUNDS-(TimeUtil.getTime()-waveEnded)/1000)));
			secondLine.setText("Spacebar to jump");
		}

		if(state == 1 || state == 2) {
			waveDisplay.setText("Wave " + (wave+1) + " in " + Math.max((SECONDS_UNTIL_NEXT-(TimeUtil.getTime()-waveEnded)/1000), 0));
			secondLine.setText("Enemies left " + game.getEnemies().size());
		}

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
		if(state == 1 && blueSlimes() <= currentBs && redSlimes() <= currentRs && greenSlimes() <= currentGs) state = 2;

		if((waveEnded <= TimeUtil.getTime() - (1000L*SECONDS_UNTIL_NEXT) || game.getEnemies().size() == 0) && state == 2) {
			state = 0;
			skipToNext = false;
			wave++;
			waveEnded = TimeUtil.getTime();
		}
	}

	public void nextWave() {
		if(state == 0) skipToNext = true;
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