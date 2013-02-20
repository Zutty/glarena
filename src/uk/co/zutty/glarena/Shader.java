package uk.co.zutty.glarena;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static uk.co.zutty.glarena.util.IOUtils.readSource;

/**
 * Created with IntelliJ IDEA.
 */
public class Shader {

    static enum Type { VERTEX, FRAGMENT }

    private int shader;

    public Shader(Type type) {
        shader = glCreateShader(type == Type.VERTEX ? GL_VERTEX_SHADER : GL_FRAGMENT_SHADER);
    }

    int getHandle() {
        return shader;
    }

    public void loadSource(String filename) {
        setSource(readSource(filename));
    }

    public void setSource(CharSequence source) {
        glShaderSource(shader, source);
    }

    public void compile() {
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
            String msg = glGetShaderInfoLog(shader, len);
            throw new GameException("Failed to compile shader: " + msg);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        glDeleteShader(shader);
    }
}
