package uk.co.zutty.glarena.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import uk.co.zutty.glarena.Technique;
import uk.co.zutty.glarena.vertex.Attribute;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class ElementArrayModel implements Model {

    private Technique technique;
    private int glVao = GL_INVALID_VALUE;
    private int glArrayVbo = GL_INVALID_VALUE;
    private int glIndexVbo = GL_INVALID_VALUE;
    private int numIndices;
    private int glTexture;

    public ElementArrayModel(Technique technique, int texture) {
        this.technique = technique;
        glTexture = texture;

        glVao = glGenVertexArrays();
        glBindVertexArray(glVao);

        glArrayVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glArrayVbo);

        int index = 0;
        for (Attribute attribute : technique.getFormat().getAttributes()) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index++, attribute.getElements(), GL_FLOAT, false, technique.getFormat().getStride(), attribute.getOffset());
        }

        glIndexVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);

        glBindVertexArray(0);
    }

    @Override
    public void setVertexData(FloatBuffer vertexData) {
        glBindBuffer(GL_ARRAY_BUFFER, glArrayVbo);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void setIndexData(ShortBuffer indexData) {
        numIndices = indexData.capacity();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public Technique getTechnique() {
        return technique;
    }

    @Override
    public void render() {
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL11.GL_TEXTURE_2D, glTexture);

        glBindVertexArray(glVao);

        glDrawElements(GL11.GL_TRIANGLES, numIndices, GL11.GL_UNSIGNED_SHORT, 0);

        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        // Delete texture
        GL11.glDeleteTextures(glTexture);

        // Delete the arrays and buffers
        glDeleteVertexArrays(glVao);
        glDeleteBuffers(glArrayVbo);
        glDeleteBuffers(glIndexVbo);
    }
}
