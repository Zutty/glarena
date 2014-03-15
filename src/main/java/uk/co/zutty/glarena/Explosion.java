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

package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.*;
import uk.co.zutty.glarena.shaders.*;

import static java.util.Arrays.asList;
import static uk.co.zutty.glarena.engine.Tween.Easing.LINEAR;
import static uk.co.zutty.glarena.engine.Tween.Easing.QUAD_INOUT;
import static uk.co.zutty.glarena.util.MathUtils.*;

public class Explosion implements Effect {

    private Emitter fireballEmitter;
    private Emitter flashEmitter;
    private Emitter sparkEmitter;
    private Emitter wakeEmitter;

    public Explosion(BillboardTechnique billboardTechnique, BulletTechnique bulletTechnique, PlanarTechnique planarTechnique) {
        fireballEmitter = new Emitter(billboardTechnique, TextureLoader.loadTexture("/textures/explosion.png"), BillboardParticle.class);
        flashEmitter = new Emitter(billboardTechnique, TextureLoader.loadTexture("/textures/flash.png"), BillboardParticle.class);
        sparkEmitter = new Emitter(bulletTechnique, TextureLoader.loadTexture("/textures/spark.png"), BulletParticle.class);
        wakeEmitter = new Emitter(planarTechnique, TextureLoader.loadTexture("/textures/wake.png"), BillboardParticle.class);
    }

    @Override
    public Iterable<Emitter> getEmitters() {
        return asList(fireballEmitter, flashEmitter, sparkEmitter, wakeEmitter);
    }

    @Override
    public void trigger(Vector3f position) {
        BillboardParticle flashParticle = (BillboardParticle) flashEmitter.emitFrom(position, UP, 0f, 10);
        flashParticle.setFade(new Tween(0f, 1f, QUAD_INOUT));
        flashParticle.setScale(new Tween(0f, 5f, QUAD_INOUT));

        BillboardParticle wakeParticle = (BillboardParticle) wakeEmitter.emitFrom(position, UP, 0f, randRange(15, 25));
        wakeParticle.setRotation(randAngle());
        wakeParticle.setFade(new Tween(1f, 0f, LINEAR));
        wakeParticle.setScale(new Tween(0f, randRange(15f, 20f), LINEAR));

        int numSparks = randRange(5, 10);
        for (int i = 0; i < numSparks; i++) {
            Particle spark = sparkEmitter.emitFrom(position, randomDirection(), randRange(.6f, .8f), randRange(5, 20));
            spark.setScale(new Tween(1f, 1f, LINEAR));
            spark.setFade(new Tween(1f, 0f, LINEAR));
        }

        int numFireballs = randRange(10, 20);
        for (int i = 0; i < numFireballs; i++) {
            BillboardParticle billboard = (BillboardParticle) fireballEmitter.emitFrom(position, randomDirection(), randRange(0.05f, 0.2f), randRange(15, 25));
            billboard.setRotation(randAngle());
            billboard.setRotationSpeed(randRange(0.01f, 0.02f));
            billboard.setScale(new Tween(randRange(.8f, 1.2f), randRange(3f, 6f), LINEAR));
            billboard.setFade(new Tween(1f, 0f, LINEAR));
        }
    }
}
