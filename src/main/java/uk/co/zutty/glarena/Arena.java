package uk.co.zutty.glarena;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import uk.co.zutty.glarena.gl.ElementArrayModel;
import uk.co.zutty.glarena.gl.Model;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    public static final Vector3f V = new Vector3f();

    private Gunship player;
    private Model ufoModel;

    private Gamepad gamepad;

    private Emitter playerBulletEmitter;
    private Emitter explosionEmitter;

    private Vector3f arenaCentre;

    private int spawnTimer = 0;
    private int waveTimer = 0;
    private int waveSpawn = 0;

    @Override
    protected void init() {
        camera.setPosition(0f, 20f, -25f);

        for(Controller controller: ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if(controller.getType() == Controller.Type.GAMEPAD) {
                gamepad = new Gamepad(controller);
            }
        }
        if(gamepad == null) {
            gamepad = new KeyboardGamepad();
        }

        Technique entityTechnique = new EntityTechnique();
        Model gunshipModel = createModel(entityTechnique, "/models/gunship.obj", "/textures/gunship_diffuse.png");
        ufoModel = createModel(entityTechnique, "/models/ufo.obj", "/textures/ufo.png");
        Model ringModel = createModel(entityTechnique, "/models/circle.obj", "/textures/circle.png");

        playerBulletEmitter = new Emitter(new BulletTechnique(), TextureLoader.loadTexture("/textures/shot.png"), BulletParticle.class);
        explosionEmitter = new Emitter(new BillboardTechnique(), TextureLoader.loadTexture("/textures/cross.png"), BillboardParticle.class);
        add(playerBulletEmitter);
        add(explosionEmitter);

        player = new Gunship(new ModelInstance(gunshipModel));
        player.setPosition(4.5f, 0, -1);
        player.setBulletEmitter(playerBulletEmitter);
        player.setGamepad(gamepad);
        add(player);

        arenaCentre = new Vector3f(0, 0, 0);

        Marker ringMarker = new Marker(new ModelInstance(ringModel));
        ringMarker.position.y = -1;
        add(ringMarker);

        final double DEG_TO_RAD = Math.PI/180.0;

        for(int i = 0; i < 360; i += 10) {
            float x = (float)Math.sin(i * DEG_TO_RAD) * 10f;
            float z = (float)Math.cos(i * DEG_TO_RAD) * 10f;

            explosionEmitter.emitFrom(new Vector3f(x, 0, z), new Vector3f(1,0,0), 0f);
        }
        explosionEmitter.update();

        exitOnGLError("init");
    }

    private Model createModel(Technique technique, String meshFile, String textureFile) {
        Mesh mesh = new ObjLoader().loadMesh(meshFile);
        int texture = TextureLoader.loadTexture(textureFile);

        ElementArrayModel model = new ElementArrayModel(technique, texture);

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(mesh.getVertices().size() * technique.getFormat().getStride());

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
        Ufo ufo = new Ufo(new ModelInstance(ufoModel), playerBulletEmitter);
        ufo.setPosition(-4.5f, 0, -1);
        add(ufo);
    }

    @Override
    protected void update() {
        if(gamepad != null) {
            gamepad.update();
        }

        if(++waveTimer > 200) {
            waveSpawn = 0;
            waveTimer = 0;
        }

        if(++spawnTimer > 15 && waveSpawn < 6) {
            spawnTimer = 0;
            ++waveSpawn;
            //spawnUfo();
        }

        super.update();

        Vector3f.sub(arenaCentre, player.position, V);
        V.scale(0.9f);

        camera.setPosition(arenaCentre.x - V.x, 20f, arenaCentre.z - 25f - V.z);
        camera.setCenter(arenaCentre.x - V.x, 0f, arenaCentre.z - V.z);
        camera.update();

        playerBulletEmitter.update();
        explosionEmitter.update();
    }

    public static void main(String... args) {
        new Arena();
    }
}
