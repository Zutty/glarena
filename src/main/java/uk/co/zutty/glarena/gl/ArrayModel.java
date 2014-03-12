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

import uk.co.zutty.glarena.engine.Technique;
import uk.co.zutty.glarena.vertex.Attribute;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class ArrayModel implements Model {

    private static final int FLOAT_BYTES = 4;
    protected Technique technique;
    protected int glVao = GL_INVALID_VALUE;
    protected int glArrayVbo = GL_INVALID_VALUE;
    private int numVertices;

    public ArrayModel(Technique technique) {
        this.technique = technique;

        glVao = glGenVertexArrays();
        glBindVertexArray(glVao);

        glArrayVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glArrayVbo);

        int index = 0;
        for (Attribute attribute : technique.getFormat().getAttributes()) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index++, attribute.getElements(), GL_FLOAT, false, technique.getFormat().getStride(), attribute.getOffset());
        }

        glBindVertexArray(0);
    }

    public void setVertexData(FloatBuffer vertexData, int numVertices) {
        this.numVertices = numVertices;

        glBindBuffer(GL_ARRAY_BUFFER, glArrayVbo);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void updateVertexData(FloatBuffer vertexData, int numVertices) {
        this.numVertices = numVertices;
        int dataSize = vertexData.capacity() * FLOAT_BYTES;

        glBindBuffer(GL_ARRAY_BUFFER, glArrayVbo);
        glBufferData(GL_ARRAY_BUFFER, dataSize, GL_STREAM_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexData);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    @Override
    public Technique getTechnique() {
        return technique;
    }

    @Override
    public void draw(int mode) {
        glBindVertexArray(glVao);
        glDrawArrays(mode, 0, numVertices);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glDeleteVertexArrays(glVao);
        glDeleteBuffers(glArrayVbo);
    }
}
