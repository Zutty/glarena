package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: Zutty
 * Date: 20/02/2013
 * Time: 19:38
 * To change this template use File | Settings | File Templates.
 */
public class Cube {

    private Vertex vertex(float x, float y, float z, float r, float g, float b, float s, float t) {
        Vertex v = new Vertex();
        v.setPosition(new Vector3f(x, y, z));
        v.setNormal(new Vector3f(r, g, b));
        v.setTexCoord(new Vector2f(s, t));
        return v;
    }
            /*
        // We'll define our quad using 4 vertices of the custom 'TexturedVertex' class
        vertices = new ArrayList<Vertex>();
        // front
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 0, 0));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 0, 1));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 1, 0));

        //right
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 0, 0));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 1.0f, 0, 0, 0, 1));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 1.0f, 0, 0, 1, 0));

        //back
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 0, 0));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 1, 1));
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 1, 0));

        // left
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, -1.0f, 0, 0, 0, 0));
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, -1.0f, 0, 0, 1, 1));
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1, 0));

        //top
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 0));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 1));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 0));

        //bottom
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 0));
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 1));
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 0));

        // Put each 'Vertex' in one FloatBuffer
        verticesByteBuffer = BufferUtils.createByteBuffer(vertices.size() *
                Vertex.STRIDE);
        FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
        for (Vertex vertex: vertices) {
            // Add position, color and texture floats to the buffer
            vertex.put(verticesFloatBuffer);
        }
        verticesFloatBuffer.flip();


        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = {
                0, 1, 2,
                2, 3, 0,

                4, 5, 6,
                6, 7, 4,

                8, 9, 10,
                10, 11, 8,

                12, 13, 14,
                14, 15, 12,

                16, 17, 18,
                18, 19, 16,

                20, 21, 22,
                22, 23, 20
        };
        */

            /*
        Model m = new Model();

        // front
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 0, 0));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 0, 1));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 1, 0));

        //right
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 0, 0));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 1.0f, 0, 0, 0, 1));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 1.0f, 0, 0, 1, 0));

        //back
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 0, 0));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 1, 1));
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 1, 0));

        // left
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, -1.0f, 0, 0, 0, 0));
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, -1.0f, 0, 0, 1, 1));
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1, 0));

        //top
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 0));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 1));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 0));

        //bottom
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 0));
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 1));
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 0));

        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = {
                0, 1, 2,
                2, 3, 0,

                4, 5, 6,
                6, 7, 4,

                8, 9, 10,
                10, 11, 8,

                12, 13, 14,
                14, 15, 12,

                16, 17, 18,
                18, 19, 16,

                20, 21, 22,
                22, 23, 20
        };
        for(int i: indices) m.addIndex((byte)i);
        */

}
