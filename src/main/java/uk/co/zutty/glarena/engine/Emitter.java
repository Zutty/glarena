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

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.gl.ArrayModel;
import uk.co.zutty.glarena.gl.Texture;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static uk.co.zutty.glarena.gl.enums.BufferUsage.STREAM;

/**
 * A container for particles that controls their position and other properties.
 */
public class Emitter implements Entity {

    private Technique technique;
    private ArrayModel model;
    private ModelInstance modelInstance;
    private Class<? extends Particle> particleType;
    private List<Particle> particles = new ArrayList<>();

    public Emitter(Technique technique, Texture texture, Class<? extends Particle> particleType) {
        this.technique = technique;
        this.particleType = particleType;
        model = new ArrayModel(technique, STREAM);
        modelInstance = new ModelInstance(model, texture);
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public Particle emitFrom(Vector3f source, Vector3f direction, float speed, int lifetime) {
        Particle particle;

        try {
            particle = particleType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GameException(e);
        }

        particle.setLifetime((short) lifetime);
        particle.setPosition(new Vector3f(source));
        particle.setVelocity(new Vector3f(direction));
        particle.getVelocity().normalise().scale(speed);
        particles.add(particle);

        return particle;
    }

    public Iterable<Particle> particles() {
        return particles;
    }

    @Override
    public void update() {
        for (Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
            Particle p = iter.next();
            p.update();
            if (p.isDead()) {
                iter.remove();
            }
        }

        FloatBuffer positions = BufferUtils.createFloatBuffer(particles.size() * technique.getFormat().getElements());

        for (Particle p : particles()) {
            p.put(positions);
        }

        positions.flip();

        model.getVertexBuffer().setData(positions, particles.size());
    }
}
