package uk.co.zutty.glarena.gl;

import uk.co.zutty.glarena.GameException;

import java.nio.charset.Charset;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static uk.co.zutty.glarena.util.IOUtils.readSource;

/**
 * Represents a shader object in the OpenGL context.
 */
public class Shader {

    private ShaderType type;
    private int glShader;

    public Shader(ShaderType type) {
        this.type = type;
        glShader = glCreateShader(type.getGlType());
    }

    public ShaderType getType() {
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
