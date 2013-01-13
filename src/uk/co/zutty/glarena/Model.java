package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 02/06/12
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class Model {

    private final List<Vector3f> vertices;
    private final List<Vector3f> normals;
    private final List<Face> faces;
    private final List<Vector2f> texCoords;

    public Model() {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        faces = new ArrayList<Face>();
        texCoords = new ArrayList<Vector2f>();
    }

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addNormal(Vector3f normal) {
        normals.add(normal);
    }

    public void addFace(Face face) {
        faces.add(face);
    }

    public void addTexCoord(Vector2f texCoord) {
        texCoords.add(texCoord);
    }

    public Vector3f getVertex(int idx) {
        return vertices.get(idx);
    }

    public Vector3f getNormal(int idx) {
        return normals.get(idx);
    }

    public Vector2f getTexCoord(int idx) {
        return texCoords.get(idx);
    }

    public List<Face> getFaces() {
        return faces;
    }
}
