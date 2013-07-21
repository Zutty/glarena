package uk.co.zutty.glarena;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * A collection of billboarded particles.
 */
public class BillboardList extends Emitter {

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

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "in_velocity");

        shader.link();
        shader.validate();

        shader.initUniform("gVP");

        init(VertexFormat.Builder.format()
                .withAttribute(3)
                .withAttribute(3)
                .build());
    }

    public void render(Matrix4f viewProjectionMatrix) {
        glDisable(GL_CULL_FACE);

        shader.use();
        shader.setUniform("gVP", viewProjectionMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, glTexture);

        buffer.bind();

        glDrawArrays(GL_POINTS, 0, particles.size());

        buffer.unbind();

        ShaderProgram.useNone();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
}