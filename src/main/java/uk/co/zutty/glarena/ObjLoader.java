/*
 * Copyright (c) 2013 George Weller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
    public Mesh loadEntityMesh(String filename) {
        return loadMesh(filename, new EntityVertexMapper());
    }

    public Mesh loadUnlitMesh(String filename) {
        return loadMesh(filename, new UnlitVertexMapper());
    }

    private Mesh loadMesh(String filename, VertexMapper mapper) {
        Mesh m = new Mesh();

        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        Map<String, Integer> inverseIndex = new HashMap<>();

        for (String[] line : parse(filename)) {
            switch (line[0]) {
                case "v":
                    positions.add(parseVector3f(line));
                    break;
                case "vn":
                    normals.add(parseVector3f(line));
                    break;
                case "vt":
                    texCoords.add(parseVector2f(line));
                    break;
                case "f":
                    for (int i = 1; i < line.length; i++) {
                        String vertexTriple = line[i];
                        if (!inverseIndex.containsKey(vertexTriple)) {
                            // Format is pos/tex/norm
                            String[] vtParts = vertexTriple.split("/");
                            int positionIdx = Integer.valueOf(vtParts[0]);
                            int texCoordIdx = Integer.valueOf(vtParts[1]);
                            int normalIdx = Integer.valueOf(vtParts[2]);

                            Vertex v = mapper.makeVertex(positions.get(positionIdx - 1), normals.get(normalIdx - 1), texCoords.get(texCoordIdx - 1));

                            inverseIndex.put(vertexTriple, m.addVertex(v));
                        }

                        m.addIndex(inverseIndex.get(vertexTriple));
                    }
                    break;
            }
        }

        return m;
    }

    private interface VertexMapper {
        Vertex makeVertex(Vector3f position, Vector3f normal, Vector2f texCoord);
    }

    private class EntityVertexMapper implements VertexMapper {
        public Vertex makeVertex(Vector3f position, Vector3f normal, Vector2f texCoord) {
            EntityVertex v = new EntityVertex();
            v.setPosition(position);
            v.setNormal(normal);
            v.setTexCoord(texCoord);
            return v;
        }
    }

    private class UnlitVertexMapper implements VertexMapper {
        public Vertex makeVertex(Vector3f position, Vector3f normal, Vector2f texCoord) {
            UnlitVertex v = new UnlitVertex();
            v.setPosition(position);
            v.setTexCoord(texCoord);
            return v;
        }
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
                super.finalize();
                reader.close();
            }
        };
    }
}
