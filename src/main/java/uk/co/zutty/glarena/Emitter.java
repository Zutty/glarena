package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.gl.ArrayModel;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.vertex.VertexFormat;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A container for particles that controls their position and other properties.
 */
public abstract class Emitter {

    protected ShaderProgram shader;
    private ArrayModel model;
    private VertexFormat format;
    private Class<? extends Particle> particleType;
    protected int glTexture = -1;
    protected List<Particle> particles = new ArrayList<>();

    protected Emitter(ShaderProgram shader, VertexFormat format, int glTexture, Class<? extends Particle> particleType) {
        this.shader = shader;
        this.format = format;
        this.glTexture = glTexture;
        this.particleType = particleType;

        final VertexFormat fmt = format;
        model = new ArrayModel(glTexture, new Technique() {
            @Override
            public VertexFormat getFormat() {
                return fmt;
            }

            @Override
            public void setProjectionMatrix(Matrix4f projectionMatrix) {}

            @Override
            public void setCamera(Camera camera) {}

            @Override
            public void renderInstance(ModelInstance instance) {}
        });
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

        model.updateVertexData(positions, particles.size());
    }

    protected void initUniforms(ShaderProgram shader) {}

    public void render(Matrix4f viewProjectionMatrix) {
        glDisable(GL_CULL_FACE);

        shader.use();
        shader.setUniform("gVP", viewProjectionMatrix);
        initUniforms(shader);

        model.draw(GL_POINTS);

        ShaderProgram.useNone();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

}
