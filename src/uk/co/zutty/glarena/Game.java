package uk.co.zutty.glarena;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import uk.co.zutty.glarena.util.MatrixUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL20.*;
import static uk.co.zutty.glarena.util.MathUtils.degreesToRadians;

public class Game {
    // Entry point for the application
    public static void main(String[] args) {
        new Game();
    }

    // Setup variables
    private final String WINDOW_TITLE = "3D Game";
    private final int WIDTH = 1024;
    private final int HEIGHT = 768;
    // Quad variables
    //private int vaoId = 0;
    //private int vboId = 0;
    //private int vboiId = 0;
    //private int indicesCount = 0;
    private List<Vertex> vertices = null;
    private ByteBuffer verticesByteBuffer = null;
    // Shader variables
    private ShaderProgram shader;
    // Texture variables
    private int[] texIds = new int[] {0, 0};
    private int textureSelector = 0;
    // Moving variables
    //private int projectionMatrixLocation = 0;
    //private int viewMatrixLocation = 0;
    //private int modelMatrixLocation = 0;
    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private Vector3f modelPos = null;
    private Vector3f modelAngle = null;
    private Vector3f modelScale = null;
    private Vector3f cameraPos = null;
    private FloatBuffer matrix44Buffer = null;
    private Mesh cube;

    public Game() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glClearDepth(1);


        this.setupTextures();
        this.setupQuad();
        this.setupShaders();
        this.setupMatrices();

        Display.setVSyncEnabled(true);

        while (!Display.isCloseRequested()) {
            // Do a single loop (logic/render)
            this.loopCycle();

            Display.update();
            // Force a maximum FPS of about 60
            Display.sync(60);
            // Let the CPU synchronize with the GPU if GPU is tagging behind
        }

