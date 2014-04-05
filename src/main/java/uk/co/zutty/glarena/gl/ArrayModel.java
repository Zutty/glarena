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
import uk.co.zutty.glarena.gl.enums.BufferUsage;
import uk.co.zutty.glarena.vertex.Attribute;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static uk.co.zutty.glarena.gl.enums.BufferType.ARRAY;

public class ArrayModel implements Model {

    protected Technique technique;
    protected int glVao = GL_INVALID_VALUE;
    private Buffer vertexBuffer;

    public ArrayModel(Technique technique, BufferUsage usage) {
        this.technique = technique;

        glVao = glGenVertexArrays();
        glBindVertexArray(glVao);

        vertexBuffer = new Buffer(ARRAY, usage);
        vertexBuffer.bind();

        int index = 0;
        for (Attribute attribute : technique.getFormat().getAttributes()) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index++, attribute.getElements(), GL_FLOAT, false, technique.getFormat().getStride(), attribute.getOffset());
        }

        glBindVertexArray(0);
    }

    public Buffer getVertexBuffer() {
        return vertexBuffer;
    }

    @Override
    public Technique getTechnique() {
        return technique;
    }

    @Override
    public void draw(int mode) {
        glBindVertexArray(glVao);
        glDrawArrays(mode, 0, vertexBuffer.getNumElements());
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glDeleteVertexArrays(glVao);
        vertexBuffer.destroy();
    }
}
