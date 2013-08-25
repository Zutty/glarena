package uk.co.zutty.glarena;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the raw data for a 3D mesh
 */
public class Mesh {

    private List<Vertex> vertices;
    private List<Short> indices;

    public Mesh() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public void addIndex(int index) {
        indices.add((short) index);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Short> getIndices() {
        return indices;
    }
}
