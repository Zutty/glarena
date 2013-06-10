package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class Model {

    private int glVao;
    private int glVertexVbo;
    private int glIndexVbo;
    private int numIndices;
    private int glTexture;

    public Model(int texture) {
        glTexture = texture;
        numIndices = 0;
    }

    public static Model fromMesh(Mesh mesh, int texture) {
        Model model = new Model(texture);
        model.numIndices = mesh.getIndices().size();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(mesh.getVertices().size() * Vertex.STRIDE);
        ShortBuffer indexBuffer = BufferUtils.createShortBuffer(model.numIndices);

        for (Vertex vertex : mesh.getVertices()) {
            vertex.put(vertexBuffer);
        }

        for (short index : mesh.getIndices()) {
            indexBuffer.put(index);
        }

        vertexBuffer.flip();
        indexBuffer.flip();

        model.glVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(model.glVao);

        model.glVertexVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, model.glVertexVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STREAM_DRAW);

        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_OFFSET);
        GL20.glVertexAttribPointer(1, Vertex.NORMAL_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_OFFSET);
        GL20.glVertexAttribPointer(2, Vertex.TEXCOORD_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXCOORD_OFFSET);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        model.glIndexVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.glIndexVbo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return model;
    }

    public void render() {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture);
        //glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        //glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

        //glActiveTexture(GL_TEXTURE1);
        //glEnable(GL_TEXTURE_2D);
        //glBindTexture(GL_TEXTURE_2D, normalMap);
        //glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        //glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

// Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(glVao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

// Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, glIndexVbo);

// Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, numIndices, GL11.GL_UNSIGNED_SHORT, 0);

// Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);

        ShaderProgram.useNone();
    }

    public void destroy() {
        // Delete texture
        GL11.glDeleteTextures(glTexture);

        // Delete the mesh
        GL30.glBindVertexArray(glVao);

        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(glVertexVbo);

        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(glIndexVbo);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(glVao);
    }
}