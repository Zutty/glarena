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

package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.util.MatrixUtils;

/**
 * Class to encapsulate the view matrix.
 */
public class Camera {

    private Vector3f position;
    private Vector3f center;
    private Matrix4f matrix;

    public Camera() {
        position = new Vector3f();
        center = new Vector3f();
        matrix = new Matrix4f();
    }

    public Vector3f getDirection() {
        Vector3f dir = Vector3f.sub(position, center, null);
        return dir.normalise(dir);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setCenter(float x, float y, float z) {
        center.x = x;
        center.y = y;
        center.z = z;
    }

    public void update() {
        matrix = MatrixUtils.lookAt(position.x, position.y, position.z,
                center.x, center.y, center.z,
                0f, 1f, 0f);
    }

    public Matrix4f getViewMatrix() {
        return matrix;
    }
}
