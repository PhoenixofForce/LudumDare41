package game.gameobjects;

public enum Material {

	GOLD("material_gold"), WOOD("material_wood"), STONE("material_stone");

	private String sheetName;
	Material(String sName) {
		this.sheetName = sName;
	}

	public String getSheetName() {
		return sheetName;
	}
}
