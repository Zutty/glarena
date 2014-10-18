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

package uk.co.zutty.glarena.util;

import org.lwjgl.util.vector.Vector3f;

public class MathUtils {

    public static final float DEGTORAD = (float) (Math.PI / 180d);

    public static final Vector3f EAST = new Vector3f(1f, 0f, 0f);
    public static final Vector3f UP = new Vector3f(0f, 1f, 0f);
    public static final Vector3f NORTH = new Vector3f(0f, 0f, 1f);

    public static float cot(float angle) {
        return (float) (1f / Math.tan(angle));
    }

    public static float degreesToRadians(float degrees) {
        return degrees * DEGTORAD;
    }

    public static float randRange(float from, float to) {
        return ((float) Math.random() * (to - from)) + from;
    }

    public static int randRange(int from, int to) {
        return (int) (Math.random() * (double) (to - from)) + from;
    }

    public static float randAngle() {
        return (float) (Math.random() * 2.0 * Math.PI);
    }

    public static Vector3f randomDirection() {
        double azimuth = Math.random() * 2.0 * Math.PI;
        float z = (float) ((Math.random() * 2.0) - 1.0);
        float f = (float) Math.sqrt(1.0 - z * z);
        return new Vector3f((float) Math.cos(azimuth) * f, (float) Math.sin(azimuth) * f, z);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
