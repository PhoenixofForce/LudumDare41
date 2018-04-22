package game;

import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.wall.Background;

import java.util.*;

public class Path {

	private boolean[][] path;
	private int width, height;

	private List<float[]> pathFields = new ArrayList<>();

	public Path(Game game, int width, int height) {
		this.width = width;
		this.height = height;

		path = new boolean[width][height];
		Random r = new Random();
		int yDump = height/2;

		for(int x = 0; x < width; x++) {
			if (!path[x][yDump]) pathFields.add(0, new float[]{x, yDump});
			path[x][yDump] = true;

			int mode = r.nextInt(3);
			if(x > 2 && path[x-2][yDump]) {
				if(mode == 2 && yDump > 0) yDump--;
				else if(mode == 1 && yDump < height-1) yDump++;
				if (!path[x][yDump]) pathFields.add(0, new float[]{x, yDump});
				path[x][yDump] = true;
			}
		}
		float[] first = pathFields.get(0);
		pathFields.add(0, new float[] {first[0]+1, first[0]});
		float[] last = pathFields.get(pathFields.size()-1);
		pathFields.add(new float[] {last[0]-1, last[0]});

		Map<HitBox, String> background = new HashMap<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				String tile = "grass";
				if(path[x][y]) {
					if(x > 0 && path[x-1][y] && y > 0 && path[x][y-1]) tile = "path_tr";
					else if(x > 0 && path[x-1][y] && y < height-1 && path[x][y+1]) tile = "path_br";
					else if(x < width-1 && path[x+1][y] && y > 0 && path[x][y-1]) tile = "path_tl";
					else if(x < width-1 && path[x+1][y] && y < height-1 && path[x][y+1]) tile = "path_bl";
					else tile = Math.random() <= 0.5f? "path_t": "path_b";

					if(x == width-1) {
						if(y > 0 && path[x][y-1] && !path[x-1][y]) tile = "path_tl";
						else if(y < height-1 && path[x][y+1] && !path[x-1][y]) tile = "path_bl";
					}
				}

				background.put(new HitBox(x, y, 1, 1), tile);
			}
		}
		game.addGameObject(new Background(background));
	}

	public boolean isBlocked(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height && path[x][y];
	}

	public void setBlocked(int x, int y, boolean b) {
		path[x][y] = b;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPathLength() {
		return pathFields.size();
	}

	public float[] getPathPosition(float position) {
		if (position < 0 || position > getPathLength()-1) return null;
		if (position == (int) position) return pathFields.get((int) position);

		float[] pre = pathFields.get((int)position);
		float[] post = pathFields.get(1+(int)position);
		float inter = position - (int)position;

		return new float[] {post[0]*inter + pre[0]*(1-inter), post[1]*inter + pre[1]*(1-inter)};
	}
}
