package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Loader for wavefront OBJ model data.
 */
public class ObjLoader {
    public Mesh loadMesh(String filename) {
        Mesh m = new Mesh();

        List<Vector3f> positions = new ArrayList<Vector3f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Vector2f> texCoords = new ArrayList<Vector2f>();
        Map<String, Integer> inverseIndex = new HashMap<String, Integer>();

        for (String[] line : parse(filename)) {
            if (line[0].equals("v")) {
                positions.add(parseVector3f(line));
            } else if (line[0].equals("vn")) {
                normals.add(parseVector3f(line));
            } else if (line[0].equals("vt")) {
                texCoords.add(parseVector2f(line));
            } else if (line[0].equals("f")) {
                for (int i = 1; i < line.length; i++) {
                    String vertexTriple = line[i];
                    if (!inverseIndex.containsKey(vertexTriple)) {
                        // Format is pos/tex/norm
                        String[] vtParts = vertexTriple.split("/");
                        int positionIdx = Integer.valueOf(vtParts[0]);
                        int texCoordIdx = Integer.valueOf(vtParts[1]);
                        int normalIdx = Integer.valueOf(vtParts[2]);

                        Vertex v = new Vertex();
                        v.setPosition(positions.get(positionIdx - 1));
                        v.setTexCoord(texCoords.get(texCoordIdx - 1));
                        v.setNormal(normals.get(normalIdx - 1));

                        inverseIndex.put(vertexTriple, m.addVertex(v));
                    }

                    m.addIndex(inverseIndex.get(vertexTriple));
                }
            }
        }

        return m;
    }

    private Vector3f parseVector3f(String[] line) {
        return new Vector3f(Float.valueOf(line[1]), Float.valueOf(line[2]), Float.valueOf(line[3]));
    }

    private Vector2f parseVector2f(String[] line) {
        return new Vector2f(Float.valueOf(line[1]), 1 - Float.valueOf(line[2]));
    }

    private Iterable<String[]> parse(String filename) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));

        return new Iterable<String[]>() {
            public Iterator<String[]> iterator() {
                return new Iterator<String[]>() {
                    private String nextLine;

                    public boolean hasNext() {
                        try {
                            return (nextLine = reader.readLine()) != null;
                        } catch (IOException e) {
                            return false;
                        }
                    }

                    public String[] next() {
                        return nextLine.trim().split("\\s+");
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            protected void finalize() throws Throwable {
                reader.close();
            }
        };
    }
}
