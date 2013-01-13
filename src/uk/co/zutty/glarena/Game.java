package uk.co.zutty.glarena;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.GL11.*;
import static uk.co.zutty.glarena.VectorUtils.asFloatBuffer;


/**
 * Created with IntelliJ IDEA.
 */
public class Game {

    private static Camera cam;
    //private static float[] lightPosition = {-5f, 3f, 10f, 0.0f};
    private static float[] lightPosition = {-1.0f, 0.0f, 0.0f, 0.0f};
    private static Mesh fighter;
    private static Mesh earth;
    private static Skybox skybox;
    private static ShaderProgram shader;
    private static ShaderProgram skyboxShader;
    private static int diffuseModifierUniform;
    private static float pos;

    public static void main(String[] args) {
        setUpDisplay();

        pos = 0f;

        if(!GLContext.getCapabilities().OpenGL20) {
            System.out.println("ONOES!");
            System.exit(1);
        }

        shader = ShaderProgram.build("C:\\Users\\George\\Java\\3DGame\\assets\\vert_norm.glsl", "C:\\Users\\George\\Java\\3DGame\\assets\\frag_norm.glsl");
        //shader.initUniform("colorMap");
        //shader.initUniform("specMap");
        shader.initUniform("normalMap");

        skyboxShader = ShaderProgram.build("C:\\Users\\George\\Java\\3DGame\\assets\\skybox_vertex.glsl", "C:\\Users\\George\\Java\\3DGame\\assets\\skybox_fragment.glsl");

        setUpCamera();
        setUpDisplayLists();
        setUpLighting();
        while (!Display.isCloseRequested()) {
            render();
            checkInput();
            Display.update();
            Display.sync(60);

            pos += 0.01f;
        }
        cleanUp();
        System.exit(0);
    }

    private static void setUpDisplayLists() {
        //fighter = loadMesh("C:\\Users\\George\\Java\\3DGame\\assets\\fighter.obj", "C:\\Users\\George\\Java\\3DGame\\assets\\fighter_tex.png", "C:\\Users\\George\\Java\\3DGame\\assets\\fighter_tex_ao.png", "C:\\Users\\George\\Java\\3DGame\\assets\\fighter_tex_ao.png");
        earth = loadMesh("C:\\Users\\George\\Java\\3DGame\\assets\\planet.obj", "C:\\Users\\George\\Java\\3DGame\\assets\\textures\\earth\\earth_colour.png", "C:\\Users\\George\\Java\\3DGame\\assets\\textures\\earth\\earth_specular.png", "C:\\Users\\George\\Java\\3DGame\\assets\\textures\\earth\\earth_normal.png");

        skybox = new Skybox(cam);
    }

    private static Mesh loadMesh(String modelFile, String textureFile, String specularMapFile, String normalMapFile) {
        Model m = new ObjLoader().loadModel(modelFile);
        int t = TextureLoader.loadTexture(textureFile);
        int sp = TextureLoader.loadTexture(specularMapFile);
        int n = TextureLoader.loadTexture(normalMapFile);
        return Mesh.fromModel(m, t, sp, n);
    }

    private static void checkInput() {
        cam.processMouse(1, 80, -80);
        cam.processKeyboard(16, 0.003f, 0.003f, 0.003f);

        if (Mouse.isButtonDown(0))
            Mouse.setGrabbed(true);
        else if (Mouse.isButtonDown(1))
            Mouse.setGrabbed(false);
    }

    private static void cleanUp() {
        //glDeleteTextures(texture);
        Display.destroy();
    }

    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        cam.applyModelviewMatrix(true);

        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));


        glPushMatrix();

        // Draw the skybox
        skyboxShader.use();
        skybox.draw();

        // Draw the rest of the scene
        shader.use();
        //shader.setUniform("colorMap", 0);
        //shader.setUniform("specMap", 1);
        shader.setUniform("normalMap", 1);

        GL20.glBindAttribLocation(shader.getHandle(), 3, "vTangent");

        glPushMatrix();
        glRotatef(23.4f, 0, 0, 1);
        glRotatef(pos, 0, -1, 0);
        earth.render();
        glPopMatrix();

        /*
        glPushMatrix();
        glTranslatef(10f * (float) Math.cos(pos * Math.PI), 0f, 10f * (float) Math.sin(pos * Math.PI));
        glRotatef(pos * 180, 0, -1, 0);
        glRotatef(30f, 0, 0, 1);
        fighter.render();
        glPopMatrix();*/

        ShaderProgram.useNone();

        glPopMatrix();
    }

    private static void setUpLighting() {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));
        //glLight(GL_LIGHT0, GL_SPECULAR, asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        //glEnable(GL_COLOR_MATERIAL);
        //glColorMaterial(GL_FRONT, GL_DIFFUSE);
        glEnable(GL_TEXTURE_2D);
    }



    private static void setUpCamera() {
        cam = new Camera((float) Display.getWidth()
                / (float) Display.getHeight(), -5f, 3f, 10f);
        cam.setPitch(5f);
        cam.setYaw(12f);
        cam.setFov(70);
        cam.applyProjectionMatrix();
    }

    private static void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(1024, 768));
            Display.setVSyncEnabled(true);
            Display.setTitle("3D Game");
            Display.create();
        } catch (LWJGLException e) {
            System.err.println("The display wasn't initialized correctly. :(");
            Display.destroy();
            System.exit(1);
        }
    }
}
