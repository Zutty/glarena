package uk.co.zutty.glarena.gl;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import uk.co.zutty.glarena.Mesh;
import uk.co.zutty.glarena.ModelInstance;
import uk.co.zutty.glarena.Technique;
import uk.co.zutty.glarena.Vertex;
import uk.co.zutty.glarena.vertex.VertexArray;
import uk.co.zutty.glarena.vertex.VertexBuffer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

/**
 * Represents a model stored on the GPU that can be rendered.
 */
public class Model {

    private Technique technique;
    private VertexArray vertexArray;
    private VertexBuffer vertexBuffer;
    private VertexBuffer indexBuffer;
    private int numIndices;
    private int glTexture;

    public Model(Technique technique, Mesh mesh, int texture) {
        this.technique = technique;
        glTexture = texture;

        numIndices = mesh.getIndices().size();

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(mesh.getVertices().size() * technique.getFormat().getStride());
        ShortBuffer indexData = BufferUtils.createShortBuffer(numIndices);

        for (Vertex vertex : mesh.getVertices()) {
            vertex.put(vertexData);
        }

        for (short index : mesh.getIndices()) {
            indexData.put(index);
        }

        vertexData.flip();
        indexData.flip();

        vertexArray = new VertexArray();
        vertexArray.bind();

        vertexBuffer = new VertexBuffer();
        vertexBuffer.bind();

        vertexBuffer.setData(vertexData);

        vertexArray.createAttributePointers(technique.getFormat());

        indexBuffer = new VertexBuffer(GL_ELEMENT_ARRAY_BUFFER);
        indexBuffer.bind();
        indexBuffer.setData(indexData);

        vertexArray.unbind();
    }

    public Technique getTechnique() {
        return technique;
    }

    public ModelInstance newInstance() {
        ModelInstance instance = new ModelInstance();
        instance.setModel(this);
        return instance;
    }

    public void render() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture);

        vertexArray.bind();

        GL11.glDrawElements(GL11.GL_TRIANGLES, numIndices, GL11.GL_UNSIGNED_SHORT, 0);

        vertexArray.unbind();
    }

    public void destroy() {
        // Delete texture
        GL11.glDeleteTextures(glTexture);

        // Delete the mesh
        vertexArray.destroy();
        vertexBuffer.destroy();
        indexBuffer.destroy();
    }
}
