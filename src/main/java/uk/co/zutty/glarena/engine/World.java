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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.util.MatrixUtils;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static uk.co.zutty.glarena.engine.Layer.*;

public class World {

    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Camera camera = new Camera();

    private List<Entity> entities = new ArrayList<>();
    private Collection<Entity> toRemove = new ArrayList<>();
    private Map<Layer, List<ModelInstance>> instances = new HashMap<>();

    public World() {
        for(Layer l : Layer.values()) {
            instances.put(l, new ArrayList<ModelInstance>());
        }
    }

    public void init() {}

    public void add(ModelInstance instance, Layer layer) {
        instances.get(layer).add(instance);
    }

    public void add(Entity entity, Layer layer) {
        entities.add(entity);
        instances.get(layer).add(entity.getModelInstance());
    }

    public void add(Effect effect) {
        for (Entity entity : effect.getEmitters()) {
            add(entity, TRANSPARENT);
        }
    }

    public void remove(Entity entity) {
        toRemove.add(entity);
    }

    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }

        // Update list
        for (Entity r : toRemove) {
            entities.remove(r);
            for(List<ModelInstance> list : instances.values()) {
                list.remove(r.getModelInstance());
            }
        }
        toRemove.clear();

        Util.checkGLError();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDepthMask(false);

        renderList(BACKGROUND);

        glDepthMask(true);

        renderList(DEFAULT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        renderList(TRANSPARENT);

        glDisable(GL_DEPTH_TEST);

        renderList(FOREGROUND);

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);

        Util.checkGLError();
    }

    private void renderList(Layer layer) {
        for (ModelInstance instance : instances.get(layer)) {
            Technique technique = instance.getModel().getTechnique();
            technique.setCamera(camera);
            technique.setProjectionMatrix(projectionMatrix);

            technique.renderInstance(instance);
        }
    }
}
