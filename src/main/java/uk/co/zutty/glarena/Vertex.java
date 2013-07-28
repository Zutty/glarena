package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Vertex definition for interleaved VBOs.
 */
public class Vertex {

    private float positionX;
    private float positionY;
    private float positionZ;

    private float normalX;
    private float normalY;
    private float normalZ;

    private float texCoordS;
    private float texCoordT;

    public void setPosition(Vector3f position) {
        positionX = position.x;
        positionY = position.y;
        positionZ = position.z;
    }

    public void setNormal(Vector3f normal) {
        normalX = normal.x;
        normalY = normal.y;
        normalZ = normal.z;
    }

    public void setTexCoord(Vector2f texCoord) {
        texCoordS = texCoord.x;
        texCoordT = texCoord.y;
    }

    public void put(FloatBuffer vertexBuffer) {
        vertexBuffer.put(positionX);
        vertexBuffer.put(positionY);
        vertexBuffer.put(positionZ);

        vertexBuffer.put(normalX);
        vertexBuffer.put(normalY);
        vertexBuffer.put(normalZ);

        vertexBuffer.put(texCoordS);
        vertexBuffer.put(texCoordT);
    }
}
