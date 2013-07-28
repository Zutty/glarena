package uk.co.zutty.glarena.vertex;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Wrapper for a VAO.
 */
public class VertexArray {

    private int glVao = GL_INVALID_VALUE;

    public VertexArray() {
        glVao = glGenVertexArrays();
    }

    public void createAttributePointers(VertexFormat format) {
        int index = 0;
        for (Attribute attribute : format.getAttributes()) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index++, attribute.getElements(), GL_FLOAT, false, format.getStride(), attribute.getOffset());
        }
    }

    public void bind() {
        glBindVertexArray(glVao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(glVao);
    }
}
