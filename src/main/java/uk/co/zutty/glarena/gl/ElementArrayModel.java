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

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static uk.co.zutty.glarena.gl.enums.BufferType.ELEMENT_ARRAY;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class ElementArrayModel extends ArrayModel {

    private Buffer indexBuffer;

    public ElementArrayModel(Technique technique, BufferUsage usage) {
        super(technique, usage);

        glBindVertexArray(glVao);

        indexBuffer = new Buffer(ELEMENT_ARRAY, usage);
        indexBuffer.bind();

        glBindVertexArray(0);
    }

    public Buffer getIndexBuffer() {
        return indexBuffer;
    }

    @Override
    public void draw(int mode) {
        glBindVertexArray(glVao);
        glDrawElements(mode, indexBuffer.getNumElements(), GL_UNSIGNED_SHORT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        indexBuffer.destroy();
    }
}
