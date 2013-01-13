package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 */
public class ObjLoader {
    public Model loadModel(String filename) {
        Model m = new Model();

        for(String[] line: parse(filename)) {
            if(line[0].equals("v")) {
                m.addVertex(parseVector3f(line));
            } else if(line[0].equals("vn")) {
                m.addNormal(parseVector3f(line));
            } else if(line[0].equals("f")) {
                int a = Integer.valueOf(line[1].split("/")[0]);
                int b = Integer.valueOf(line[2].split("/")[0]);
                int c = Integer.valueOf(line[3].split("/")[0]);
                Vector3i verts = new Vector3i(a, b, c);

                int d = Integer.valueOf(line[1].split("/")[1]);
                int e = Integer.valueOf(line[2].split("/")[1]);
                int f = Integer.valueOf(line[3].split("/")[1]);
                Vector3i texCoords = new Vector3i(d, e, f);

                int g = Integer.valueOf(line[1].split("/")[2]);
                int h = Integer.valueOf(line[2].split("/")[2]);
                int i = Integer.valueOf(line[3].split("/")[2]);
                Vector3i norms = new Vector3i(g, h, i);

                m.addFace(new Face(norms, texCoords, verts));
            } else if(line[0].equals("vt")) {
                m.addTexCoord(new Vector2f(Float.valueOf(line[1]), Float.valueOf(line[2])));
            }
        }

        return m;
    }

    private Vector3f parseVector3f(String[] line) {
        return new Vector3f(Float.valueOf(line[1]), Float.valueOf(line[2]), Float.valueOf(line[3]));
    }

    private Iterable<String[]> parse(String filename) {
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        }

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
