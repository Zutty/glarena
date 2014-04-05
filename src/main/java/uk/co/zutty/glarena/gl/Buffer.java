/*
 * Copyright (c) 2014 George Weller
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

import uk.co.zutty.glarena.gl.enums.BufferType;
import uk.co.zutty.glarena.gl.enums.BufferUsage;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Encapsulates a VBO.
 */
public class Buffer {

    private int glVbo;
    private int glType;
    private int glUsage;
    private int numElements = 0;

    public Buffer(BufferType type, BufferUsage usage) {
        glVbo = glGenBuffers();
        glType = type.getGlType();
        glUsage = usage.getGlUsage();
    }

    public int getNumElements() {
        return numElements;
    }

    public void bind() {
        glBindBuffer(glType, glVbo);
    }

    public void setData(FloatBuffer data, int numElements) {
        this.numElements = numElements;

        glBindBuffer(glType, glVbo);
        glBufferData(glType, data, glUsage);
        glBindBuffer(glType, 0);
    }

    public void setData(ShortBuffer data) {
        this.numElements = data.capacity();

        glBindBuffer(glType, glVbo);
        glBufferData(glType, data, glUsage);
        glBindBuffer(glType, 0);
    }

    public void destroy() {
        glDeleteBuffers(glVbo);
    }
}
