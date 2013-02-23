package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static uk.co.zutty.glarena.VectorUtils.asFloats;

/**
 * Created with IntelliJ IDEA.
 */
public class Model {

    private int vaoId;
    private int iboId;

    private int numIndeces;
    private int texture;

    public Model(int texture) {
        this.texture = texture;

        numIndeces = 0;
    }

    public static Model fromMesh(Mesh mesh, int texture) {
        Model model = new Model(texture);
        model.numIndeces = mesh.getIndeces().size();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(mesh.getVertices().size() * Vertex.STRIDE);
        ShortBuffer indexBuffer = BufferUtils.createShortBuffer(model.numIndeces);

        for (Vertex vertex: mesh.getVertices()) {
            vertex.put(vertexBuffer);
        }

         for(short index: mesh.getIndeces()) {
            indexBuffer.put(index);
        }

        vertexBuffer.flip();
        indexBuffer.flip();

        model.vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(model.vaoId);

        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STREAM_DRAW);

        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_OFFSET);
        GL20.glVertexAttribPointer(1, Vertex.NORMAL_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_OFFSET);
        GL20.glVertexAttribPointer(2, Vertex.TEXCOORD_ELEMENTS, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXCOORD_OFFSET);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        model.iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return model;
    }

    public void render() {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        //glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        //glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

        //glActiveTexture(GL_TEXTURE1);
        //glEnable(GL_TEXTURE_2D);
        //glBindTexture(GL_TEXTURE_2D, normalMap);
        //glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        //glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

// Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

// Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);

// Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, numIndeces, GL11.GL_UNSIGNED_SHORT, 0);

// Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /*@Override
    protected void finalize() throws Throwable {
        super.finalize();
        //glDeleteBuffers(vboVertexHandle);
        //glDeleteBuffers(vboTexCoordsHandle);
        //glDeleteBuffers(vboNormalHandle);
        //glDeleteBuffers(vboTangentHandle);
    } */
}
