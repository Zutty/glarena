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
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Player controlled entity.
 */
public class Gunship extends AbstractEntity {

    public static final float SPEED = .4f;
    public static final float DEAD_ZONE = 0.1f;

    private long timer = 10L;
    private float yawRadians = 0;
    private Emitter bulletEmitter;
    private Vector4f emitPointL;
    private Vector4f emitPointR;
    private boolean emitAlt;
    private Gamepad gamepad;

    public Gunship(ModelInstance modelInstance) {
        setModelInstance(modelInstance);
        emitPointL = new Vector4f(0.7f, 0, 2.9f, 1);
        emitPointR = new Vector4f(-0.7f, 0, 2.9f, 1);
        emitAlt = true;
    }

    public void setGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void setBulletEmitter(Emitter bulletEmitter) {
        this.bulletEmitter = bulletEmitter;
    }

    @Override
    public void update() {
        if (gamepad.getLeftStick().lengthSquared() > DEAD_ZONE) {
            position.x -= gamepad.getLeftStick().x * SPEED;
            position.z -= gamepad.getLeftStick().y * SPEED;
        }

        Vector2f direction = (gamepad.getRightStick().lengthSquared() > DEAD_ZONE) ? gamepad.getRightStick() : ((gamepad.getLeftStick().lengthSquared() > DEAD_ZONE) ? gamepad.getLeftStick() : null);

        float prevYaw = yaw;

        if (direction != null) {
            yawRadians = (float) Math.atan2(-direction.x, -direction.y);
            yaw = yawRadians * (180f / (float) Math.PI);
        }

        roll = easeAngle(roll, (prevYaw - yaw) * 10f);

        super.update();

        ++timer;
        if (gamepad.getRightStick().lengthSquared() > DEAD_ZONE || gamepad.isButtonDown()) {
            if (timer >= 3L) {
                timer = 0;
                Vector4f emitPosition = new Vector4f((emitAlt = !emitAlt) ? emitPointL : emitPointR);
                Matrix4f.transform(getModelInstance().getMatrix(), emitPosition, emitPosition); //TODO a bit of a hack

                bulletEmitter.emitFrom(xyz(emitPosition), new Vector3f((float) Math.sin(yawRadians), 0, (float) Math.cos(yawRadians)), 1.2f, 100);
            }
        }
    }

    private float easeAngle(float current, float target) {
        return current + delta(target, current) / 10f;
    }

    private float delta(float a, float b) {
        float diff = Math.abs(a - b) % 360f;
        diff = (diff > 180) ? 360f - diff : diff;
        return (a > b) ? diff : -diff;
    }

    public static Vector3f xyz(Vector4f v) {
        return new Vector3f(v.x, v.y, v.z);
    }
}
