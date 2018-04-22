package game.window.shader.shader;

import game.window.shader.ShaderProgram;

/**
 * A shader used to draw rectangles with only a single image
 */
public class HealthBarShader extends ShaderProgram {
	private static final String BASIC_FRAGMENT_FILE = "healthBarFragmentShader";
	private static final String BASIC_VERTEX_FILE = "healthBarVertexShader";

	private int xLocation, yLocation, widthLocation, heightLocation;
	private int useCameraLocation, healthLocation;

	public HealthBarShader() {
		super(BASIC_VERTEX_FILE, BASIC_FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {

	}

	@Override
	protected void getUniformLocations() {
		super.getUniformLocations();

		xLocation = getUniformLocation("x");
		yLocation = getUniformLocation("y");
		widthLocation = getUniformLocation("width");
		heightLocation = getUniformLocation("height");
		useCameraLocation = getUniformLocation("useCamera");
		healthLocation = getUniformLocation("health");
	}

	/**
	 * Loads a value into the shader
	 * @param x the x position of the rectangle
	 * @param y the y position of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public void setBounds(float x, float y, float width, float height) {
		setUniform1f(xLocation, x);
		setUniform1f(yLocation, y);
		setUniform1f(widthLocation, width);
		setUniform1f(heightLocation, height);
	}

	/**
	 * Loads a value into the shader
	 * @param useCamera whether the transformation by the camera should be discarded
	 */
	public void setUseCamera(boolean useCamera) {
		setUniform1f(useCameraLocation, useCamera ? 1 : 0);
	}

	public void setHealth(float health) {
		setUniform1f(healthLocation, health);
	}
}
