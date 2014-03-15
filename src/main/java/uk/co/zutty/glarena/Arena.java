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

package uk.co.zutty.glarena;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.*;
import uk.co.zutty.glarena.gl.ArrayModel;
import uk.co.zutty.glarena.gl.ElementArrayModel;
import uk.co.zutty.glarena.gl.Model;
import uk.co.zutty.glarena.shaders.*;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    public static final Vector3f V = new Vector3f();

    private ObjLoader objLoader;
    private Gunship player;
    private Model ufoModel;

    private Gamepad gamepad;

    private Emitter playerBulletEmitter;
    private Effect explosionEffect;

    private Vector3f arenaCentre;

    private int spawnTimer = 0;
    private int waveTimer = 0;
    private int waveSpawn = 0;

    @Override
    protected void init() {
        camera.setPosition(0f, 20f, -25f);

        for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if (controller.getType() == Controller.Type.GAMEPAD) {
                gamepad = new Gamepad(controller);
            }
        }
        if (gamepad == null) {
            gamepad = new KeyboardGamepad();
        }

        objLoader = new ObjLoader();

        Technique entityTechnique = new EntityTechnique();
        Model gunshipModel = createModel(entityTechnique, objLoader.loadEntityMesh("/models/gunship.obj"));
        ufoModel = createModel(entityTechnique, objLoader.loadEntityMesh("/models/ufo.obj"));

        Technique unlitTechnique = new UnlitTechnique();
        Model ringModel = createModel(unlitTechnique, objLoader.loadUnlitMesh("/models/circle.obj"));

        ArrayModel skyQuadModel = new ArrayModel(new SkyboxTechnique());
        FloatBuffer buffer = BufferUtils.createFloatBuffer(8);
        buffer.put(new float[]{-1, -1, 1, -1, -1, 1, 1, 1});
        buffer.flip();
        skyQuadModel.setVertexData(buffer, 4);
        ModelInstance skyQuad = new ModelInstance(skyQuadModel, TextureLoader.loadCubemap("/textures/skybox/basic"));
        addBackground(skyQuad);

        BulletTechnique bulletTechnique = new BulletTechnique();
        BillboardTechnique billboardTechnique = new BillboardTechnique();
        PlanarTechnique planarTechnique = new PlanarTechnique();

        playerBulletEmitter = new Emitter(bulletTechnique, TextureLoader.loadTexture("/textures/shot.png"), BulletParticle.class);
        addTransparent(playerBulletEmitter);

        explosionEffect = new Explosion(billboardTechnique, bulletTechnique, planarTechnique);
        add(explosionEffect);

        player = new Gunship(new ModelInstance(gunshipModel, TextureLoader.loadTexture("/textures/gunship_diffuse.png")));
        player.setPosition(4.5f, 0, -1);
        player.setBulletEmitter(playerBulletEmitter);
        player.setGamepad(gamepad);
        add(player);

        arenaCentre = new Vector3f(0, 0, 0);

        Marker ringMarker = new Marker(new ModelInstance(ringModel, TextureLoader.loadTexture("/textures/circle.png")));
        ringMarker.getPosition().y = -1;
        add(ringMarker);

        Util.checkGLError();
    }

    private Model createModel(Technique technique, Mesh mesh) {
        ElementArrayModel model = new ElementArrayModel(technique);

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(mesh.getVertices().size() * technique.getFormat().getElements());

        for (Vertex vertex : mesh.getVertices()) {
            vertex.put(vertexData);
        }

        vertexData.flip();
        model.setVertexData(vertexData, mesh.getVertices().size());

        ShortBuffer indexData = BufferUtils.createShortBuffer(mesh.getIndices().size());

        for (short index : mesh.getIndices()) {
            indexData.put(index);
        }

        indexData.flip();
        model.setIndexData(indexData);

        return model;
    }

    public void spawnUfo() {
        Ufo ufo = new Ufo(new ModelInstance(ufoModel, TextureLoader.loadTexture("/textures/ufo.png")), playerBulletEmitter, explosionEffect);
        ufo.setGame(this);
        ufo.setPosition(-4.5f, 0, -1);
        add(ufo);
    }

    @Override
    protected void update() {
        if (gamepad != null) {
            gamepad.update();
        }

        if (++waveTimer > 200) {
            waveSpawn = 0;
            waveTimer = 0;
        }

        if (++spawnTimer > 15 && waveSpawn < 6) {
            spawnTimer = 0;
            ++waveSpawn;
            spawnUfo();
        }

        super.update();

        Vector3f.sub(arenaCentre, player.getPosition(), V);
        V.scale(0.9f);

        camera.setPosition(arenaCentre.x - V.x, 20f, arenaCentre.z - 25f - V.z);
        camera.setCenter(arenaCentre.x - V.x, 0f, arenaCentre.z - V.z);
        camera.update();
    }

    public static void main(String... args) {
        new Arena();
    }
}
