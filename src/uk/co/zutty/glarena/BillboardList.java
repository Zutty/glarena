package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * A collection of billboarded particles.
 */
public class BillboardList {

    private ShaderProgram shader;
    private int glTexture;
    private int glVao;
    private int glVbo;
    private List<Particle> particles;

    public BillboardList() {
        glTexture = -1;
        glVbo = GL_INVALID_VALUE;
        particles = new ArrayList<Particle>();
    }

    public void destroy() {
        if (glVbo != GL_INVALID_VALUE) {
            glDeleteBuffers(glVbo);
        }
    }

    public void init(String texFilename) {
        glTexture = TextureLoader.loadTexture(texFilename);

        shader = new ShaderProgram();

        Shader vertexShader = new Shader(Shader.Type.VERTEX);
        vertexShader.loadSource("/shaders/billboard/vertex.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader geometryShader = new Shader(Shader.Type.GEOMETRY);
        geometryShader.loadSource("/shaders/billboard/geometry.glsl");
        geometryShader.compile();
        shader.attachShader(geometryShader);

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT);
        fragmentShader.loadSource("/shaders/billboard/fragment.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.link();
        shader.validate();

        shader.initUniform("gVP");
        shader.initUniform("gCameraPos");

        createPositionBuffer();
    }

    public void createPositionBuffer() {
        glVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(glVao);

        glVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);   // position

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

    }

    public void emitFrom(Vector3f source, Vector3f direction, float speed) {
        Particle particle = new Particle();
        particle.setLifetime((short) 100);
        particle.setPosition(new Vector3f(source));
        particle.setVelocity(new Vector3f(direction));
        particle.getVelocity().normalise().scale(speed);
        particles.add(particle);
    }

    public void update() {
        for(Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
            Particle p = iter.next();
            p.update();
            if(p.isDead()) {
                iter.remove();
            }
        }

        FloatBuffer positions = BufferUtils.createFloatBuffer(particles.size() * 3);

        for(Particle p: particles) {
            p.put(positions);
        }

        positions.flip();

        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
        glBufferData(GL_ARRAY_BUFFER, particles.size() * 3 * 4, GL_STREAM_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, positions);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);


    }

    public void render(Matrix4f viewProjectionMatrix, Vector3f cameraPos) {
        shader.use();

        shader.setUniform("gVP", viewProjectionMatrix);
        shader.setUniform("gCameraPos", cameraPos);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, glTexture);

        GL30.glBindVertexArray(glVao);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, glVbo);

        glDrawArrays(GL_POINTS, 0, particles.size());

        glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        ShaderProgram.useNone();
    }
}