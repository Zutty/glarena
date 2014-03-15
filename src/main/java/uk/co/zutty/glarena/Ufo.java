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

/**
 * AI controlled enemy entity.
 */
public class Ufo extends AbstractEntity {

    private static final float HIT_RADIUS_SQUARED = 1.8f * 1.8f;
    protected Game game;

    private Emitter playerBulletEmitter;
    private Effect explosionEffect;
    private int timer = 0;

    public Ufo(ModelInstance modelInstance, Emitter playerBulletEmitter, Effect explosionEffect) {
        setModelInstance(modelInstance);
        this.playerBulletEmitter = playerBulletEmitter;
        this.explosionEffect = explosionEffect;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void update() {
        yaw -= 3;

        ++timer;

        position.x += Math.sin((double) timer / (50.0 - ((double) timer / 20.0))) * 0.5f;
        position.z += Math.cos((double) timer / (50.0 - ((double) timer / 20.0))) * 0.5f;

        for (Particle p : playerBulletEmitter.particles()) {
            Vector3f.sub(p.getPosition(), position, Arena.V);
            if (Arena.V.lengthSquared() < HIT_RADIUS_SQUARED) {
                game.remove(this);
                explosionEffect.trigger(position);
                p.setLifetime((short) 0);
            }
        }

        super.update();
    }
}
