package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.gl.Model;
import uk.co.zutty.glarena.gl.Texture;

public class ModelInstance {

    private Model model;
    private Texture texture;
    private Matrix4f matrix = new Matrix4f();

    public ModelInstance(Model model, Texture texture) {
        this.model = model;
        this.texture = texture;
    }

    public Model getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }
}
