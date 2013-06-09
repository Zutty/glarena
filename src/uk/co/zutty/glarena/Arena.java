package uk.co.zutty.glarena;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    private ShaderProgram shader;
    private Gunship player;

    private Gamepad gamepad;

    private BillboardList billboardList;

    @Override
    protected void init() {
        shader = ShaderProgram.build("/shaders/vec.glsl", "/shaders/frag.glsl");

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "foobar");
        shader.bindAttribLocation(2, "in_TextureCoord");

        shader.link();
        shader.validate();

        // Get matrices uniform locations
        shader.initUniform("projectionMatrix");
        shader.initUniform("viewMatrix");
        shader.initUniform("modelMatrix");

        camera.setPosition(0f, 20f, -25f);

        for(Controller controller: ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if(controller.getType() == Controller.Type.GAMEPAD) {
                gamepad = new Gamepad(controller);
            }
        }

        Model gunshipModel = Model.fromMesh(new ObjLoader().loadMesh("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"));
        Model ufoModel = Model.fromMesh(new ObjLoader().loadMesh("/models/ufo.obj"), TextureLoader.loadTexture("/textures/ufo.png"));

        billboardList = new BillboardList();
        billboardList.init("/textures/shot.png");

        player = new Gunship(gunshipModel, shader);
        player.setPosition(4.5f, 0, -1);
        player.setBillboardList(billboardList);
        player.setGamepad(gamepad);
        add(player);

        Ufo ufo = new Ufo(ufoModel, shader);
        ufo.setPosition(-4.5f, 0, -1);
        add(ufo);

        exitOnGLError("init");
    }

    @Override
    protected void update() {
        camera.setCenter(player.position.x, player.position.y, -1);

        if(gamepad != null) {
            gamepad.update();
        }

        super.update();

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
