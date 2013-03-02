package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the raw data for a 3D mesh
 */
public class Mesh {

    private List<Vertex> vertices;
    private List<Short> indices;

    public Mesh() {
        vertices = new ArrayList<Vertex>();
        indices = new ArrayList<Short>();
    }

    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public void addIndex(int index) {
        indices.add((short)index);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Short> getIndices() {
        return indices;
    }
}
