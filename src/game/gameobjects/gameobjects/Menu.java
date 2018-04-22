package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.TowerType;
import game.util.TextureHandler;
import game.util.TimeUtil;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import game.window.shader.shader.ColorShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu extends AbstractGameObject implements Drawable {
	public static final float[] NO_HIGHTLIGHT_COLOR = new float[]{0, 0, 0, 1};
	public static final float[] HIGHTLIGHT_COLOR = new float[]{0.125f, 0.125f, 0.125f, 1};
	private MenuRow mainToolBar = new MenuRow(null, 0.1f);
	private MenuRow buildingToolBar = new MenuRow(mainToolBar, 0.2f);

	private List<MenuRow> menuRows;

	private float mousePositionX, mousePositionY;
	private boolean mouseClicked;

	private MenuItem hightlighted;

	public Menu() {
		menuRows = new ArrayList<>();

		createMainToolBar();
		createBuildingToolBar();

		menuRows.add(mainToolBar);
		menuRows.add(buildingToolBar);
	}

	private void createMainToolBar() {
		mainToolBar.setX(0);

		MenuItem menuItem1 = new IconMenuItem("textures_symbol_build", mainToolBar, buildingToolBar) {
			@Override
			public void onClick() {

			}
		};
		MenuItem menuItem2 = new IconMenuItem("textures_symbol_upgrade", mainToolBar) {
			@Override
			public void onClick() {

			}
		};
		MenuItem menuItem3 = new IconMenuItem("textures_symbol_delete", mainToolBar) {
			@Override
			public void onClick() {

			}
		};
		mainToolBar.addMenuItem(menuItem1);
		mainToolBar.addMenuItem(menuItem2);
		mainToolBar.addMenuItem(menuItem3);
	}

	private void createBuildingToolBar() {
		buildingToolBar.setX(mainToolBar.x - 2*mainToolBar.getWidth()/6);
		for (TowerType type: TowerType.values()) {
			MenuItem item = new IconMenuItem(type.getSprite().getTexture(0, 0), mainToolBar, buildingToolBar) {

				@Override
				public void onClick() {

				}
			};

			buildingToolBar.addMenuItem(item);
		}
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
		MenuRow mouseRow = getMousedMenuRow(mousePositionX, mousePositionY);

		if (mouseRow == null) {
			menuRows = Arrays.asList(mainToolBar);
		} else {
			hightlighted = mouseRow.getMenuItem(mousePositionX);
			menuRows = hightlighted.getMenuRows();
		}

		for (MenuRow menuRow: menuRows) menuRow.update();
	}

	public boolean setMousePosition(float x, float y) {
		this.mousePositionX = x;
		this.mousePositionY = y;
		return getMousedMenuRow(x, y) != null;
	}

	public void setMouseClicked(boolean b) {
		mouseClicked = b;
	}

	private MenuRow getMousedMenuRow(float x, float y) {
		float ty = 1;
		for (MenuRow menuRow: menuRows) {
			ty -= menuRow.getHeight();
			if (y > ty + menuRow.getHeight() || y < ty) continue;

			float w = menuRow.getWidth();
			if (x >= (menuRow.x - w/2)/game.getWindow().getAspectRatio() && x <= (menuRow.x + w/2)/ (game.getWindow().getAspectRatio())) return menuRow;
		}
		return null;
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
		private float height;
		public MenuRow(MenuRow parent, float height) {
			items = new ArrayList<>();
			this.parent = parent;
			this.x = 0;
			this.height = height;
		}
		void setX(float x) {
			this.x = x;
		}

		void draw(Window window, BasicShader shader1, ColorShader shader2, float y) {
			shader2.draw((x - getWidth()/2) / window.getAspectRatio(), y - getHeight(), getWidth() / window.getAspectRatio(), getHeight(), 0x63/255f, 0x36/255f, 0x62/255f, 1);

			float x2 = (x - getWidth()/2);
			for (MenuItem item: items) {
				item.draw(window, shader1, shader2, x2, y, getHeight());
				x2 += item.getWidth();
			}
		}

		float getHeight() {
			return height;
		}

		float getWidth() {
			float w = 0;
			for (MenuItem m: items) w += m.getWidth();
			return w;
		}

		void addMenuItem(MenuItem menuItem) {
			items.add(menuItem);
		}

		MenuItem getMenuItem(float x) {
			float x2 = this.x - getWidth()/2;
			for (MenuItem item: items) {
				if ((x2)/game.getWindow().getAspectRatio() <= x && (x2 + item.getWidth())/game.getWindow().getAspectRatio() >= x) return item;
				x2 += item.getWidth();
			}

			return null;
		}

		void update() {
			if (parent != null && !menuRows.contains(parent)) {
				menuRows.remove(this);
			}
		}
	}


	interface MenuItem {
		void draw(Window window, BasicShader shader1, ColorShader shader2, float x, float y, float height);
		float getWidth();
		void onClick();
		List<MenuRow> getMenuRows();
	}

	abstract class IconMenuItem implements MenuItem {
		private List<MenuRow> menuRowList;
		private Rectangle r;

		public IconMenuItem(String textureName, MenuRow... menuRows) {
			this.r = TextureHandler.getSpriteSheetBounds(textureName);

			menuRowList = Arrays.asList(menuRows);
		}

		public IconMenuItem(Rectangle r, MenuRow... menuRows) {
			this.r = r;

			menuRowList = Arrays.asList(menuRows);
		}

		@Override
		public void draw(Window window, BasicShader shader1, ColorShader shader2, float x, float y, float height) {
			shader1.draw(x / window.getAspectRatio(), y-height, 0.1f / window.getAspectRatio(), height, r.x, r.y, r.width, r.height, false, hightlighted == this ? HIGHTLIGHT_COLOR : NO_HIGHTLIGHT_COLOR);
		}

		@Override
		public float getWidth() {
			return 0.1f;
		}

		@Override
		public List<MenuRow> getMenuRows() {
			return menuRowList;
		}
	}
}
