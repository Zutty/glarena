package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Vertex definition
 */
public class Vertex {

    private static final int FLOAT_BYTES = 4;
    public static final int POSITION_ELEMENTS = 3;
    public static final int NORMAL_ELEMENTS = 3;
    public static final int TEXCOORD_ELEMENTS = 2;
    public static final int POSITION_BYTES = POSITION_ELEMENTS * FLOAT_BYTES;
    public static final int NORMAL_BYTES = NORMAL_ELEMENTS * FLOAT_BYTES;
    public static final int TEXCOORD_BYTES = TEXCOORD_ELEMENTS * FLOAT_BYTES;

    public static final int POSITION_OFFSET = 0;
    public static final int NORMAL_OFFSET = POSITION_OFFSET + POSITION_BYTES;
    public static final int TEXCOORD_OFFSET = NORMAL_OFFSET + NORMAL_BYTES;

    public static final int STRIDE = POSITION_BYTES + NORMAL_BYTES + TEXCOORD_BYTES;

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
