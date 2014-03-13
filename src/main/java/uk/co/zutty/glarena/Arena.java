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
import uk.co.zutty.glarena.gl.ElementArrayModel;
import uk.co.zutty.glarena.gl.Model;
import uk.co.zutty.glarena.shaders.*;
import uk.co.zutty.glarena.util.MathUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static uk.co.zutty.glarena.engine.Tween.Easing.LINEAR;
import static uk.co.zutty.glarena.engine.Tween.Easing.QUAD_INOUT;
import static uk.co.zutty.glarena.util.MathUtils.*;

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
    private Emitter explosionFireballEmitter;
    private Emitter explosionFlashEmitter;
    private Emitter explosionSparkEmitter;
    private Emitter explosionWakeEmitter;

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

        BulletTechnique bulletTechnique = new BulletTechnique();
        BillboardTechnique billboardTechnique = new BillboardTechnique();
        PlanarTechnique planarTechnique = new PlanarTechnique();

        playerBulletEmitter = new Emitter(bulletTechnique, TextureLoader.loadTexture("/textures/shot.png"), BulletParticle.class);
        add(playerBulletEmitter, true);

        explosionFireballEmitter = new Emitter(billboardTechnique, TextureLoader.loadTexture("/textures/explosion.png"), BillboardParticle.class);
        add(explosionFireballEmitter, true);

        explosionFlashEmitter = new Emitter(billboardTechnique, TextureLoader.loadTexture("/textures/flash.png"), BillboardParticle.class);
        add(explosionFlashEmitter, true);

        explosionSparkEmitter = new Emitter(bulletTechnique, TextureLoader.loadTexture("/textures/spark.png"), BulletParticle.class);
        add(explosionSparkEmitter, true);

        explosionWakeEmitter = new Emitter(planarTechnique, TextureLoader.loadTexture("/textures/wake.png"), BillboardParticle.class);
        add(explosionWakeEmitter, true);

        player = new Gunship(new ModelInstance(gunshipModel, TextureLoader.loadTexture("/textures/gunship_diffuse.png")));
        player.setPosition(4.5f, 0, -1);
        player.setBulletEmitter(playerBulletEmitter);
        player.setGamepad(gamepad);
        add(player, false);

        arenaCentre = new Vector3f(0, 0, 0);

        Marker ringMarker = new Marker(new ModelInstance(ringModel, TextureLoader.loadTexture("/textures/circle.png")));
        ringMarker.getPosition().y = -1;
        add(ringMarker, false);

        Util.checkGLError();
    }

    public void explode(Vector3f at) {
        BillboardParticle flashParticle = (BillboardParticle) explosionFlashEmitter.emitFrom(at, UP, 0f, 10);
        flashParticle.setFade(new Tween(0f, 1f, QUAD_INOUT));
        flashParticle.setScale(new Tween(0f, 5f, QUAD_INOUT));

        BillboardParticle wakeParticle = (BillboardParticle) explosionWakeEmitter.emitFrom(at, UP, 0f, randRange(15, 25));
        wakeParticle.setRotation(randAngle());
        wakeParticle.setFade(new Tween(1f, 0f, LINEAR));
        wakeParticle.setScale(new Tween(0f, randRange(15f, 20f), LINEAR));

        int numSparks = randRange(5, 10);
        for (int i = 0; i < numSparks; i++) {
            Particle spark = explosionSparkEmitter.emitFrom(at, randomDirection(), randRange(.6f, .8f), randRange(5, 20));
            spark.setScale(new Tween(1f, 1f, LINEAR));
            spark.setFade(new Tween(1f, 0f, LINEAR));
        }

        int numFireballs = randRange(10, 20);
        for (int i = 0; i < numFireballs; i++) {
            BillboardParticle billboard = (BillboardParticle) explosionFireballEmitter.emitFrom(at, randomDirection(), randRange(0.05f, 0.2f), randRange(15, 25));
            billboard.setRotation(randAngle());
            billboard.setRotationSpeed(randRange(0.01f, 0.02f));
            billboard.setScale(new Tween(randRange(.8f, 1.2f), randRange(3f, 6f), LINEAR));
            billboard.setFade(new Tween(1f, 0f, LINEAR));
        }
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
        Ufo ufo = new Ufo(new ModelInstance(ufoModel, TextureLoader.loadTexture("/textures/ufo.png")), playerBulletEmitter);
        ufo.setGame(this);
        ufo.setPosition(-4.5f, 0, -1);
        add(ufo, false);
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
