package uk.co.zutty.glarena;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    private final static Vector3f V = new Vector3f();

    private ShaderProgram shader;
    private ShaderProgram markerShader;
    private Gunship player;

    private Gamepad gamepad;

    private BillboardList billboardList;

    private Vector3f arenaCentre;

    @Override
    protected void init() {
        shader = ShaderProgram.build("/shaders/vec.glsl", "/shaders/frag.glsl");

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "foobar");
        shader.bindAttribLocation(2, "in_TextureCoord");

        shader.link();
        shader.validate();

        markerShader = ShaderProgram.build("/shaders/marker/vertex.glsl", "/shaders/marker/fragment.glsl");
        markerShader.bindAttribLocation(0, "in_Position");
        markerShader.bindAttribLocation(1, "in_TextureCoord");
        markerShader.link();
        markerShader.validate();

        // Get matrices uniform locations
        shader.initUniform("projectionMatrix");
        shader.initUniform("viewMatrix");
        shader.initUniform("modelMatrix");
        markerShader.initUniform("projectionMatrix");
        markerShader.initUniform("viewMatrix");
        markerShader.initUniform("modelMatrix");

        camera.setPosition(0f, 20f, -25f);

        for(Controller controller: ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if(controller.getType() == Controller.Type.GAMEPAD) {
                gamepad = new Gamepad(controller);
            }
        }

        Model gunshipModel = Model.fromMesh(new ObjLoader().loadMesh("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"));
        Model ufoModel = Model.fromMesh(new ObjLoader().loadMesh("/models/ufo.obj"), TextureLoader.loadTexture("/textures/ufo.png"));
        Model ringModel = Model.fromMesh(new ObjLoader().loadMesh("/models/circle.obj"), TextureLoader.loadTexture("/textures/circle.png"));

        billboardList = new BillboardList();
        billboardList.init("/textures/shot.png");

        player = new Gunship(gunshipModel, shader);
        player.setPosition(4.5f, 0, -1);
        player.setBillboardList(billboardList);
        player.setGamepad(gamepad);
        add(player);

        arenaCentre = new Vector3f(0, 0, 0);

        Ufo ufo = new Ufo(ufoModel, shader);
        ufo.setPosition(-4.5f, 0, -1);
        add(ufo);

        Marker ringMarker = new Marker(ringModel, shader);
        ringMarker.position.y = -1;
        add(ringMarker);

        exitOnGLError("init");
    }

    @Override
    protected void update() {
        if(gamepad != null) {
            gamepad.update();
        }

        super.update();

        Vector3f.sub(arenaCentre, player.position, V);
        V.scale(0.9f);

        camera.setPosition(arenaCentre.x - V.x, 20f, arenaCentre.z - 25f - V.z);
        camera.setCenter(arenaCentre.x - V.x, 0f, arenaCentre.z - V.z);
        camera.update();

        billboardList.update();
    }

    @Override
    protected void render() {
        super.render();

        Matrix4f viewProjectionMatrix = new Matrix4f();
        Matrix4f.mul(projectionMatrix, camera.getViewMatrix(), viewProjectionMatrix);

        billboardList.render(viewProjectionMatrix);
    }

    public static void main(String... args) {
        new Arena();
    }
}
