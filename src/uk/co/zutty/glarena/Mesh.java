package uk.co.zutty.glarena;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureEnvCombine;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static uk.co.zutty.glarena.VectorUtils.asFloatBuffer;
import static uk.co.zutty.glarena.VectorUtils.asFloats;
import static uk.co.zutty.glarena.VectorUtils.zeroArray;

/**
 * Created with IntelliJ IDEA.
 */
public class Mesh {

    private int vboVertexHandle;
    private int vboTexCoordsHandle;
    private int vboNormalHandle;

    private int numVertices;
    private int texture;
    private int specularMap;
    public static final Vector3f V = new Vector3f();

    public Mesh(int texture, int specularMap) {
        this.texture = texture;
        this.specularMap = specularMap;

        vboVertexHandle = glGenBuffers();
        vboTexCoordsHandle = glGenBuffers();
        vboNormalHandle = glGenBuffers();

        numVertices = 0;
    }

    public void bindVertices(FloatBuffer vertices) {
        bindBuffer(vertices, vboVertexHandle);
    }

    public void bindTexCoords(FloatBuffer texCoords) {
        bindBuffer(texCoords, vboTexCoordsHandle);
    }

    public void bindNormals(FloatBuffer normals) {
        bindBuffer(normals, vboNormalHandle);
    }

    protected void bindBuffer(FloatBuffer normals, int handle) {
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
    }

    public static Mesh fromModel(Model model, int texture, int specularMap) {//}, int normalMap) {
        Mesh mesh = new Mesh(texture, specularMap);
        mesh.numVertices = model.getFaces().size() * 3;

        Vector3f s = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f t = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f[] sArr = zeroArray(mesh.numVertices);

        /*
        Vector3f[] tArr = zeroArray(mesh.numVertices);

        for (Face face : model.getFaces()) {
            int idx1 = face.getVertices().getA() - 1;
            int idx2 = face.getVertices().getB() - 1;
            int idx3 = face.getVertices().getC() - 1;

            Vector3f v1 = model.getVertex(idx1);
            Vector3f v2 = model.getVertex(idx2);
            Vector3f v3 = model.getVertex(idx3);

            Vector2f w1 = model.getTexCoord(face.getTexCoords().getA() - 1);
            Vector2f w2 = model.getTexCoord(face.getTexCoords().getB() - 1);
            Vector2f w3 = model.getTexCoord(face.getTexCoords().getC() - 1);

            // Calculate tangents
            float x1 = v2.x - v1.x;
            float x2 = v3.x - v1.x;
            float y1 = v2.y - v1.y;
            float y2 = v3.y - v1.y;
            float z1 = v2.z - v1.z;
            float z2 = v3.z - v1.z;

            float s1 = w2.x - w1.x;
            float s2 = w3.x - w1.x;
            float t1 = w2.y - w1.y;
            float t2 = w3.y - w1.y;

            float r = 1.0f / (s1 * t2 - s2 * t1);
            s.set((t2 * x1 - t1 * x2) * r, (t2 * y1 - t1 * y2) * r, (t2 * z1 - t1 * z2) * r);
            t.set((s1 * x2 - s2 * x1) * r, (s1 * y2 - s2 * y1) * r, (s1 * z2 - s2 * z1) * r);

            Vector3f.add(sArr[idx1], s, sArr[idx1]);
            Vector3f.add(sArr[idx2], s, sArr[idx2]);
            Vector3f.add(sArr[idx3], s, sArr[idx3]);

            Vector3f.add(tArr[idx1], t, tArr[idx1]);
            Vector3f.add(tArr[idx2], t, tArr[idx2]);
            Vector3f.add(tArr[idx3], t, tArr[idx3]);
        }
                          */
        FloatBuffer vertices = BufferUtils.createFloatBuffer(mesh.numVertices * 3);
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(mesh.numVertices * 2);
        FloatBuffer normals = BufferUtils.createFloatBuffer(mesh.numVertices * 3);
        //FloatBuffer tangents = BufferUtils.createFloatBuffer(mesh.numVertices * 4);

        for (Face face : model.getFaces()) {
            int idx1 = face.getVertices().getA() - 1;
            int idx2 = face.getVertices().getB() - 1;
            int idx3 = face.getVertices().getC() - 1;

            Vector3f v1 = model.getVertex(idx1);
            Vector3f v2 = model.getVertex(idx2);
            Vector3f v3 = model.getVertex(idx3);

            Vector2f w1 = model.getTexCoord(face.getTexCoords().getA() - 1);
            Vector2f w2 = model.getTexCoord(face.getTexCoords().getB() - 1);
            Vector2f w3 = model.getTexCoord(face.getTexCoords().getC() - 1);

            Vector3f n1 = model.getNormal(face.getNormals().getA() - 1);
            Vector3f n2 = model.getNormal(face.getNormals().getB() - 1);
            Vector3f n3 = model.getNormal(face.getNormals().getC() - 1);

            vertices.put(asFloats(v1));
            vertices.put(asFloats(v2));
            vertices.put(asFloats(v3));

            texCoords.put(asFloats(w1));
            texCoords.put(asFloats(w2));
            texCoords.put(asFloats(w3));

            normals.put(asFloats(n1));
            normals.put(asFloats(n2));
            normals.put(asFloats(n3));

            //putTangents(tangents, n1, sArr[idx1], tArr[idx1]);
            //putTangents(tangents, n2, sArr[idx2], tArr[idx2]);
            //putTangents(tangents, n3, sArr[idx3], tArr[idx3]);
        }

        vertices.flip();
        texCoords.flip();
        normals.flip();

        mesh.bindVertices(vertices);
        mesh.bindTexCoords(texCoords);
        mesh.bindNormals(normals);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return mesh;
    }

