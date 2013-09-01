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

import org.lwjgl.opengl.GL15;
import uk.co.zutty.glarena.Technique;

import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class ElementArrayModel extends ArrayModel {

    private int glIndexVbo = GL_INVALID_VALUE;
    private int numIndices;

    public ElementArrayModel(Technique technique) {
        super(technique);

        glBindVertexArray(glVao);

        glIndexVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);

        glBindVertexArray(0);
    }

    public void setIndexData(ShortBuffer indexData) {
        numIndices = indexData.capacity();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw(int mode) {
        glBindVertexArray(glVao);
        glDrawElements(mode, numIndices, GL_UNSIGNED_SHORT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        GL15.glDeleteBuffers(glIndexVbo);
    }
}
