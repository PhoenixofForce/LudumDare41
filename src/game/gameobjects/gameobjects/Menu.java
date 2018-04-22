package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.TowerType;
import game.util.TextureHandler;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import game.window.shader.shader.ColorShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AbstractGameObject implements Drawable {
	private MenuRow mainToolBar = createMainToolBar();
	private MenuRow buildingToolBar = createBuildingToolBar();

	private List<MenuRow> menuRows;

	public Menu() {
		menuRows = new ArrayList<>();

		menuRows.add(mainToolBar);
		menuRows.add(buildingToolBar);
	}

	private MenuRow createMainToolBar() {
		MenuRow menuRow = new MenuRow(null);
		menuRow.setX(0);

		MenuItem menuItem1 = new IconMenuItem("textures_enemy_slime_r_0") {
			@Override
			public void onClick() {

			}

			@Override
			public void onHover() {

			}
		};
		MenuItem menuItem2 = new IconMenuItem("textures_enemy_slime_r_1") {
			@Override
			public void onClick() {

			}

			@Override
			public void onHover() {

			}
		};
		MenuItem menuItem3 = new IconMenuItem("textures_enemy_slime_r_2") {
			@Override
			public void onClick() {

			}

			@Override
			public void onHover() {

			}
		};
		menuRow.addMenuItem(menuItem1);
		menuRow.addMenuItem(menuItem2);
		menuRow.addMenuItem(menuItem3);

		return menuRow;
	}

	private MenuRow createBuildingToolBar() {
		MenuRow menuRow = new MenuRow(mainToolBar);
		menuRow.setX(mainToolBar.x - 2*mainToolBar.getWidth()/6);
		for (TowerType type: TowerType.values()) {
			MenuItem item = new IconMenuItem(type.getSprite().getTexture(0, 0)) {
				@Override
				public void onClick() {

				}

				@Override
				public void onHover() {

				}
			};

			menuRow.addMenuItem(item);
		}

		return menuRow;
	}

	@Override
	public float getDrawingPriority() {
		return -1000;
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public void update(Game game) {
		for (MenuRow menuRow: menuRows) menuRow.update();
	}

	@Override
	public void draw(Window window, long time) {
		ColorShader colorShader = (ColorShader) window.getShaderHandler().getShader(ShaderType.COLOR_SHADER);
		BasicShader basicShader = (BasicShader) window.getShaderHandler().getShader(ShaderType.BASIC_SHADER);

		float y = 1;
		for (MenuRow menuRow: menuRows) {
			menuRow.draw(window, basicShader, colorShader, y);
			y -= menuRow.getHeight();
		}
	}

	@Override
	public void setup(Window window) {

	}

	@Override
	public void cleanUp(Window window) {

	}

	class MenuRow {
		private List<MenuItem> items;
		private MenuRow parent;
		private float x;
		public MenuRow(MenuRow parent) {
			items = new ArrayList<>();
			this.parent = parent;
			this.x = 0;
		}
		void setX(float x) {
			this.x = x;
		}

		void draw(Window window, BasicShader shader1, ColorShader shader2, float y) {
			shader2.draw((x - getWidth()/2) / window.getAspectRatio(), y - getHeight(), getWidth() / window.getAspectRatio(), getHeight(), 0x9e/255f, 0x44/255f, 0x91/255f, 0.75f);

			float x2 = (x - getWidth()/2);
			for (MenuItem item: items) {
				item.draw(window, shader1, shader2, x2, y);
				x2 += item.getWidth();
			}
		}

		float getHeight() {
			return 0.1f;
		}

		float getWidth() {
			float w = 0;
			for (MenuItem m: items) w += m.getWidth();
			return w;
		}

		void addMenuItem(MenuItem menuItem) {
			items.add(menuItem);
		}

		void onClick() {

		}

		void onHover() {

		}

		void update() {
			if (parent != null && !menuRows.contains(parent)) {
				menuRows.remove(this);
			}
		}
	}


	interface MenuItem {
		void draw(Window window, BasicShader shader1, ColorShader shader2, float x, float y);
		float getWidth();
		void onClick();
		void onHover();
	}

	abstract class IconMenuItem implements MenuItem {

		private Rectangle r;
		public IconMenuItem(String textureName) {
			this.r = TextureHandler.getSpriteSheetBounds(textureName);
		}

		public IconMenuItem(Rectangle r) {
			this.r = r;
		}

		@Override
		public void draw(Window window, BasicShader shader1, ColorShader shader2, float x, float y) {
			shader1.draw(x / window.getAspectRatio(), y-0.1f, 0.1f / window.getAspectRatio(), 0.1f, r.x, r.y, r.width, r.height, false);
		}

		@Override
		public float getWidth() {
			return 0.1f;
		}
	}
}