    private static void putTangents(FloatBuffer tangents, Vector3f n, Vector3f s, Vector3f t) {
        // Gram-Schmidt orthogonalize
        V.set(n);
        V.scale(Vector3f.dot(n, s));
        Vector3f.sub(s, V, V);
        V.normalise();
        V.store(tangents);

        // Calculate handedness
        Vector3f.cross(n, s, V);
        tangents.put((Vector3f.dot(V, t) < 0.0f) ? -1f : 1f);
    }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glEnable(GL_TEXTURE_2D);
        glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, specularMap);
        glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

        //glActiveTexture(GL_TEXTURE1);
        //glEnable(GL_TEXTURE_2D);
        //glBindTexture(GL_TEXTURE_2D, normalMap);
        //glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, EXTTextureEnvCombine.GL_COMBINE_EXT);
        //glTexEnvf (GL_TEXTURE_ENV, EXTTextureEnvCombine.GL_COMBINE_RGB_EXT, GL_INCR);

        // Vertices
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        // Texture coordinates
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoordsHandle);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);
        // Normals
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0L);
        // Tangents
        //glBindBuffer(GL_ARRAY_BUFFER, vboTangentHandle);
        //glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0L);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        //glEnableVertexAttribArray(3);

        glMaterial(GL_FRONT, GL_DIFFUSE, asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
        glMaterial(GL_FRONT, GL_SPECULAR, asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
        glMaterialf(GL_FRONT, GL_SHININESS, 30f);
        glMaterial(GL_FRONT, GL_AMBIENT, asFloatBuffer(0.0f, 0.0f, 0.0f, 1.0f));

        glDrawArrays(GL_TRIANGLES, 0, numVertices);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        //glDisableVertexAttribArray(3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        glDeleteBuffers(vboVertexHandle);
        glDeleteBuffers(vboTexCoordsHandle);
        glDeleteBuffers(vboNormalHandle);
        //glDeleteBuffers(vboTangentHandle);
    }
}
