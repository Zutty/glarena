package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created with IntelliJ IDEA.
 */
public class ShaderProgram {

    private int shaderProgram;
    private Map<String, Integer> uniforms;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram() {
        shaderProgram = glCreateProgram();
        uniforms = new HashMap<String, Integer>();
    }

    int getHandle() {
        return shaderProgram;
    }

    public void attachShader(Shader shader) {
        glAttachShader(shaderProgram, shader.getHandle());
    }

    public void link() {
        glLinkProgram(shaderProgram);
    }

    public void validate() {
        glValidateProgram(shaderProgram);
    }

    public void use() {
        glUseProgram(shaderProgram);
    }

    public static void useNone() {
        glUseProgram(0);
    }

    public void initUniform(String name) {
        int loc = glGetUniformLocation(shaderProgram, name);
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
        glBindAttribLocation(shaderProgram, index, name);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        glDeleteProgram(shaderProgram);
        //GL20.glUseProgram(0);
        //GL20.glDetachShader(pId, vsId);
        //GL20.glDetachShader(pId, fsId);

        //GL20.glDeleteShader();
        //GL20.glDeleteShader(fsId);
        //GL20.glDeleteProgram(pId);
    }

    public static ShaderProgram build(String vertexFile, String fragmentFile) {
        ShaderProgram shader = new ShaderProgram();

        Shader vertexShader = new Shader(Shader.Type.VERTEX);
        vertexShader.loadSource(vertexFile);
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT);
        fragmentShader.loadSource(fragmentFile);
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        //shader.link();
        //shader.validate();

        return shader;
    }
}
