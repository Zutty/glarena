package uk.co.zutty.glarena;

/**
 * AI controlled enemy entity.
 */
public class Ufo extends Entity {

    public Ufo(Model model, ShaderProgram shader) {
        super(model, shader);
    }

    @Override
    public void update() {
        yaw += 2;

        super.update();
    }
}
