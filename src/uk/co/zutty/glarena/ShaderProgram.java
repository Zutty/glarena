package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Represents a shader program in the OpenGL context.
 */
public class ShaderProgram {

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private int glProgram;
    private Shader vertexShader;
    private Shader fragmentShader;
    private Map<String, Integer> uniforms;

    public ShaderProgram() {
        glProgram = glCreateProgram();
        uniforms = new HashMap<String, Integer>();
    }

    public int getGlObject() {
        return glProgram;
    }

    public void attachShader(Shader shader) {
        glAttachShader(glProgram, shader.getGlObject());
    }

    public void link() {
        glLinkProgram(glProgram);
    }

    public void validate() {
        glValidateProgram(glProgram);
    }

    public void use() {
        glUseProgram(glProgram);
    }

    public static void useNone() {
        glUseProgram(0);
    }

    public void initUniform(String name) {
        int loc = glGetUniformLocation(glProgram, name);
        if(loc < 0) {
            throw new GameException("Uniform '" + name + "' not found.");
        }
        uniforms.put(name, loc);
    }

    public void setUniform(String name, FloatBuffer value) {
        glUniform1(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, Matrix4f value) {
        value.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(getUniformLocation(name), false, matrixBuffer);
    }

    protected int getUniformLocation(String name) {
        if(!uniforms.containsKey(name)) {
            throw new GameException("Uniform '" + name + "' not initialised.");
        }
        return uniforms.get(name);
    }

    public void bindAttribLocation(int index, String name) {
        glBindAttribLocation(glProgram, index, name);
    }

    public void destroy() {
        use();

        GL20.glDetachShader(glProgram, vertexShader.getGlObject());
        GL20.glDetachShader(glProgram, fragmentShader.getGlObject());

        vertexShader.destroy();
        fragmentShader.destroy();
        GL20.glDeleteProgram(glProgram);

        // TODO we don't know that these are 0, 1, and 2. Perhaps tie to bindAttribLocation.
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        useNone();
    }

    public static ShaderProgram build(String vertexFile, String fragmentFile) {
        ShaderProgram shader = new ShaderProgram();

        Shader vertexShader = new Shader(Shader.Type.VERTEX);
        vertexShader.loadSource(vertexFile);
        vertexShader.compile();
        shader.attachShader(vertexShader);
        shader.vertexShader = vertexShader;

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT);
        fragmentShader.loadSource(fragmentFile);
        fragmentShader.compile();
        shader.attachShader(fragmentShader);
        shader.fragmentShader = fragmentShader;

        //shader.link();
        //shader.validate();

        return shader;
    }
}
