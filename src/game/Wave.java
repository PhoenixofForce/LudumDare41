package game;

import game.gameobjects.Material;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.entities.Enemy;
import game.gameobjects.gameobjects.entities.entities.EnemyType;
import game.util.TimeUtil;

import java.awt.*;

public class Wave {

	private static final int FIRST_WAVE_TIME 		= 30;
	private static final int SECONDS_BETWEENROUNDS 	= 15;
	private static final int SECONDS_UNTIL_NEXT		= 360;

	private Game game;
	private int wave;
	private long waveEnded;

	private int state;
	private boolean skipToNext;

	private int currentGs = -1, currentRs = -1, currentBs = -1;
	private int lastTickSpawned_R, lastTickSpawned_G, lastTickSpawned_B;
	private int enemiesKilled = 0, lastSpaceTick = 0;

	private Text waveDisplay, secondLine;

	public Wave(Game game) {
		this.game = game;
		wave = 1;
		waveEnded = TimeUtil.getTime();

		waveDisplay = new Text(0.99f, 0.99f, -100, "Next Wave in " + SECONDS_BETWEENROUNDS, 0.04f, false, 1f, 1f, Color.WHITE);
		secondLine = new Text(0.99f, 0.99f - 0.0625f, -100, "Spacebar to jump", 0.04f, false, 1f, 1f, Color.WHITE);
		game.addGameObject(waveDisplay);
		game.addGameObject(secondLine);

		skipToNext = false;
		state = 0;
	}

	public void update() {
		if((skipToNext || (wave != 1? TimeUtil.getTime()-(1000L*SECONDS_BETWEENROUNDS): TimeUtil.getTime()-(1000L*FIRST_WAVE_TIME)) >= waveEnded) && state == 0) {
			currentBs = 0;
			currentGs = 0;
			currentRs = 0;

			enemiesKilled = 0;
			state = 1;

			if(skipToNext) game.getMaterial(Material.GOLD).add((int)((wave != 1? SECONDS_BETWEENROUNDS: FIRST_WAVE_TIME)-(TimeUtil.getTime()-waveEnded)/1000));
			skipToNext = false;
			//for(Material m: Material.values()) game.getMaterial(m).add(50);		//FOR TESTING
		} else {
			waveDisplay.setText("Wave " + wave + " in " + Math.max(0, ((wave != 1? SECONDS_BETWEENROUNDS: FIRST_WAVE_TIME)-(TimeUtil.getTime()-waveEnded)/1000)));
			secondLine.setText("Spacebar to jump");
		}

		if(state == 1 || state == 2) {
			if(state == 2) secondLine.setText("Spacebar to jump");
			else secondLine.setText("");
			waveDisplay.setText("Enemies left " + Math.abs((greenSlimes() + blueSlimes() + redSlimes() - enemiesKilled)));
		}

		//SPAWNING
		if(state == 1) {
			if(currentRs < redSlimes() && game.getGameTick()-65 >= lastTickSpawned_R) {
				game.addGameObject(new Enemy(this, EnemyType.RED_SLIME));
				currentRs++;
				lastTickSpawned_R = game.getGameTick();
			}

			if(currentBs < blueSlimes() && game.getGameTick()-60 >= lastTickSpawned_B && currentRs >= Math.ceil(redSlimes()/2.0f)-wave/2.0f) {
				game.addGameObject(new Enemy(this, EnemyType.BLUE_SLIME));
				currentBs++;
				lastTickSpawned_B = game.getGameTick();
			}

			if(currentGs < greenSlimes() && game.getGameTick()-55 >= lastTickSpawned_G && currentBs >= Math.ceil(blueSlimes()/2.0f)-wave/2.0f) {
				game.addGameObject(new Enemy(this, EnemyType.GREEN_SLIME));
				currentGs++;
				lastTickSpawned_G = game.getGameTick();
			}
		}
		if(state == 1 && blueSlimes() == currentBs && redSlimes() == currentRs && greenSlimes() == currentGs) state = 2;

		if((skipToNext || game.getEnemies().size() == 0) && state == 2) {
			state = 0;
			skipToNext = false;
			wave++;
			waveEnded = TimeUtil.getTime();
		}
	}

	public void nextWave() {
		if((state == 0 || state == 2) && game.getGameTick()-20 >= lastSpaceTick) {
			skipToNext = true;
			lastSpaceTick = game.getGameTick();
		}
	}

	public void enemyKilled() {
		enemiesKilled++;
	}

	private int blueSlimes() {
		return Math.max(
				Math.max(Math.round(1.0f * wave), 2)
				, 0);
	}

	private int redSlimes() {
		return Math.max(
				Math.round(0.3f * (wave - 4))
				, 0);
	}

	private int greenSlimes() {
		return Math.max(
				Math.round(0.5f * (wave - 9))
				, 0);
	}

	public int getWave() {
		return wave;
	}
}