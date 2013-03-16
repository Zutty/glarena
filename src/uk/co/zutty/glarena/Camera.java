package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.util.MatrixUtils;

/**
 * Class to encapsulate the view matrix.
 */
public class Camera {

    private Vector3f position;
    private Vector3f center;
    private Matrix4f matrix;

    public Camera() {
        position = new Vector3f();
        center = new Vector3f();
        matrix = new Matrix4f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setCenter(float x, float y, float z) {
        center.x = x;
        center.y = y;
        center.z = z;
    }

    public void update() {
        matrix = MatrixUtils.lookAt(position.x, position.y, position.z,
                    center.x, center.y, center.z,
                    0f, 1f, 0f);
    }

    public Matrix4f getViewMatrix() {
        return matrix;
    }
}
