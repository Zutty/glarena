package uk.co.zutty.glarena;

import org.lwjgl.opengl.GL32;

import java.nio.charset.Charset;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static uk.co.zutty.glarena.util.IOUtils.readSource;

/**
 * Represents a shader object in the OpenGL context.
 */
public class Shader {

    static enum Type {
        VERTEX(GL_VERTEX_SHADER),
        GEOMETRY(GL_GEOMETRY_SHADER),
        FRAGMENT(GL_FRAGMENT_SHADER);

        private int glType;

        Type(int glType) {
            this.glType = glType;
        }

        public int getGlType() {
            return glType;
        }
    }

    private Type type;
    private int glShader;

    public Shader(Type type) {
        this.type = type;
        glShader = glCreateShader(type.getGlType());
    }

    public Type getType() {
        return type;
    }

    public int getGlObject() {
        return glShader;
    }

    public void loadSource(String filename) {
        setSource(readSource(filename));
    }

    public void setSource(CharSequence source) {
        if(!Charset.forName("US-ASCII").newEncoder().canEncode(source)) {
            throw new IllegalArgumentException("Shader source contains illegal characters");
        }
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
