package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.gl.ArrayModel;
import uk.co.zutty.glarena.gl.Texture;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A container for particles that controls their position and other properties.
 */
public class Emitter implements Entity {

    private Technique technique;
    private ArrayModel model;
    private ModelInstance modelInstance;
    private Class<? extends Particle> particleType;
    private List<Particle> particles = new ArrayList<>();

    protected Emitter(Technique technique, Texture texture, Class<? extends Particle> particleType) {
        this.technique = technique;
        this.particleType = particleType;
        model = new ArrayModel(texture, technique);
        modelInstance = new ModelInstance(model);
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void emitFrom(Vector3f source, Vector3f direction, float speed) {
        Particle particle;

        try {
            particle = particleType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GameException(e);
        }

        particle.setLifetime((short) 100);
        particle.setPosition(new Vector3f(source));
        particle.setVelocity(new Vector3f(direction));
        particle.getVelocity().normalise().scale(speed);
        particles.add(particle);
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

        model.updateVertexData(positions, particles.size());
    }
}
