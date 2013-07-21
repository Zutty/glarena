package uk.co.zutty.glarena.vertex;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import uk.co.zutty.glarena.Particle;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.*;

/**
 * Wrapper for  VAO and a single VBO.
 */
public class VertexBuffer {

    private int glVao = GL_INVALID_VALUE;
    private int glVbo = GL_INVALID_VALUE;
    private VertexFormat format;

    public VertexBuffer(VertexFormat format) {
        this.format = format;
    }

    public VertexFormat getFormat() {
        return format;
    }

    public void createBuffer() {
        glVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(glVao);

        glVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glVbo);

        int index = 0;
        for(Attribute attribute : format.getAttributes()) {
            glVertexAttribPointer(index++, attribute.getElements(), GL_FLOAT, false, format.getStride(), attribute.getOffset());
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void bind() {
        GL30.glBindVertexArray(glVao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, glVbo);
    }

    public void unbind() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
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
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
