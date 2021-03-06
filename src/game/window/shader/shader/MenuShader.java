package game.window.shader.shader;

import game.window.shader.ShaderProgram;
import org.lwjgl.opengl.GL11;

/**
 * A shader used to draw rectangles with only a single image
 */
public class MenuShader extends ShaderProgram {
	private static final String BASIC_FRAGMENT_FILE = "menuFragmentShader";
	private static final String BASIC_VERTEX_FILE = "menuVertexShader";

	private int xLocation, yLocation, widthLocation, heightLocation;
	private int useCameraLocation, amountLocation, screenRatioLocation, sizeLocation, borderLocation;

	public MenuShader() {
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
		amountLocation = getUniformLocation("amount");
		screenRatioLocation = getUniformLocation("screenRatio");
		sizeLocation = getUniformLocation("size");
		borderLocation = getUniformLocation("border");
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

	public void setAmount(int amount) {
		setUniform1i(amountLocation, amount);
	}

	public void setSize(float size) {
		setUniform1f(sizeLocation, size);
	}

	public void setBorder(float border) {
		setUniform1f(borderLocation, border);
	}

	public void setScreenRatio(float screenRatio) {
		setUniform1f(screenRatioLocation, screenRatio);
	}

	public void draw(float x, float y, float width, float height, boolean useCamera, int amount, float screenRatio, float size, float border) {
		start();
		setBounds(x, y, width, height);
		setUseCamera(useCamera);
		setAmount(amount);
		setScreenRatio(screenRatio);
		setSize(size);
		setBorder(border);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
}
