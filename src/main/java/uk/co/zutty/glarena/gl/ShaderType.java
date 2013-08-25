package uk.co.zutty.glarena.gl;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public enum ShaderType {
    VERTEX(GL_VERTEX_SHADER),
    GEOMETRY(GL_GEOMETRY_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER);

    private int glType;

    ShaderType(int glType) {
        this.glType = glType;
    }

    public int getGlType() {
        return glType;
    }
}
