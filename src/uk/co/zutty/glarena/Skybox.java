package uk.co.zutty.glarena;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * Created with IntelliJ IDEA.
 */
public class Skybox {

    private int texture;
    private Camera camera;

    public Skybox(Camera camera) {
        this.camera = camera;

        texture = TextureLoader.loadCubemap("C:\\Users\\George\\Java\\3DGame\\assets\\textures\\skybox\\test");
    }

    public void draw() {
        //glPushAttrib(GL_ENABLE_BIT);
        glPushMatrix();

        //glLoadIdentity();
        glTranslatef(camera.getX(), camera.getY(), camera.getZ());
        //glActiveTexture(GL_TEXTURE0);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_CUBE_MAP);

        // Just in case we set all vertices to white.
        //glColor4f(1, 1, 1, 1);

        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

        // Render the front quad
        glBegin(GL_QUADS);
            glTexCoord3f(1.0f, 1.0f, -1.0f);
            glVertex3f(100.0f, 100.0f, -100.0f);
            glTexCoord3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-100.0f, 100.0f, -100.0f);
            glTexCoord3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-100.0f, -100.0f, -100.0f);
            glTexCoord3f(1.0f, -1.0f, -1.0f);
            glVertex3f(100.0f, -100.0f, -100.0f);
        glEnd();

        // Render the left quad
        glBegin(GL_QUADS);
            glTexCoord3f(1.0f, 1.0f, 1.0f);
            glVertex3f(100.0f, 100.0f, 100.0f);
            glTexCoord3f(1.0f, 1.0f, -1.0f);
            glVertex3f(100.0f, 100.0f, -100.0f);
            glTexCoord3f(1.0f, -1.0f, -1.0f);
            glVertex3f(100.0f, -100.0f, -100.0f);
            glTexCoord3f(1.0f, -1.0f, 1.0f);
            glVertex3f(100.0f, -100.0f, 100.0f);
        glEnd();

        // Render the back quad
        glBegin(GL_QUADS);
            glTexCoord3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-100.0f, 100.0f, 100.0f);
            glTexCoord3f(1.0f, 1.0f, 1.0f);
            glVertex3f(100.0f, 100.0f, 100.0f);
            glTexCoord3f(1.0f, -1.0f, 1.0f);
            glVertex3f(100.0f, -100.0f, 100.0f);
            glTexCoord3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-100.0f, -100.0f, 100.0f);
        glEnd();

        // Render the right quad
        glBegin(GL_QUADS);
            glTexCoord3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-100.0f, 100.0f, -100.0f);
            glTexCoord3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-100.0f, 100.0f, 100.0f);
            glTexCoord3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-100.0f, -100.0f, 100.0f);
            glTexCoord3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-100.0f, -100.0f, -100.0f);
        glEnd();

        // Render the top quad
        glBegin(GL_QUADS);
            glTexCoord3f(1.0f, 1.0f, -1.0f);
            glVertex3f(100.0f, 100.0f, -100.0f);
            glTexCoord3f(1.0f, 1.0f, 1.0f);
            glVertex3f(100.0f, 100.0f, 100.0f);
            glTexCoord3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-100.0f, 100.0f, 100.0f);
            glTexCoord3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-100.0f, 100.0f, -100.0f);
        glEnd();

        // Render the bottom quad
        glBegin(GL_QUADS);
            glTexCoord3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-100.0f, -100.0f, -100.0f);
            glTexCoord3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-100.0f, -100.0f, 100.0f);
            glTexCoord3f(1.0f, -1.0f, 1.0f);
            glVertex3f(100.0f, -100.0f, 100.0f);
            glTexCoord3f(1.0f, -1.0f, -1.0f);
            glVertex3f(100.0f, -100.0f, -100.0f);
        glEnd();

        // Restore enable bits and matrix
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
       // glPopAttrib();
        glPopMatrix();

        glDisable(GL_TEXTURE_CUBE_MAP);
    }
}
