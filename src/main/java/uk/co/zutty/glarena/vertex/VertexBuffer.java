package uk.co.zutty.glarena.vertex;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL15.*;

/**
 * Wrapper for a VBO.
 */
public class VertexBuffer {

    private int glVbo = GL_INVALID_VALUE;

    public VertexBuffer() {
        glVbo = glGenBuffers();
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        if (glVbo != GL_INVALID_VALUE) {
            glDeleteBuffers(glVbo);
        }
    }

    public void subdata(FloatBuffer data, long dataSize) {
        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
        glBufferData(GL_ARRAY_BUFFER, dataSize, GL_STREAM_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
