package game;

import game.window.Window;

public class Main {
	/**
	 * Start of the program
	 */

	public static Thread render, game;
	public static void main(String[] args) {
		if (game != null) game.stop();
		if (render != null) render.stop();

		game = Thread.currentThread();

		Options.load();
		Window w = new Window();
		Game g = new Game(w);

		render = new Thread(g::gameLoop);
		render.start();
		w.run();
	}
}
