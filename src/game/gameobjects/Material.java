package game.gameobjects;

public enum Material {

	WOOD("material_wood"), STONE("material_wood"), ENERGY("material_wood"), METAL("material_wood");

	private String sheetName;
	Material(String sName) {
		this.sheetName = sName;
	}

	public String getSheetName() {
		return sheetName;
	}
}
