package uk.co.zutty.glarena;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.util.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Game {

    private Matrix4f projectionMatrix = null;
    protected Camera camera;
    private List<Entity> entities;

    public Game() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();

        setup();
        init();
        exitOnGLError("init");

        Display.setVSyncEnabled(true);

        while (!Display.isCloseRequested()) {
            update();
            render();

            Display.update();
            Display.sync(60);
        }

        this.destroyOpenGL();
    }

    private void setup() {
        projectionMatrix = MatrixUtils.frustum(Display.getWidth(), Display.getHeight(), 60, 0.1f, 100.0f);
        camera = new Camera();
        entities = new ArrayList<Entity>();
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

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glClearDepth(1);

        exitOnGLError("setupOpenGL");
    }

    protected void init() {}

    public void add(Entity entity) {
        entities.add(entity);
    }

    protected void update() {
        /*if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
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
        }*/

        for(Entity entity: entities) {
            entity.update();
        }

        camera.setCenter(0f, 0f, -1f);
        camera.update();

        this.exitOnGLError("update");
    }

    protected void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for(Entity entity: entities) {
            entity.render(projectionMatrix, camera.getViewMatrix());
        }

        exitOnGLError("render");
    }

    private void destroyOpenGL() {
        for(Entity entity: entities) {
            entity.destroy();
        }

        exitOnGLError("destroyOpenGL");

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
