package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.Material;
import game.gameobjects.gameobjects.entities.entities.BuildingType;
import game.gameobjects.gameobjects.entities.entities.TowerType;
import game.util.TextureHandler;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import game.window.shader.shader.MenuShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu extends AbstractGameObject implements Drawable {
	public static final float BORDER = 0.042f;
	public static final float SIZE = 0.1f;
	public static final float[] NO_HIGHTLIGHT_COLOR = new float[]{0, 0, 0, 1};
	public static final float[] HIGHTLIGHT_COLOR = new float[]{0.125f, 0.125f, 0.125f, 1};
	private MenuRow mainToolBar = new MenuRow(null, SIZE);
	private MenuRow buildingToolBar = new MenuRow(mainToolBar, 2 * SIZE);
	private MenuRow buildingToolBar2 = new MenuRow(buildingToolBar, 2 * SIZE);

	private List<MenuRow> menuRows;

	private float mousePositionX, mousePositionY;
	private boolean mouseClicked;

	private MenuItem hightlighted;

	public Menu() {
		menuRows = new ArrayList<>();

		createMainToolBar();
		createBuildingToolBar();
		createBuildingToolBar2();

		menuRows.add(mainToolBar);
		menuRows.add(buildingToolBar);
		menuRows.add(buildingToolBar2);
	}

	private void createMainToolBar() {
		mainToolBar.setX(0);

		MenuItem menuItem1 = new IconMenuItem("textures_symbol_build", mainToolBar, buildingToolBar, buildingToolBar2) {
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
				game.setDestroyTowers();
			}

			@Override
			public boolean get3() {
				return true;
			}
		};
		mainToolBar.addMenuItem(menuItem1);
		mainToolBar.addMenuItem(menuItem2);
		mainToolBar.addMenuItem(menuItem3);
	}

	private void createBuildingToolBar() {
		buildingToolBar.setX(mainToolBar.x - 2 * mainToolBar.getWidth() / 6);
		for (TowerType type : TowerType.values()) {
			MenuItem item = new IconMenuItem(type.getSprite().getTexture(0, 0), mainToolBar, buildingToolBar, buildingToolBar2) {

				@Override
				public void onClick() {
					menuRows = Arrays.asList(mainToolBar);
					game.setSelectedTower(type);
				}

				@Override
				public TowerType get1() {
					return type;
				}
			};

			buildingToolBar.addMenuItem(item);
		}
	}

	private void createBuildingToolBar2() {
		buildingToolBar2.setX(mainToolBar.x - 2 * mainToolBar.getWidth() / 6);
		for (BuildingType type : BuildingType.values()) {
			MenuItem item = new IconMenuItem(type.getSprite().getTexture(0, 0), mainToolBar, buildingToolBar, buildingToolBar2) {

				@Override
				public void onClick() {
					menuRows = Arrays.asList(mainToolBar);
					game.setSelectedBuilding(type);
				}

				@Override
				public BuildingType get2() {
					return type;
				}
			};

			buildingToolBar2.addMenuItem(item);
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
			hightlighted = null;
			menuRows = Arrays.asList(mainToolBar);

			if (game.whoDidThis == 37525423) {
				game.removeToolTip();
			}
		} else {
			hightlighted = mouseRow.getMenuItem(mousePositionX, mousePositionY);
			if (hightlighted == null) {
				menuRows = Arrays.asList(mainToolBar);
			} else {
				menuRows = hightlighted.getMenuRows();
				if (hightlighted.get3()) {
					game.textGold.setText("");
					game.textWood.setText("");
					game.textStone.setText("");
					game.textInfo.setText("Destroy a tower to get back half the price");
					game.textInfo.setColor(Color.WHITE);

					game.whoDidThis = 37525423;
				} else if (hightlighted.get1() != null) {
					TowerType b = hightlighted.get1();

					float factor = (float) Math.pow(Game.TOWER_FACTOR, game.getTowerCount(b));
					game.textGold.setText("<gold> " + Math.round(factor * b.getGoldCosts()));
					game.textGold.setColor(game.getMaterial(Material.GOLD).getAmount() >= Math.round(factor * b.getGoldCosts()) ? Color.WHITE : Color.RED);
					game.textWood.setText("<wood> " + Math.round(factor * b.getWoodCosts()));
					game.textWood.setColor(game.getMaterial(Material.WOOD).getAmount() >= Math.round(factor * b.getWoodCosts()) ? Color.WHITE : Color.RED);
					game.textStone.setText("<stone> " + Math.round(factor * b.getStoneCosts()));
					game.textStone.setColor(game.getMaterial(Material.STONE).getAmount() >= Math.round(factor * b.getStoneCosts()) ? Color.WHITE : Color.RED);
					game.textInfo.setText("Builds a new " + b.name() + " tower");
					game.textInfo.setColor(Color.WHITE);

					game.whoDidThis = 37525423;
				} else if (hightlighted.get2() != null) {
					BuildingType b = hightlighted.get2();

					float factor = (float) Math.pow(Game.BUILDING_FACTOR, game.getBuildingCount(b));
					game.textGold.setText("<gold> " + Math.round(factor * b.getGoldCosts()));
					game.textGold.setColor(game.getMaterial(Material.GOLD).getAmount() >= Math.round(factor * b.getGoldCosts()) ? Color.WHITE : Color.RED);
					game.textWood.setText("<wood> " + Math.round(factor * b.getWoodCosts()));
					game.textWood.setColor(game.getMaterial(Material.WOOD).getAmount() >= Math.round(factor * b.getWoodCosts()) ? Color.WHITE : Color.RED);
					game.textStone.setText("<stone> " + Math.round(factor * b.getStoneCosts()));
					game.textStone.setColor(game.getMaterial(Material.STONE).getAmount() >= Math.round(factor * b.getStoneCosts()) ? Color.WHITE : Color.RED);
					game.textInfo.setText("Builds a new " + b.name() + " building");
					game.textInfo.setColor(Color.WHITE);

					game.whoDidThis = 37525423;
				} else if (game.whoDidThis == 37525423) {
					game.removeToolTip();
				}

				if (mouseClicked) hightlighted.onClick();
			}
		}

		for (MenuRow menuRow : menuRows) menuRow.update();
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
		for (MenuRow menuRow : menuRows) {
			ty -= menuRow.getHeight();
			if (y > ty + menuRow.getHeight() || y < ty) continue;

			float w = menuRow.getWidth();
			if (x >= (menuRow.x - w / 2) / game.getWindow().getAspectRatio() && x <= (menuRow.x + w / 2) / (game.getWindow().getAspectRatio())) {
				return menuRow;
			}
		}
		return null;
	}

	@Override
	public void draw(Window window, long time) {
		MenuShader menuShader = (MenuShader) window.getShaderHandler().getShader(ShaderType.MENU_SHADER);
		BasicShader basicShader = (BasicShader) window.getShaderHandler().getShader(ShaderType.BASIC_SHADER);

		float y = 1;
		for (MenuRow menuRow : menuRows) {
			menuRow.draw(window, basicShader, menuShader, y);
			y -= menuRow.getHeight();
		}
	}

	@Override
	public void setup(Window window) {

	}

	@Override
	public void cleanUp(Window window) {

	}

	interface MenuItem {
		void draw(Window window, BasicShader shader1, MenuShader shader2, float x, float y, float height);

		float getWidth();

		void onClick();

		List<MenuRow> getMenuRows();

		default TowerType get1() {
			return null;
		}

		default BuildingType get2() {
			return null;
		}

		default boolean get3() {
			return false;
		}
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
			shader2.draw((x - getWidth() / 2) / window.getAspectRatio(), y - getHeight(), getWidth() / window.getAspectRatio(), getHeight(), false, items.size(), window.getAspectRatio(), SIZE, BORDER);

			float x2 = (x - getWidth() / 2) + 4 * BORDER * SIZE;
			for (MenuItem item : items) {
				item.draw(window, shader1, shader2, x2, y - 4 * BORDER * SIZE, getHeight() - 8 * BORDER * SIZE);
				x2 += item.getWidth();
			}
		}

		float getHeight() {
			return height + 8 * BORDER * SIZE;
		}

		float getWidth() {
			float w = 8 * BORDER * SIZE;
			for (MenuItem m : items) w += m.getWidth();
			return w;
		}

		void addMenuItem(MenuItem menuItem) {
			items.add(menuItem);
		}

		MenuItem getMenuItem(float x, float y) {
			float x2 = this.x - getWidth() / 2 + 4 * BORDER * SIZE;
			if (x <= (x2) / game.getWindow().getAspectRatio()) return items.get(0);

			for (MenuItem item : items) {
				if ((x2) / game.getWindow().getAspectRatio() <= x && (x2 + item.getWidth()) / game.getWindow().getAspectRatio() >= x)
					return item;
				x2 += item.getWidth();
			}

			return items.get(items.size() - 1);
		}

		void update() {
			if (parent != null && !menuRows.contains(parent)) {
				menuRows.remove(this);
			}
		}
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
			shader1.draw((x + BORDER * getWidth()) / window.getAspectRatio(), y - height * (1 - BORDER), (1 - 2 * BORDER) * getWidth() / window.getAspectRatio(), height * (1 - 2 * BORDER), r.x, r.y, r.width, r.height, false, hightlighted == this ? HIGHTLIGHT_COLOR : NO_HIGHTLIGHT_COLOR);
		}

		@Override
		public float getWidth() {
			return SIZE;
		}

		@Override
		public List<MenuRow> getMenuRows() {
			return menuRowList;
		}
	}
}
