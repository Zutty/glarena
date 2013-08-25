package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.vertex.VertexFormat;

/**
 * Encapsulates a vertex format and a shader.
 */
public interface Technique {

    VertexFormat getFormat();

    void setProjectionMatrix(Matrix4f projectionMatrix);

    void setCamera(Camera camera);

    void renderInstance(ModelInstance instance);
}
