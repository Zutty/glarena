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

import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.*;
import uk.co.zutty.glarena.util.MathUtils;

import static uk.co.zutty.glarena.engine.Tween.Easing.LINEAR;

/**
 * AI controlled enemy entity.
 */
public class Ufo extends AbstractEntity {

    private static final float HIT_RADIUS_SQUARED = 1.8f * 1.8f;
    private static final int FIRE_INTERVAL = 30;
    private static final float BULLET_SPEED = 0.3f;
    private static final float SPEED = 0.6f;
    protected Game game;

    private Emitter playerBulletEmitter;
    private Effect explosionEffect;
    private Vector3f velocity;
    private Emitter bulletEmitter;
    private int fireTimer = 0;
    private double angleOffset = 0f;

    public Ufo(ModelInstance modelInstance, Emitter playerBulletEmitter, Effect explosionEffect) {
        setModelInstance(modelInstance);
        this.playerBulletEmitter = playerBulletEmitter;
        this.explosionEffect = explosionEffect;
    }

    public void setBulletEmitter(Emitter bulletEmitter) {
        this.bulletEmitter = bulletEmitter;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
        this.velocity.normalise();
        this.velocity.scale(SPEED);
    }

    @Override
    public void update() {
        yaw -= 3;
        ++fireTimer;

        Vector3f.add(position, velocity, position);

        if (fireTimer >= FIRE_INTERVAL) {
            fireTimer = 0;
            angleOffset += 5.0;

            for (double angle = 0; angle < 360; angle += 10) {
                Particle particle = bulletEmitter.emitFrom(position, MathUtils.unitVector(angle + angleOffset), BULLET_SPEED, 250);
                particle.setScale(new Tween(1f, 1f, LINEAR));
                particle.setFade(new Tween(1f, 1f, LINEAR));
            }
        }

        for (Particle p : playerBulletEmitter.particles()) {
            Vector3f.sub(p.getPosition(), position, Arena.V);
            if (Arena.V.lengthSquared() < HIT_RADIUS_SQUARED) {
                game.remove(this);
                ((Arena) game).addScore(1);
                explosionEffect.trigger(position);
                p.setLifetime((short) 0);
                break;
            }
        }

        super.update();
    }
}
