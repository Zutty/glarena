/*
 * Copyright (c) 2013 George Weller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package uk.co.zutty.glarena.gl;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.GameException;

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
    private Map<ShaderType, Shader> shaders;
    private Map<String, Integer> uniforms;

    public ShaderProgram() {
        glProgram = glCreateProgram();
        shaders = new HashMap<>();
        uniforms = new HashMap<>();
    }

    public void attachShader(Shader shader) {
        glAttachShader(glProgram, shader.getGlObject());
        shaders.put(shader.getType(), shader);
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
        if (loc < 0) {
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

    public void setUniform(String name, Vector3f value) {
        glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Matrix4f value) {
        value.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(getUniformLocation(name), false, matrixBuffer);
    }

    protected int getUniformLocation(String name) {
        if (!uniforms.containsKey(name)) {
            throw new GameException("Uniform '" + name + "' not initialised.");
        }
        return uniforms.get(name);
    }

    public void bindAttribLocation(int index, String name) {
        glBindAttribLocation(glProgram, index, name);
    }

    public void destroy() {
        use();

        for (Shader shader : shaders.values()) {
            glDetachShader(glProgram, shader.getGlObject());
            shader.destroy();
        }

        glDeleteProgram(glProgram);

        useNone();
    }
}
