package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * Created with IntelliJ IDEA.
 */
public class BillboardList {

    private static final int NUM_ROWS = 10;
    private static final int NUM_COLUMNS = 10;

    private ShaderProgram shader;
    private int glTexture;
    private int glVao;
    private int glVbo;

    public BillboardList() {
        glTexture = -1;
        glVbo = GL_INVALID_VALUE;
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

        //shader.initUniform("gColorMap");
        shader.initUniform("gVP");
        shader.initUniform("gCameraPos");

        createPositionBuffer();
    }


    public void createPositionBuffer() {
        FloatBuffer positions = BufferUtils.createFloatBuffer(NUM_ROWS * NUM_COLUMNS * 3);

        for ( int j = 0 ; j < NUM_ROWS ; j++) {
            for ( int i = 0 ; i < NUM_COLUMNS ; i++) {
                    //positions[j * NUM_COLUMNS + i] = new Vector3f((float)i, 0.0f, (float)j);
                positions.put(i);
                positions.put(0f);
                positions.put(j);
            }
        }

        positions.flip();

        glVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(glVao);

        glVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);   // position

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render(Matrix4f viewProjectionMatrix, Vector3f cameraPos) {
        shader.use();

        //m_technique.SetVP(VP);
        //m_technique.SetCameraPosition(CameraPos);
        shader.setUniform("gVP", viewProjectionMatrix);
        shader.setUniform("gCameraPos", cameraPos);

        //m_pTexture->Bind(COLOR_TEXTURE_UNIT);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, glTexture);

        GL30.glBindVertexArray(glVao);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
        //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);   // position

        glDrawArrays(GL_POINTS, 0, NUM_ROWS * NUM_COLUMNS);

        glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        ShaderProgram.useNone();
    }
}