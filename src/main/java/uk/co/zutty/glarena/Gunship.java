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

import org.lwjgl.util.vector.*;
import uk.co.zutty.glarena.engine.*;
import uk.co.zutty.glarena.util.MathUtils;

import static uk.co.zutty.glarena.engine.Tween.Easing.EXPO_OUT;
import static uk.co.zutty.glarena.engine.Tween.Easing.LINEAR;

/**
 * Player controlled entity.
 */
public class Gunship extends AbstractEntity {

    public static final float SPEED = 1f;
    public static final float FIRING_SPEED = 0.9f;
    public static final float DEAD_ZONE = 0.1f;
    public static final float ROLL_ANGLE = 10f;

    public static final Vector2f BOUNDS = new Vector2f(40f, 30f);
    public static final float BULLET_SPEED = 3f;

    private long timer = 10L;
    private Emitter bulletEmitter;
    private Vector4f emitPointL;
    private Vector4f emitPointR;
    private Gamepad gamepad;

    private Vector3f levelPosition = new Vector3f();
    private Vector2f screenPosition = new Vector2f();

    public Gunship(ModelInstance modelInstance) {
        setModelInstance(modelInstance);
        emitPointL = new Vector4f(0.7f, 0, 2.9f, 1);
        emitPointR = new Vector4f(-0.7f, 0, 2.9f, 1);
    }

    public void setGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void setBulletEmitter(Emitter bulletEmitter) {
        this.bulletEmitter = bulletEmitter;
    }

    public void setLevelPosition(Vector3f levelPosition) {
        this.levelPosition = levelPosition;
        //this.

    }

    @Override
    public void update() {
        float prevX = screenPosition.x;

        float currentSpeed = gamepad.isButtonDown() ? FIRING_SPEED : SPEED;

        if (gamepad.getLeftStick().lengthSquared() > DEAD_ZONE) {
            screenPosition.x -= gamepad.getLeftStick().x * currentSpeed;
            screenPosition.y -= gamepad.getLeftStick().y * currentSpeed;

            screenPosition.x = MathUtils.clamp(screenPosition.x, -BOUNDS.x, BOUNDS.x);
            screenPosition.y = MathUtils.clamp(screenPosition.y, -BOUNDS.y, BOUNDS.y);
        }

        position.set(levelPosition);
        position.x += screenPosition.x;
        position.z += screenPosition.y;

        float rollFactor = prevX - position.x;
        float rollClamp = (rollFactor < 0f) ? -1f : (rollFactor > 0f) ? 1f : 0f;

        roll = easeAngle(roll, rollClamp * ROLL_ANGLE);

        super.update();

        ++timer;
        if (gamepad.isButtonDown()) {
            if (timer >= 3L) {
                timer = 0;
                Vector4f emitPosition = new Vector4f();
                Matrix4f.transform(getModelInstance().getMatrix(), emitPointL, emitPosition);

                float yawRadians = 0;
                //yawRadians = (float) Math.atan2(-direction.x, -direction.y);
                Vector3f direction = new Vector3f((float) Math.sin(yawRadians), 0, (float) Math.cos(yawRadians));

                Particle particle = bulletEmitter.emitFrom(xyz(emitPosition), direction, BULLET_SPEED, 100);
                particle.setScale(new Tween(1f, 1f, LINEAR));
                particle.setFade(new Tween(1f, 0f, EXPO_OUT));

                Matrix4f.transform(getModelInstance().getMatrix(), emitPointR, emitPosition);

                particle = bulletEmitter.emitFrom(xyz(emitPosition), direction, BULLET_SPEED, 100);
                particle.setScale(new Tween(1f, 1f, LINEAR));
                particle.setFade(new Tween(1f, 0f, EXPO_OUT));
            }
        }
    }

    private float easeAngle(float current, float target) {
        return current + delta(target, current) / 2f;
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
