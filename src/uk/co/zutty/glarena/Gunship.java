package uk.co.zutty.glarena;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public Gunship(Model model, ShaderProgram shader) {
        super(model, shader);
    }

    @Override
    public void update() {
        yaw += 1;

        super.update();
    }
}
