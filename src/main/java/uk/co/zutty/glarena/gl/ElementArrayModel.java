package uk.co.zutty.glarena.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import uk.co.zutty.glarena.Technique;
import uk.co.zutty.glarena.vertex.Attribute;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class ElementArrayModel extends ArrayModel {

    private int glIndexVbo = GL_INVALID_VALUE;
    private int numIndices;

    public ElementArrayModel(Technique technique, Texture texture) {
        super(texture, technique);

        glBindVertexArray(glVao);

        glIndexVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);

        glBindVertexArray(0);
    }

    public void setIndexData(ShortBuffer indexData) {
        numIndices = indexData.capacity();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw(int mode) {
        texture.bind();

        glBindVertexArray(glVao);

        glDrawElements(mode, numIndices, GL_UNSIGNED_SHORT, 0);

        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        GL15.glDeleteBuffers(glIndexVbo);
    }
}
