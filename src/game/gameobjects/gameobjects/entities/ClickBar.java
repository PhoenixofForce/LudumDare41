package game.gameobjects.gameobjects.entities;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.TextureHandler;
import game.window.Drawable;
import game.window.Keyboard;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import game.window.shader.shader.MenuShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickBar extends AbstractGameObject implements Drawable {
	public static final float BORDER = 0.0f;
	public static final float SIZE = 0.3f;
	public static final float[] NO_HIGHTLIGHT_COLOR = new float[]{0, 0, 0, 1};
	public static final float[] HIGHTLIGHT_COLOR = new float[]{0.125f, 0.125f, 0.125f, 1};
	private MenuRow mainToolBar = new MenuRow(null, SIZE);

	private List<MenuRow> menuRows;

	private float mousePositionX, mousePositionY;
	private boolean mouseClicked;

	private MenuItem hightlighted;

	public ClickBar() {
		menuRows = new ArrayList<>();

		createMainToolBar();

		menuRows.add(mainToolBar);
	}

	private void createMainToolBar() {
		mainToolBar.setX(0);

		MenuItem menuItem1 = new IconMenuItem("textures_material_wood", mainToolBar) {
			@Override
			public void onClick() {
				game.getMaterial(Material.WOOD).add(1);
			}
		};
		MenuItem menuItem2 = new IconMenuItem("textures_material_stone", mainToolBar) {
			@Override
			public void onClick() {
				game.getMaterial(Material.STONE).add(1);
			}
		};

		mainToolBar.addMenuItem(menuItem1);
		mainToolBar.addMenuItem(menuItem2);
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
			hightlighted = null;
			menuRows = Arrays.asList(mainToolBar);
		} else {
			hightlighted = mouseRow.getMenuItem(mousePositionX, mousePositionY);
			if (hightlighted == null) {
				menuRows = Arrays.asList(mainToolBar);
			} else {
				menuRows = hightlighted.getMenuRows();

				if (mouseClicked) hightlighted.onClick();
			}
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
		float ty = -1;
		for (MenuRow menuRow: menuRows) {
			ty += menuRow.getHeight();
			if (y > ty  || y < ty - menuRow.getHeight()) continue;

			float w = menuRow.getWidth();
			if (x >= (menuRow.x - w/2)/game.getWindow().getAspectRatio() && x <= (menuRow.x + w/2)/ (game.getWindow().getAspectRatio())) {
				return menuRow;
			}
		}
		return null;
	}

	@Override
	public void draw(Window window, long time) {
		MenuShader menuShader = (MenuShader) window.getShaderHandler().getShader(ShaderType.MENU_SHADER);
		BasicShader basicShader = (BasicShader) window.getShaderHandler().getShader(ShaderType.BASIC_SHADER);

		float y = -1;
		for (MenuRow menuRow: menuRows) {
			y += menuRow.getHeight();
			menuRow.draw(window, basicShader, menuShader, y);
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

		void draw(Window window, BasicShader shader1, MenuShader shader2, float y) {
			//shader2.draw((x - getWidth()/2) / window.getAspectRatio(), y - getHeight(), getWidth() / window.getAspectRatio(), getHeight(), false, items.size(), window.getAspectRatio(), SIZE, BORDER);

			float x2 = (x - getWidth()/2)+4*BORDER*SIZE;
			for (MenuItem item: items) {
				item.draw(window, shader1, shader2, x2, y-4*BORDER*SIZE, getHeight()-8*BORDER*SIZE);
				x2 += item.getWidth();
			}
		}

		float getHeight() {
			return height+8*BORDER*SIZE;
		}

		float getWidth() {
			float w = 8*BORDER*SIZE;
			for (MenuItem m: items) w += m.getWidth();
			return w;
		}

		void addMenuItem(MenuItem menuItem) {
			items.add(menuItem);
		}

		MenuItem getMenuItem(float x, float y) {
			float x2 = this.x - getWidth()/2+4*BORDER*SIZE;
			if (x <= (x2)/game.getWindow().getAspectRatio()) return items.get(0);

			for (MenuItem item: items) {
				if ((x2)/game.getWindow().getAspectRatio() <= x && (x2 + item.getWidth())/game.getWindow().getAspectRatio() >= x) return item;
				x2 += item.getWidth();
			}

			return items.get(items.size()-1);
		}

		void update() {
			if (parent != null && !menuRows.contains(parent)) {
				menuRows.remove(this);
			}
		}
	}


	interface MenuItem {
		void draw(Window window, BasicShader shader1, MenuShader shader2, float x, float y, float height);
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
		public void draw(Window window, BasicShader shader1, MenuShader shader2, float x, float y, float height) {
			float border = BORDER;
			if (hightlighted == this && game.getWindow().getKeyboard().isPressed(Keyboard.MOUSE_BUTTON_1)) border = 0.0625f;
			shader1.draw((x+ border*getWidth()) / window.getAspectRatio(), y-height*(1-border), (1-2*border)*getWidth() / window.getAspectRatio(), height*(1-2*border), r.x, r.y, r.width, r.height, false, hightlighted == this ? HIGHTLIGHT_COLOR : NO_HIGHTLIGHT_COLOR);
		}

		@Override
		public float getWidth() {
			return 2*SIZE;
		}

		@Override
		public List<MenuRow> getMenuRows() {
			return menuRowList;
		}
	}
}
