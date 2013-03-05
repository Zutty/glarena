package uk.co.zutty.glarena;

/**
 * Concrete game class for the glArena game.
 */
public class Arena extends Game {

    private ShaderProgram shader;

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

        camera.setPosition(0f, 10f, -5f);

        Model gunshipModel = Model.fromMesh(new ObjLoader().loadMesh("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"));
        Model ufoModel = Model.fromMesh(new ObjLoader().loadMesh("/models/ufo.obj"), TextureLoader.loadTexture("/textures/ufo.png"));

        Gunship a = new Gunship(gunshipModel, shader);
        a.setPosition(4.5f, 0, -1);
        add(a);

        Ufo b = new Ufo(ufoModel, shader);
        b.setPosition(-4.5f, 0, -1);
        add(b);
    }

    public static void main(String... args) {
        new Arena();
    }
}
