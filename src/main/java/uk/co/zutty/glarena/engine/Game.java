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

package uk.co.zutty.glarena.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class Game {

    private World world;

    public Game(World world) {
        this.world = world;
    }

    public void run() {
        setupOpenGL();

        world.init();
        Util.checkGLError();

        Display.setVSyncEnabled(true);

        while (!Display.isCloseRequested()) {
            world.update();
            world.render();

            Display.update();
            Display.sync(60);
        }

        destroyOpenGL();
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

            glViewport(0, 0, Display.getWidth(), Display.getHeight());
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Setup an XNA like background color
        glClearColor(0.4f, 0.6f, 0.9f, 0f);
        //glClearColor(0f, 0f, 0f, 0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(ARBSeamlessCubeMap.GL_TEXTURE_CUBE_MAP_SEAMLESS);
        glCullFace(GL_BACK);
        glClearDepth(1);

        Util.checkGLError();
    }

    private void destroyOpenGL() {
        Util.checkGLError();
        Display.destroy();
    }
}
