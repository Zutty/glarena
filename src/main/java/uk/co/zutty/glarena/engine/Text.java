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

package uk.co.zutty.glarena.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import uk.co.zutty.glarena.gl.ElementArrayModel;
import uk.co.zutty.glarena.gl.Texture;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static uk.co.zutty.glarena.gl.enums.BufferUsage.STREAM;

public class Text implements Entity {

    private Technique technique;
    private ElementArrayModel model;
    private ModelInstance modelInstance;

    private List<TextInstance> instances = new ArrayList<>();

    public Text(Technique technique, Texture texture) {
        this.technique = technique;
        model = new ElementArrayModel(technique, STREAM);
        modelInstance = new ModelInstance(model, texture);
    }

    public void add(TextInstance textInstance) {
        instances.add(textInstance);
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void setWorld(World world) {}

    @Override
    public void update() {
        float ratio = (float) Display.getWidth() / (float)Display.getHeight();

        int totalChars = 0;
        for(TextInstance text : instances) {
            totalChars += text.getText().length();
        }

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(totalChars * 4 * 7);
        ShortBuffer indexData = BufferUtils.createShortBuffer(totalChars * 6);

        short index = 0;
        for(TextInstance text : instances) {
            float totalWidth = 0f;

            for(int i = 0; i < text.getText().length(); i++) {
                char c = text.getText().charAt(i);

                float x = -1f + (text.getXOffset() + totalWidth)/ratio;
                float y = text.getY();
                float width = 0.1f;
                totalWidth += width;
                float height = 0.2f;
                width /= ratio;

                int val = Integer.valueOf(""+c);
                float u = (float)(val % 8)/8f;
                float v = (float)Math.floor((double)val / 8.0) / 4f;

                vertexData.put(new float[]{
                        x, y,                  u, v + 0.25f,                   255f, 255f, 255f,
                        x + width, y,          u + 0.125f, v + 0.25f,          255f, 255f, 255f,
                        x, y + height,         u, v,           255f, 255f, 255f,
                        x + width, y + height, u + 0.125f, v,  255f, 255f, 255f
                });
                indexData.put(new short[]{
                        index, (short) (index + 1), (short) (index + 2),
                        (short) (index + 2), (short) (index + 1), (short) (index + 3),
                });
                index += 4;
            }
        }

        vertexData.flip();
        indexData.flip();

        model.getVertexBuffer().setData(vertexData, totalChars * 4);
        model.getIndexBuffer().setData(indexData);
    }
}