        // Destroy OpenGL (Display)
        this.destroyOpenGL();
    }

    private void setupMatrices() {
        // Setup projection matrix
        projectionMatrix = MatrixUtils.frustum(Display.getWidth(), Display.getHeight(), 60, 0.1f, 100.0f);

        // Setup view matrix
        viewMatrix = new Matrix4f();

        // Setup model matrix
        modelMatrix = new Matrix4f();

        // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
    }

    private void setupTextures() {
        //texIds[0] = this.loadPNGTexture("/textures/checkerboard.png", GL13.GL_TEXTURE0);
        //texIds[1] = this.loadPNGTexture("/textures/gunship_diffuse.png", GL13.GL_TEXTURE0);

        texIds[1] = TextureLoader.loadTexture("/textures/gunship_diffuse.png");

        this.exitOnGLError("setupTexture");
    }

    private void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                    .withForwardCompatible(true)
                    .withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, contextAtrributes);

            GL11.glViewport(0, 0, WIDTH, HEIGHT);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        this.exitOnGLError("setupOpenGL");
    }

    private Vertex vertex(float x, float y, float z, float r, float g, float b, float s, float t) {
        Vertex v = new Vertex();
        v.setPosition(new Vector3f(x, y, z)); v.setNormal(new Vector3f(r, g, b)); v.setTexCoord(new Vector2f(s, t));
        return v;
    }

    private void setupQuad() {
        /*
        // We'll define our quad using 4 vertices of the custom 'TexturedVertex' class
        vertices = new ArrayList<Vertex>();
        // front
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 0, 0));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 0, 1));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 1, 0));

        //right
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 0, 0));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 1.0f, 0, 0, 0, 1));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 1.0f, 0, 0, 1, 0));

        //back
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 0, 0));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 1, 1));
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 1, 0));

        // left
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, -1.0f, 0, 0, 0, 0));
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, -1.0f, 0, 0, 1, 1));
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1, 0));

        //top
        vertices.add(vertex(-0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 0));
        vertices.add(vertex(0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 1));
        vertices.add(vertex(0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 1));
        vertices.add(vertex(-0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 0));

        //bottom
        vertices.add(vertex(-0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 0));
        vertices.add(vertex(0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 1));
        vertices.add(vertex(0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 1));
        vertices.add(vertex(-0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 0));

        // Put each 'Vertex' in one FloatBuffer
        verticesByteBuffer = BufferUtils.createByteBuffer(vertices.size() *
                Vertex.STRIDE);
        FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
        for (Vertex vertex: vertices) {
            // Add position, color and texture floats to the buffer
            vertex.put(verticesFloatBuffer);
        }
        verticesFloatBuffer.flip();


        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = {
                0, 1, 2,
                2, 3, 0,

                4, 5, 6,
                6, 7, 4,

                8, 9, 10,
                10, 11, 8,

                12, 13, 14,
                14, 15, 12,

                16, 17, 18,
                18, 19, 16,

                20, 21, 22,
                22, 23, 20
        };
        indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);

        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENTS, GL11.GL_FLOAT,
                false, Vertex.STRIDE, Vertex.POSITION_OFFSET);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, Vertex.NORMAL_ELEMENTS, GL11.GL_FLOAT,
                false, Vertex.STRIDE, Vertex.NORMAL_OFFSET);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, Vertex.TEXCOORD_ELEMENTS, GL11.GL_FLOAT,
                false, Vertex.STRIDE, Vertex.TEXCOORD_OFFSET);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        // Create a new VBO for the indices and select it (bind) - INDICES
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        */

        // Set the default quad rotation, scale and position values
        modelPos = new Vector3f(0, 0, -1);
        modelAngle = new Vector3f(0, 0, 0);
        modelScale = new Vector3f(1, 1, 1);
        cameraPos = new Vector3f(0, 0, 0);

        this.exitOnGLError("setupQuad");
        /*
        Model m = new Model();

        // front
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 0, 0));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 0, 1));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 0, 0, 1.0f, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 0, 0, 1.0f, 1, 0));

        //right
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 0, 0));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 1.0f, 0, 0, 0, 1));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 1.0f, 0, 0, 1, 0));

        //back
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 0, 0));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, 0, 0, -1.0f, 1, 1));
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, 0, 0, -1.0f, 1, 0));

        // left
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, -1.0f, 0, 0, 0, 0));
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, -1.0f, 0, 0, 1, 1));
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1, 0));

        //top
        m.addVertex(vertex(-0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 0));
        m.addVertex(vertex(0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1, 1));
        m.addVertex(vertex(0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 1));
        m.addVertex(vertex(-0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0, 0));

        //bottom
        m.addVertex(vertex(-0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 0));
        m.addVertex(vertex(0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1, 1));
        m.addVertex(vertex(0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 1));
        m.addVertex(vertex(-0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0, 0));

        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = {
                0, 1, 2,
                2, 3, 0,

                4, 5, 6,
                6, 7, 4,

                8, 9, 10,
                10, 11, 8,

                12, 13, 14,
                14, 15, 12,

                16, 17, 18,
                18, 19, 16,

                20, 21, 22,
                22, 23, 20
        };
        for(int i: indices) m.addIndex((byte)i);
        */

        cube =  Mesh.fromModel(new ObjLoader().loadModel("/models/gunship.obj"), texIds[1]);
    }
    private void setupShaders() {
        // Load the vertex shader
        /*
        vsId = this.loadShader("/shaders/vec.glsl",
                GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        fsId = this.loadShader("/shaders/frag.glsl",
                GL20.GL_FRAGMENT_SHADER);

        // Create a new shader program that links both shaders
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);
        */

        shader = ShaderProgram.build("/shaders/vec.glsl", "/shaders/frag.glsl");

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "foobar");
        shader.bindAttribLocation(2, "in_TextureCoord");

        shader.link();
        shader.validate();

        // Get matrices uniform locations
        shader.initUniform("projectionMatrix");
        shader.initUniform("viewMatrix");
        shader.initUniform("modelMatrix");
        //projectionMatrixLocation = GL20.glGetUniformLocation(shader.getHandle(), "projectionMatrix");
        //viewMatrixLocation = GL20.glGetUniformLocation(shader.getHandle(), "viewMatrix");
        //modelMatrixLocation = GL20.glGetUniformLocation(shader.getHandle(), "modelMatrix");

        //glLinkProgram(shader.getHandle());

        this.exitOnGLError("setupShaders");
    }


    private void logicCycle() {
        //-- Input processing
        float rotationDelta = 15f;
        float scaleDelta = 0.1f;
        float posDelta = 0.1f;
        Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
        Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta,
                -scaleDelta);

        /*
        while(Keyboard.next()) {
            // Only listen to events where the key was pressed (down event)
            if (!Keyboard.getEventKeyState()) continue;

            // Switch textures depending on the key released
            switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_1:
                    textureSelector = 0;
                    break;
                case Keyboard.KEY_2:
                    textureSelector = 1;
                    break;
            }

            // Change model scale, rotation and translation values
            switch (Keyboard.getEventKey()) {
                // Move
                case Keyboard.KEY_UP:
                    modelPos.y += posDelta;
                    break;
                case Keyboard.KEY_DOWN:
                    modelPos.y -= posDelta;
                    break;
                // Scale
                case Keyboard.KEY_P:
                    Vector3f.add(modelScale, scaleAddResolution, modelScale);
                    break;
                case Keyboard.KEY_M:
                    Vector3f.add(modelScale, scaleMinusResolution, modelScale);
                    break;
                // Rotation
                case Keyboard.KEY_LEFT:
                    modelAngle.z += rotationDelta;
                    break;
                case Keyboard.KEY_RIGHT:
                    modelAngle.z -= rotationDelta;
                    break;
                case Keyboard.KEY_W:
                    cameraPos.z -= 0.01f;
                    break;
                case Keyboard.KEY_S:
                    cameraPos.z += 0.01f;
                    break;
                case Keyboard.KEY_A:
                    cameraPos.x -= 0.01f;
                    break;
                case Keyboard.KEY_D:
                    cameraPos.x += 0.01f;
                    break;
            }
        }
        */

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cameraPos.z -= 0.01f;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cameraPos.z += 0.01f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cameraPos.x -= 0.01f;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cameraPos.x += 0.01f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            cameraPos.y -= 0.01f;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cameraPos.y += 0.01f;
        }

        //modelAngle.x += 0.5f;
        modelAngle.y += 1f;
        //modelAngle.z += 0.7f;

        //-- Update matrices
        // Reset view and model matrices
        viewMatrix =  MatrixUtils.lookAt(cameraPos.x, cameraPos.y, cameraPos.z,
                0.0f, 0.0f, -1f,
                0.0f, 1.0f, 0.0f);
        modelMatrix = new Matrix4f();

        // Translate camera
        //viewMatrix.setIdentity();
        //Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);

        // Scale, translate and rotate model
        Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
        Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
        Matrix4f.rotate(degreesToRadians(modelAngle.z), new Vector3f(0, 0, 1),
                modelMatrix, modelMatrix);
        Matrix4f.rotate(degreesToRadians(modelAngle.y), new Vector3f(0, 1, 0),
                modelMatrix, modelMatrix);
        Matrix4f.rotate(degreesToRadians(modelAngle.x), new Vector3f(1, 0, 0),
                modelMatrix, modelMatrix);

        // Upload matrices to the uniform variables
        GL20.glUseProgram(shader.getHandle());

        /*
        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
        */
        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", viewMatrix);
        shader.setUniform("modelMatrix", modelMatrix);

        GL20.glUseProgram(0);

        this.exitOnGLError("logicCycle");
    }

    private void renderCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        GL20.glUseProgram(shader.getHandle());

        cube.render();
                 /*
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIds[textureSelector]);

        // Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
                                 */
        GL20.glUseProgram(0);

        this.exitOnGLError("renderCycle");
    }

    private void loopCycle() {
        // Update logic
        this.logicCycle();
        // Update rendered frame
        this.renderCycle();

        this.exitOnGLError("loopCycle");
    }

    private void destroyOpenGL() {
        // Delete the texture
        GL11.glDeleteTextures(texIds[0]);
        GL11.glDeleteTextures(texIds[1]);

        // Delete the shaders
        GL20.glUseProgram(0);
        //GL20.glDetachShader(shader.getHandle(), vsId);
        //GL20.glDetachShader(shader.getHandle(), fsId);

        ///GL20.glDeleteShader(vsId);
        //GL20.glDeleteShader(fsId);
        GL20.glDeleteProgram(shader.getHandle());

        // Select the VAO
        //GL30.glBindVertexArray(ftvvaoId);

        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        // Delete the vertex VBO
        //GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        //GL15.glDeleteBuffers(vboId);

        // Delete the index VBO
        //GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        //GL15.glDeleteBuffers(vboiId);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        //GL30.glDeleteVertexArrays(vaoId);

        this.exitOnGLError("destroyOpenGL");

        Display.destroy();
    }

    private int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            int len = glGetShader(shaderID, GL_INFO_LOG_LENGTH);
            String msg = glGetShaderInfoLog(shaderID, len);
            System.err.println("Could not compile shader." + msg);
            System.exit(-1);
        }

        this.exitOnGLError("loadShader");

        return shaderID;
    }

    private int loadPNGTexture(String filename, int textureUnit) {
        ByteBuffer buf = null;
        int tWidth = 0;
        int tHeight = 0;

        try {
            // Open the PNG file as an InputStream
            InputStream in = getClass().getResourceAsStream(filename);
            // Link the PNG decoder to this stream
            PNGDecoder decoder = new PNGDecoder(in);

            // Get the width and height of the texture
            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();


            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
            buf.flip();

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);

        this.exitOnGLError("loadPNGTexture");

        return texId;
    }

    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();

        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);

            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}
