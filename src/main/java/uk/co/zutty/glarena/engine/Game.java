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
import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.util.MatrixUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Game {

    protected Matrix4f projectionMatrix = null;
    protected Camera camera;

    private List<Entity> entities = new ArrayList<>();
    private Collection<Entity> toRemove = new ArrayList<>();
    private List<ModelInstance> backgroundInstances = new ArrayList<>();
    private List<ModelInstance> instances = new ArrayList<>();
    private List<ModelInstance> transparentInstances = new ArrayList<>();
    private List<ModelInstance> foreground = new ArrayList<>();

    public Game() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();

        setup();
        init();
        Util.checkGLError();

        Display.setVSyncEnabled(true);

        while (!Display.isCloseRequested()) {
            update();
            render();

            Display.update();
            Display.sync(60);
        }

        destroyOpenGL();
    }

    private void setup() {
        projectionMatrix = MatrixUtils.frustum(Display.getWidth(), Display.getHeight(), 60, 0.1f, 100.0f);
        camera = new Camera();
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

    protected abstract void init();

    public void addBackground(ModelInstance instance) {
        backgroundInstances.add(instance);
    }

    public void add(Entity entity) {
        entities.add(entity);
        instances.add(entity.getModelInstance());
    }

    public void addTransparent(Entity entity) {
        entities.add(entity);
        transparentInstances.add(entity.getModelInstance());
    }

    public void add(Effect effect) {
        for (Entity entity : effect.getEmitters()) {
            addTransparent(entity);
        }
    }

    public void addForeground(ModelInstance instance) {
        foreground.add(instance);
    }
    public void addForeground(Entity entity) {
        entities.add(entity);
        foreground.add(entity.getModelInstance());
    }

    public void remove(Entity entity) {
        toRemove.add(entity);
    }

    protected void update() {
        for (Entity entity : entities) {
            entity.update();
        }

        // Update list
        for (Entity r : toRemove) {
            entities.remove(r);
            instances.remove(r.getModelInstance());
            transparentInstances.remove(r.getModelInstance());
        }
        toRemove.clear();

        Util.checkGLError();
    }

    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDepthMask(false);

        renderList(backgroundInstances);

        glDepthMask(true);

        renderList(instances);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        renderList(transparentInstances);

        glDisable(GL_DEPTH_TEST);

        renderList(foreground);

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);

        Util.checkGLError();
    }

    private void renderList(List<ModelInstance> list) {
        for (ModelInstance instance : list) {
            Technique technique = instance.getModel().getTechnique();
            technique.setCamera(camera);
            technique.setProjectionMatrix(projectionMatrix);

            technique.renderInstance(instance);
        }
    }

    private void destroyOpenGL() {
        Util.checkGLError();
        Display.destroy();
    }
}