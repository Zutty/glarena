package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.vertex.VertexArray;
import uk.co.zutty.glarena.vertex.VertexBuffer;
import uk.co.zutty.glarena.vertex.VertexFormat;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * A container for particles that controls their position and other properties.
 */
public abstract class Emitter {

    protected ShaderProgram shader;
    protected VertexArray array;
    protected VertexBuffer buffer;
    private VertexFormat format;
    protected int glTexture = -1;
    protected List<Particle> particles = new ArrayList<>();

    public void init(VertexFormat format) {
        this.format = format;
        array = new VertexArray();
        array.bind();
        buffer = new VertexBuffer();
        buffer.bind();
        array.createAttributePointers(format);
        array.unbind();
    }

    protected abstract Particle newParticle();

    public void emitFrom(Vector3f source, Vector3f direction, float speed) {
        Particle particle = newParticle();
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
        for (Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
            Particle p = iter.next();
            p.update();
            if (p.isDead()) {
                iter.remove();
            }
        }

        FloatBuffer positions = BufferUtils.createFloatBuffer(particles.size() * format.getElements());

        for (Particle p : particles()) {
            p.put(positions);
        }

        positions.flip();

        buffer.subdata(positions, particles.size() * format.getStride());
    }

    protected void initUniforms(ShaderProgram shader) {}

    public void render(Matrix4f viewProjectionMatrix) {
        glDisable(GL_CULL_FACE);

        shader.use();
        shader.setUniform("gVP", viewProjectionMatrix);
        initUniforms(shader);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, glTexture);

        array.bind();

        glDrawArrays(GL_POINTS, 0, particles.size());

        array.unbind();

        ShaderProgram.useNone();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

}
