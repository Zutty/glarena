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

    private List<Vertex> vertices;
    private List<Short> indeces;

    public Model() {
        vertices = new ArrayList<Vertex>();
        indeces = new ArrayList<Short>();
    }

    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public void addIndex(int index) {
        indeces.add((short)index);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Short> getIndeces() {
        return indeces;
    }
}
