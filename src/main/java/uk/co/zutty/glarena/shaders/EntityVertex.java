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

package uk.co.zutty.glarena.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.Vertex;

import java.nio.FloatBuffer;

/**
 * Vertex definition for interleaved VBOs.
 */
public class EntityVertex implements Vertex {

    private float positionX;
    private float positionY;
    private float positionZ;

    private float normalX;
    private float normalY;
    private float normalZ;

    private float texCoordS;
    private float texCoordT;

    public void setPosition(Vector3f position) {
        positionX = position.x;
        positionY = position.y;
        positionZ = position.z;
    }

    public void setNormal(Vector3f normal) {
        normalX = normal.x;
        normalY = normal.y;
        normalZ = normal.z;
    }

    public void setTexCoord(Vector2f texCoord) {
        texCoordS = texCoord.x;
        texCoordT = texCoord.y;
    }

    @Override
    public void put(FloatBuffer vertexBuffer) {
        vertexBuffer.put(positionX);
        vertexBuffer.put(positionY);
        vertexBuffer.put(positionZ);

        vertexBuffer.put(normalX);
        vertexBuffer.put(normalY);
        vertexBuffer.put(normalZ);

        vertexBuffer.put(texCoordS);
        vertexBuffer.put(texCoordT);
    }
}
