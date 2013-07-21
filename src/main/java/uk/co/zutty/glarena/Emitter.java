package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.vertex.VertexBuffer;
import uk.co.zutty.glarena.vertex.VertexFormat;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;

/**
 * A container for particles that controls their position and other properties.
 */
public abstract class Emitter {

    protected ShaderProgram shader;
    protected VertexBuffer buffer;
    protected int glTexture = -1;
    protected List<Particle> particles = new ArrayList<Particle>();

    public void init(VertexFormat format) {
        buffer = new VertexBuffer(format);
        buffer.createBuffer();
    }

    public void emitFrom(Vector3f source, Vector3f direction, float speed) {
        Particle particle = new Particle();
        particle.setLifetime((short) 100);
        particle.setPosition(new Vector3f(source));
        particle.setVelocity(new Vector3f(direction));
        particle.getVelocity().normalise().scale(speed);
        particles.add(particle);
    }

    public Iterable<Particle> particles() {
        return particles;
    }

    public void update() {
        for(Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
            Particle p = iter.next();
            p.update();
            if(p.isDead()) {
                iter.remove();
            }
        }

        FloatBuffer positions = BufferUtils.createFloatBuffer(particles.size() * Particle.ELEMENTS);

        for(Particle p: particles()) {
            p.put(positions);
        }

        positions.flip();

        buffer.subdata(positions, particles.size() * Particle.STRIDE);
    }
}
