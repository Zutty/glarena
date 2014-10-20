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
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.engine.*;
import uk.co.zutty.glarena.gl.ArrayModel;
import uk.co.zutty.glarena.gl.ElementArrayModel;
import uk.co.zutty.glarena.gl.Model;
import uk.co.zutty.glarena.gl.enums.TextureFormat;
import uk.co.zutty.glarena.shaders.*;
import uk.co.zutty.glarena.util.MathUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static uk.co.zutty.glarena.EventType.SPAWN_UFO;
import static uk.co.zutty.glarena.gl.enums.BufferUsage.STATIC;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    public static final Vector3f V = new Vector3f();

    public static final float TIMESCALE = 0.2f;

    private ObjLoader objLoader;
    private Gunship player;
    private Model ufoModel;

    private Gamepad gamepad;

    private Emitter playerBulletEmitter;
    private Effect explosionEffect;

    private long score = 0L;
    private TextInstance scoreText;

    private long timer = 0;
    private Vector3f currentPosition;

    private Queue<Event> eventQueue;

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
        Model spaceStationModel = createModel(entityTechnique, objLoader.loadEntityMesh("/models/space_station.obj"));

        ArrayModel skyQuadModel = new ArrayModel(new SkyboxTechnique(), STATIC);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(8);
        buffer.put(new float[]{-1, -1, 1, -1, -1, 1, 1, 1});
        buffer.flip();
        skyQuadModel.getVertexBuffer().setData(buffer, 4);
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

        currentPosition = new Vector3f(0f, 0f, 0f);

        SpaceStation spaceStation = new SpaceStation(new ModelInstance(spaceStationModel, TextureLoader.loadTexture("/textures/space_station_texture.png")));
        spaceStation.setPosition(-30f, -150f, 150f);
        add(spaceStation);

        ArrayModel hudModel = new ArrayModel(new HudTechnique(), STATIC);
        hudModel.getVertexBuffer().setData(makePanelLeft(.1f, .7f, .2f, .2f), 4);
        ModelInstance hudQuad = new ModelInstance(hudModel, TextureLoader.loadTexture("/textures/test.png"));
        addForeground(hudQuad);

        Text text = new Text(new TextTechnique(), TextureLoader.loadTexture("/textures/numbers.png", TextureFormat.RED));
        scoreText = new TextInstance(.4f, .7f, "0");
        text.add(scoreText);
        addForeground(text);

        eventQueue = new LinkedList<>();

        addWave(80, new Vector3f(50f, 0f, 0f), MathUtils.unitVector(150), 3);
        addWave(250, new Vector3f(-50f, 0f, 0f), MathUtils.unitVector(30), 3);
        addWave(420, new Vector3f(50f, 0f, 0f), MathUtils.unitVector(150), 3);
        addWave(500, new Vector3f(20f, 0f, -40f), new Vector3f(0, 0, 1f), 3);
        addWave(600, new Vector3f(-20f, 0f, -40f), new Vector3f(0, 0, 1f), 3);

        Util.checkGLError();
    }

    private void addWave(long time, Vector3f startPosition, Vector3f direction, int size) {
        startPosition.z += (float) time * TIMESCALE;

        for (int i = 0; i < size; i++) {
            V.set(direction);
            V.scale(i * 10f);

            Event event = new Event();
            event.setTime(time);
            event.setPosition(Vector3f.sub(startPosition, V, new Vector3f()));
            event.setDirection(direction);
            event.setType(SPAWN_UFO);
            eventQueue.add(event);
        }
    }

    private FloatBuffer makePanelLeft(float xOffset, float y, float width, float height) {
        float ratio = (float) Display.getWidth() / (float) Display.getHeight();
        float x = -1f + xOffset / ratio;
        width /= ratio;

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(new float[]{
                x, y, 0, 1,
                x + width, y, 1, 1,
                x, y + height, 0, 0,
                x + width, y + height, 1, 0
        });
        buffer.flip();
        return buffer;
    }

    private Model createModel(Technique technique, Mesh mesh) {
        ElementArrayModel model = new ElementArrayModel(technique, STATIC);

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(mesh.getVertices().size() * technique.getFormat().getElements());

        for (Vertex vertex : mesh.getVertices()) {
            vertex.put(vertexData);
        }

        vertexData.flip();
        model.getVertexBuffer().setData(vertexData, mesh.getVertices().size());

        ShortBuffer indexData = BufferUtils.createShortBuffer(mesh.getIndices().size());

        for (short index : mesh.getIndices()) {
            indexData.put(index);
        }

        indexData.flip();
        model.getIndexBuffer().setData(indexData);

        return model;
    }

    public void handleEvent(Event event) {
        Ufo ufo = new Ufo(new ModelInstance(ufoModel, TextureLoader.loadTexture("/textures/ufo.png")), playerBulletEmitter, explosionEffect);
        ufo.setGame(this);
        ufo.setPosition(event.getPosition());
        ufo.setVelocity(event.getDirection());
        add(ufo);
    }

    public void addScore(long points) {
        score += points;
        scoreText.setText(String.valueOf(score));
    }

    @Override
    protected void update() {
        if (gamepad != null) {
            gamepad.update();
        }

        while (eventQueue.peek() != null && eventQueue.peek().getTime() <= timer) {
            handleEvent(eventQueue.poll());
        }

        ++timer;
        currentPosition.z = timer * TIMESCALE; // TODO is this going to cause bugs?

        player.setLevelPosition(currentPosition);

        super.update();

        Vector3f.sub(currentPosition, player.getPosition(), V);
        V.scale(0.1f);

        camera.setPosition(currentPosition.x - V.x, 50f, currentPosition.z - V.z - 15f);
        camera.setCenter(currentPosition.x - V.x, 0f, currentPosition.z - V.z);
        camera.update();
    }

    public static void main(String... args) {
        new Arena();
    }
}
