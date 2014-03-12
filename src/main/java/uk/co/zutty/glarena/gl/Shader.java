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

import uk.co.zutty.glarena.engine.GameException;

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
        if (!Charset.forName("US-ASCII").newEncoder().canEncode(source)) {
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
