package uk.co.zutty.glarena;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static uk.co.zutty.glarena.util.IOUtils.readSource;

/**
 * Represents a shader object in the OpenGL context.
 */
public class Shader {

    static enum Type {VERTEX, FRAGMENT}

    private int glShader;

    public Shader(Type type) {
        glShader = glCreateShader(type == Type.VERTEX ? GL_VERTEX_SHADER : GL_FRAGMENT_SHADER);
    }

    public int getGlObject() {
        return glShader;
    }

    public void loadSource(String filename) {
        setSource(readSource(filename));
    }

    public void setSource(CharSequence source) {
        glShaderSource(glShader, source);
    }

    public void compile() {
        glCompileShader(glShader);

        if (glGetShaderi(glShader, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(glShader, GL_INFO_LOG_LENGTH);
            String msg = glGetShaderInfoLog(glShader, len);
            throw new GameException("Failed to compile shader: " + msg);
        }
    }

    public void destroy() {
        glDeleteShader(glShader);
    }
}
