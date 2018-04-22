package game.window.shader;

import game.window.shader.shader.*;

import java.util.function.Supplier;

public enum ShaderType {
	MENU_SHADER(MenuShader::new), BASIC_SHADER(BasicShader::new), STATIC_SHADER(StaticShader::new), PARTICLE_SHADER(ParticleShader::new), COLOR_SHADER(ColorShader::new), TEXT_SHADER(TextShader::new), HEALTH_BAR_SHADER(HealthBarShader::new);

	private Supplier<ShaderProgram> shader;

	ShaderType(Supplier<ShaderProgram> shader) {
		this.shader = shader;
	}

	public ShaderProgram createShader() {
		return shader.get();
	}
}
