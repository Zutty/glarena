package uk.co.zutty.glarena;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

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
        Model gunshipModel = new Model(entityTechnique, new ObjLoader().loadMesh("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"));
        ufoModel = new Model(entityTechnique, new ObjLoader().loadMesh("/models/ufo.obj"), TextureLoader.loadTexture("/textures/ufo.png"));
        Model ringModel = new Model(entityTechnique, new ObjLoader().loadMesh("/models/circle.obj"), TextureLoader.loadTexture("/textures/circle.png"));

        playerBulletEmitter = new BulletEmitter("/textures/shot.png");
        explosionEmitter = new BillboardEmitter("/textures/cross.png", camera);

        player = new Gunship(gunshipModel.newInstance());
        player.setPosition(4.5f, 0, -1);
        player.setBulletEmitter(playerBulletEmitter);
        player.setGamepad(gamepad);
        add(player);

        arenaCentre = new Vector3f(0, 0, 0);

        Marker ringMarker = new Marker(ringModel.newInstance());
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

    public void spawnUfo() {
        Ufo ufo = new Ufo(ufoModel.newInstance(), playerBulletEmitter);
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

    @Override
    protected void render() {
        super.render();

        Matrix4f viewProjectionMatrix = new Matrix4f();
        Matrix4f.mul(projectionMatrix, camera.getViewMatrix(), viewProjectionMatrix);

        playerBulletEmitter.render(viewProjectionMatrix);
        explosionEmitter.render(viewProjectionMatrix);
    }

    public static void main(String... args) {
        new Arena();
    }
}
