package uk.co.zutty.glarena;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.util.MatrixUtils;

import static org.lwjgl.opengl.GL11.*;
import static uk.co.zutty.glarena.util.MathUtils.degreesToRadians;

public class Game {
    // Entry point for the application
    public static void main(String[] args) {
        new Game();
    }

    private ShaderProgram shader;

    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private Vector3f modelPos = null;
    private Vector3f modelAngle = null;
    private Vector3f modelScale = null;
    private Vector3f cameraPos = null;
    private Model cube;

    public Game() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glClearDepth(1);

        this.setupQuad();
        this.setupShaders();
        this.setupMatrices();

        Display.setVSyncEnabled(true);

        while (!Display.isCloseRequested()) {
            // Do a single loop (logic/render)
            this.loopCycle();

            Display.update();
            Display.sync(60);
        }

        this.destroyOpenGL();
    }

    private void setupMatrices() {
        projectionMatrix = MatrixUtils.frustum(Display.getWidth(), Display.getHeight(), 60, 0.1f, 100.0f);
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    private void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAttributes = new ContextAttribs(3, 2)
                    .withForwardCompatible(true)
                    .withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(1024, 768));
            Display.setTitle("3D Game");
            Display.create(pixelFormat, contextAttributes);

            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

        // Map the internal OpenGL coordinate system to the entire screen
        //GL11.glViewport(0, 0, WIDTH, HEIGHT);

        this.exitOnGLError("setupOpenGL");
    }

    private void setupQuad() {

        // Set the default quad rotation, scale and position values
        modelPos = new Vector3f(0, 0, -1);
        modelAngle = new Vector3f(0, 0, 0);
        modelScale = new Vector3f(1, 1, 1);
        cameraPos = new Vector3f(0, 0, 0);

        this.exitOnGLError("setupQuad");

        cube = Model.fromMesh(new ObjLoader().loadModel("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"));
    }

    private void setupShaders() {
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

        this.exitOnGLError("setupShaders");
    }


    private void logicCycle() {

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cameraPos.z -= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cameraPos.z += 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cameraPos.x -= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cameraPos.x += 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            cameraPos.y -= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cameraPos.y += 0.01f;
        }

        //modelAngle.x += 0.5f;
        modelAngle.y += 1f;
        //modelAngle.z += 0.7f;

        //-- Update matrices
        // Reset view and model matrices
        viewMatrix = MatrixUtils.lookAt(cameraPos.x, cameraPos.y, cameraPos.z,
                0.0f, 0.0f, -1f,
                0.0f, 1.0f, 0.0f);
        modelMatrix = new Matrix4f();

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
        GL20.glUseProgram(shader.getGlObject());

        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", viewMatrix);
        shader.setUniform("modelMatrix", modelMatrix);

        GL20.glUseProgram(0);

        this.exitOnGLError("logicCycle");
    }

    private void renderCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.use();

        cube.render();

        ShaderProgram.useNone();

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
        shader.destroy();

        cube.destroy();

        this.exitOnGLError("destroyOpenGL");

        Display.destroy();
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
