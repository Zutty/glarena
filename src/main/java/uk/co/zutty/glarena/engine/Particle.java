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

package uk.co.zutty.glarena.engine;

import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Representation of a single particle, both the logical game entity and VBO format for OpenGL.
 */
public abstract class Particle {

    protected Vector3f position;
    protected Vector3f velocity;
    protected short lifetime;
    protected short timeToLive;
    protected Tween scale;
    protected Tween fade;

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public short getLifetime() {
        return lifetime;
    }

    public void setLifetime(short lifetime) {
        this.lifetime = lifetime;
        this.timeToLive = lifetime;
    }

    public short getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(short timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isDead() {
        return timeToLive <= 0;
    }

    public Tween getScale() {
        return scale;
    }

    public void setScale(Tween scale) {
        this.scale = scale;
    }

    public Tween getFade() {
        return fade;
    }

    public void setFade(Tween fade) {
        this.fade = fade;
    }

    public void update() {
        --timeToLive;
        Vector3f.add(position, velocity, position);
    }

    public float getTweenFactor() {
        return (float) (lifetime - timeToLive) / (float) lifetime;
    }

    public abstract void put(FloatBuffer buffer);
}
