package uk.co.zutty.glarena.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static uk.co.zutty.glarena.util.MathUtils.*;

/**
 * Utility methods to provide stock matrices.
 */
public final class MatrixUtils {

    private static final Vector3f forward = new Vector3f();
    private static final Vector3f side = new Vector3f();
    private static final Vector3f up = new Vector3f();
    private static final Vector3f eye = new Vector3f();

    public static Matrix4f frustum(float width, float height, float fieldOfView, float near, float far) {
        // Setup projection matrix
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = width / height;

        float yScale = cot(degreesToRadians(fieldOfView / 2f));
        float xScale = yScale / aspectRatio;
        float frustumLength = far - near;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((far + near) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near * far) / frustumLength);

        return projectionMatrix;
    }

    public static Matrix4f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        forward.set(centerX - eyeX, centerY - eyeY, centerZ - eyeZ);
        //forward.set(eyeX - centerX, eyeY - centerY, eyeZ - centerZ);
        forward.normalise();

        up.set(upX, upY, upZ);

		/* Side = forward x up */
        Vector3f.cross(forward, up, side);
        side.normalise();

		/* Recompute up as: up = side x forward */
        Vector3f.cross(side, forward, up);
        up.normalise();

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = side.x;
        matrix.m01 = side.y;
        matrix.m02 = side.z;
        //matrix.m03 = 0.0f;

        matrix.m10 = up.x;
        matrix.m11 = up.y;
        matrix.m12 = up.z;
        //matrix.m13 = 0.0f;

        matrix.m20 = -forward.x;
        matrix.m21 = -forward.y;
        matrix.m22 = -forward.z;
        //matrix.m33 = 0.0f;

        /*matrix.m30 = 0.0f;
        matrix.m31 = 0.0f;
        matrix.m32 = 0.0f;
        matrix.m33 = 1.0f;*/

        matrix.transpose();

        eye.set(-eyeX, -eyeY, -eyeZ);
        matrix.translate(eye);
        /*
        Matrix4f aux = new Matrix4f();
        //setup aux as a translation matrix by placing positions in the last column
        aux.m30 = -eyeX;
        aux.m31 = -eyeY;
        aux.m32 = -eyeZ;

//multiplication(in fact translation) viewMatrix with aux
        Matrix4f.mul(matrix, aux, matrix);
          */
        return matrix;
    }
}
