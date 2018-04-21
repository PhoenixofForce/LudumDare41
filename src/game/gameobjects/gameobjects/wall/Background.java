package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.hitbox.HitBox;

import java.util.Map;

public class Background extends StaticDraw {
	public Background(Map<HitBox, String> hitBoxList) {
		super(100);

		super.updateContent(hitBoxList);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getPriority() {
		return 0;
	}
}
