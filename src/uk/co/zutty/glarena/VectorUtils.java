package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 */
public class VectorUtils {
    public static Vector3f[] zeroArray(int length) {
        Vector3f[] arr = new Vector3f[length];

        for(int i = 0; i < length; i++) {
            arr[i] = new Vector3f(0.0f, 0.0f, 0.0f);
        }

        return arr;
    }

    public static FloatBuffer asFloatBuffer(float... values) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    public static float[] asFloats(Vector3f v) {
        return new float[]{v.x, v.y, v.z};
    }

    public static float[] asFloats(Vector2f v) {
        return new float[]{v.x, 1 - v.y};
    }

    public static void put(FloatBuffer buffer, Vector3f vector3f) {
        buffer.put(vector3f.x);
        buffer.put(vector3f.y);
        buffer.put(vector3f.z);
    }
}
