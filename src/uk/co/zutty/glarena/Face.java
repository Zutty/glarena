package uk.co.zutty.glarena;

/**
 */
public class Face {

    private final Vector3i vertices;
    private final Vector3i texCoords;
    private final Vector3i normals;

    public Face(Vector3i normals, Vector3i texCoords, Vector3i vertices) {
        this.normals = normals;
        this.texCoords = texCoords;
        this.vertices = vertices;
    }

    public Vector3i getVertices() {
        return vertices;
    }

    public Vector3i getTexCoords() {
        return texCoords;
    }

    public Vector3i getNormals() {
        return normals;
    }
}
